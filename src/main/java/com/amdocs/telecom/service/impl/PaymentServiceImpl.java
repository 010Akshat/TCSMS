package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.AuditLogDAO;
import com.amdocs.telecom.dao.BillingDAO;
import com.amdocs.telecom.dao.PaymentDAO;
import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.dao.impl.AuditLogDAOImpl;
import com.amdocs.telecom.dao.impl.BillingDAOImpl;
import com.amdocs.telecom.dao.impl.PaymentDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionDAOImpl;
import com.amdocs.telecom.model.AuditLog;
import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.BillStatus;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.Payment;
import com.amdocs.telecom.model.PaymentMode;
import com.amdocs.telecom.model.PaymentStatus;
import com.amdocs.telecom.service.PaymentService;
import com.amdocs.telecom.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final BillingDAO billingDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final AuditLogDAO auditLogDAO;

    public PaymentServiceImpl() {

        this.paymentDAO =
                new PaymentDAOImpl();

        this.billingDAO =
                new BillingDAOImpl();

        this.subscriptionDAO =
                new SubscriptionDAOImpl();

        this.auditLogDAO =
                new AuditLogDAOImpl();
    }

    @Override
    public Payment processPayment(
            long billId,
            BigDecimal amount,
            PaymentMode paymentMode) {

        return processPayment(
                billId,
                amount,
                paymentMode,
                PaymentStatus.SUCCESS
        );
    }

    @Override
    public Payment processPayment(
            long billId,
            BigDecimal amount,
            PaymentMode paymentMode,
            PaymentStatus paymentStatus) {

        Connection connection = null;

        try {

            // ==========================================
            // 1. INPUT VALIDATION
            // ==========================================

            if (amount == null) {

                throw new IllegalArgumentException(
                        "Payment amount is mandatory."
                );
            }

            if (amount.compareTo(
                    BigDecimal.ZERO
            ) <= 0) {

                throw new IllegalArgumentException(
                        "Payment amount must be greater than zero."
                );
            }

            if (paymentMode == null) {

                throw new IllegalArgumentException(
                        "Payment mode is mandatory."
                );
            }

            if (paymentStatus == null) {

                throw new IllegalArgumentException(
                        "Payment status is mandatory."
                );
            }

            // ==========================================
            // 2. OPEN TRANSACTION
            // ==========================================

            connection =
                    DBConnection.getConnection();

            connection.setAutoCommit(false);

            // ==========================================
            // 3. VALIDATE BILL
            // ==========================================

            Bill bill =
                    billingDAO.findById(
                            billId,
                            connection
                    );

            if (bill == null) {

                throw new IllegalArgumentException(
                        "Bill not found."
                );
            }

            // ==========================================
            // 4. BILL MUST NOT ALREADY BE PAID
            // ==========================================

            if (bill.getBillStatus()
                    == BillStatus.PAID) {

                throw new IllegalArgumentException(
                        "Bill is already paid."
                );
            }

            // ==========================================
            // 5. VALIDATE AMOUNT
            // ==========================================

            if (amount.compareTo(
                    bill.getTotalAmount()
            ) != 0) {

                throw new IllegalArgumentException(
                        "Payment amount does not match bill amount."
                );
            }

            // ==========================================
            // 6. GET CUSTOMER FROM SUBSCRIPTION
            // ==========================================

            MobileSubscription subscription =
                    subscriptionDAO.findById(
                            bill.getSubscriptionId()
                    );

            if (subscription == null) {

                throw new IllegalArgumentException(
                        "Subscription associated with bill not found."
                );
            }

            long customerId =
                    subscription.getCustomerId();

            // ==========================================
            // 7. CHECK EXISTING PAYMENTS
            // ==========================================

            List<Payment> existingPayments =
                    paymentDAO.findByBillId(
                            billId,
                            connection
                    );

            /*
             * A DECLINED payment is only a failed
             * payment attempt. It must NOT prevent
             * a later successful retry.
             *
             * Only an existing SUCCESS payment
             * prevents another payment.
             */
            boolean successfulPaymentExists =
                    existingPayments.stream()
                            .anyMatch(payment ->
                                    payment.getPaymentStatus()
                                            == PaymentStatus.SUCCESS
                            );

            if (successfulPaymentExists) {

                throw new IllegalArgumentException(
                        "Payment already exists for this bill."
                );
            }

            // ==========================================
            // 8. CREATE PAYMENT
            // ==========================================

            Payment payment =
                    new Payment(
                            0,
                            generateTransactionReference(),
                            billId,
                            customerId,
                            amount.setScale(2),
                            paymentMode,
                            LocalDateTime.now(),
                            paymentStatus
                    );

            paymentDAO.save(
                    payment,
                    connection
            );

            // ==========================================
            // 9. UPDATE BILL ONLY FOR SUCCESS
            // ==========================================

            if (paymentStatus
                    == PaymentStatus.SUCCESS) {

                bill.setBillStatus(
                        BillStatus.PAID
                );

                billingDAO.update(
                        bill,
                        connection
                );
            }

            // ==========================================
            // 10. CREATE AUDIT LOG
            // ==========================================

            String auditAction =
                    paymentStatus
                            == PaymentStatus.SUCCESS
                            ? "PAYMENT_SUCCESS"
                            : "PAYMENT_DECLINED";

            String auditDetails =
                    paymentStatus
                            == PaymentStatus.SUCCESS
                            ? "Payment processed successfully."
                            : "Payment was declined. Bill remains unpaid.";

            AuditLog auditLog =
                    new AuditLog(
                            0,
                            auditAction,
                            payment.getPaymentId(),
                            bill.getBillId(),
                            customerId,
                            LocalDateTime.now(),
                            auditDetails
                    );

            auditLogDAO.save(
                    auditLog,
                    connection
            );

            // ==========================================
            // 11. COMMIT
            // ==========================================

            connection.commit();

            return payment;

        } catch (Exception e) {

            // ==========================================
            // 12. ROLLBACK
            // ==========================================

            if (connection != null) {

                try {

                    connection.rollback();

                } catch (Exception rollbackException) {

                    e.addSuppressed(
                            rollbackException
                    );
                }
            }

            if (e instanceof IllegalArgumentException) {

                throw (IllegalArgumentException) e;
            }

            throw new RuntimeException(
                    "Payment processing failed.",
                    e
            );

        } finally {

            if (connection != null) {

                try {
                    connection.setAutoCommit(true);
                } catch (Exception ignored) {
                }

                try {
                    connection.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public Payment findById(
            long paymentId) {

        return paymentDAO.findById(
                paymentId
        );
    }

    @Override
    public Payment findByTransactionReference(
            String transactionReference) {

        return paymentDAO
                .findByTransactionReference(
                        transactionReference
                );
    }

    @Override
    public List<Payment> findByBillId(
            long billId) {

        return paymentDAO.findByBillId(
                billId
        );
    }

    @Override
    public List<Payment> findByCustomerId(
            long customerId) {

        return paymentDAO.findByCustomerId(
                customerId
        );
    }

    @Override
    public List<Payment> findAll() {

        return paymentDAO.findAll();
    }

    @Override
    public void update(
            Payment payment) {

        paymentDAO.update(
                payment
        );
    }

    @Override
    public void delete(
            long paymentId) {

        paymentDAO.delete(
                paymentId
        );
    }

    private String generateTransactionReference() {

        String reference;

        do {

            reference =
                    "TXN-" +
                            System.currentTimeMillis();

        } while (
                paymentDAO
                        .findByTransactionReference(
                                reference
                        ) != null
        );

        return reference;
    }
}