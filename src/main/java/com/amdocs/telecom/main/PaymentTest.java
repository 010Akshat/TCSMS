package com.amdocs.telecom.main;

import com.amdocs.telecom.dao.AuditLogDAO;
import com.amdocs.telecom.dao.impl.AuditLogDAOImpl;
import com.amdocs.telecom.model.AuditLog;
import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.BillStatus;
import com.amdocs.telecom.model.Payment;
import com.amdocs.telecom.model.PaymentMode;
import com.amdocs.telecom.model.PaymentStatus;
import com.amdocs.telecom.service.BillingService;
import com.amdocs.telecom.service.PaymentService;
import com.amdocs.telecom.service.impl.BillingServiceImpl;
import com.amdocs.telecom.service.impl.PaymentServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PaymentTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        PaymentService paymentService =
                new PaymentServiceImpl();

        BillingService billingService =
                new BillingServiceImpl();

        System.out.println(
                "=== PAYMENT TEST SUITE ==="
        );

        long subscriptionId = 2;

        // ==========================================
        // CREATE A FRESH TEST BILL
        // ==========================================

        LocalDate billingMonth =
                findUnusedBillingMonth(
                        billingService,
                        subscriptionId
                );

        Bill bill =
                billingService.generateBill(
                        subscriptionId,
                        billingMonth,
                        18.0,
                        0.00
                );

        System.out.println(
                "\nTest Bill Created:"
        );

        System.out.println(
                "Bill ID: " +
                        bill.getBillId()
        );

        System.out.println(
                "Bill Number: " +
                        bill.getBillNumber()
        );

        System.out.println(
                "Billing Month: " +
                        bill.getBillingMonth()
        );

        System.out.println(
                "Bill Amount: ₹" +
                        bill.getTotalAmount()
        );

        System.out.println(
                "Bill Status: " +
                        bill.getBillStatus()
        );

        // ==========================================
        // TEST 1: SUCCESSFUL PAYMENT
        // ==========================================

        System.out.println(
                "\n=== TEST 1: SUCCESSFUL PAYMENT ==="
        );

        Payment payment = null;

        try {

            payment =
                    paymentService.processPayment(
                            bill.getBillId(),
                            bill.getTotalAmount(),
                            PaymentMode.UPI
                    );

            if (payment != null &&
                    payment.getPaymentId() > 0 &&
                    payment.getPaymentStatus()
                            == PaymentStatus.SUCCESS) {

                pass(
                        "Successful payment"
                );

                System.out.println(
                        "Payment ID: " +
                                payment.getPaymentId()
                );

                System.out.println(
                        "Transaction Reference: " +
                                payment.getTransactionReference()
                );

                System.out.println(
                        "Amount: ₹" +
                                payment.getAmount()
                );

                System.out.println(
                        "Payment Mode: " +
                                payment.getPaymentMode()
                );

                System.out.println(
                        "Payment Status: " +
                                payment.getPaymentStatus()
                );

            } else {

                fail(
                        "Successful payment"
                );
            }

        } catch (Exception e) {

            fail(
                    "Successful payment: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: BILL BECOMES PAID
        // ==========================================

        System.out.println(
                "\n=== TEST 2: BILL STATUS AFTER PAYMENT ==="
        );

        try {

            Bill updatedBill =
                    billingService.findById(
                            bill.getBillId()
                    );

            if (updatedBill != null &&
                    updatedBill.getBillStatus()
                            == BillStatus.PAID) {

                pass(
                        "Bill marked as PAID"
                );

                System.out.println(
                        "Bill Status: " +
                                updatedBill.getBillStatus()
                );

            } else {

                fail(
                        "Bill marked as PAID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Bill status verification: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: PAYMENT RETRIEVAL
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND PAYMENT BY ID ==="
        );

        try {

            Payment foundPayment =
                    paymentService.findById(
                            payment.getPaymentId()
                    );

            if (foundPayment != null &&
                    foundPayment.getPaymentId()
                            == payment.getPaymentId()) {

                pass(
                        "Find payment by ID"
                );

                System.out.println(
                        "Payment ID: " +
                                foundPayment.getPaymentId()
                );

            } else {

                fail(
                        "Find payment by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find payment by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: FIND BY TRANSACTION REFERENCE
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND BY TRANSACTION REFERENCE ==="
        );

        try {

            Payment foundPayment =
                    paymentService
                            .findByTransactionReference(
                                    payment.getTransactionReference()
                            );

            if (foundPayment != null &&
                    foundPayment.getPaymentId()
                            == payment.getPaymentId()) {

                pass(
                        "Find payment by transaction reference"
                );

                System.out.println(
                        "Transaction Reference: " +
                                foundPayment
                                        .getTransactionReference()
                );

            } else {

                fail(
                        "Find payment by transaction reference"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find by transaction reference: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: FIND PAYMENTS BY BILL
        // ==========================================

        System.out.println(
                "\n=== TEST 5: FIND PAYMENTS BY BILL ==="
        );

        try {

            List<Payment> payments =
                    paymentService.findByBillId(
                            bill.getBillId()
                    );

            final long successfulPaymentId =
                    payment.getPaymentId();

            boolean found =
                    payments.stream()
                            .anyMatch(existing ->
                                    existing.getPaymentId()
                                            == successfulPaymentId
                            );

            if (found) {

                pass(
                        "Find payments by bill"
                );

                System.out.println(
                        "Payments found: " +
                                payments.size()
                );

            } else {

                fail(
                        "Find payments by bill"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find payments by bill: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 6: SUCCESS AUDIT LOG
        // ==========================================

        System.out.println(
                "\n=== TEST 6: SUCCESS AUDIT LOG ==="
        );

        try {

            AuditLogDAO auditLogDAO =
                    new AuditLogDAOImpl();

            List<AuditLog> auditLogs =
                    auditLogDAO.findByPaymentId(
                            payment.getPaymentId()
                    );

            boolean found =
                    auditLogs.stream()
                            .anyMatch(audit ->
                                    "PAYMENT_SUCCESS"
                                            .equals(
                                                    audit.getAction()
                                            )
                            );

            if (found) {

                pass(
                        "Success audit log"
                );

                AuditLog audit =
                        auditLogs.stream()
                                .filter(a ->
                                        "PAYMENT_SUCCESS"
                                                .equals(
                                                        a.getAction()
                                                )
                                )
                                .findFirst()
                                .get();

                System.out.println(
                        "Audit ID: " +
                                audit.getAuditId()
                );

                System.out.println(
                        "Action: " +
                                audit.getAction()
                );

                System.out.println(
                        "Details: " +
                                audit.getDetails()
                );

            } else {

                fail(
                        "Success audit log"
                );
            }

        } catch (Exception e) {

            fail(
                    "Success audit log: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 7: DUPLICATE PAYMENT
        // ==========================================

        System.out.println(
                "\n=== TEST 7: DUPLICATE PAYMENT ==="
        );

        try {

            paymentService.processPayment(
                    bill.getBillId(),
                    bill.getTotalAmount(),
                    PaymentMode.CARD
            );

            fail(
                    "Duplicate payment rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    (
                            e.getMessage().contains(
                                    "Bill is already paid."
                            )
                                    ||
                                    e.getMessage().contains(
                                            "Payment already exists for this bill."
                                    )
                    )) {

                pass(
                        "Duplicate payment rejection"
                );

                System.out.println(
                        "Reason: " +
                                e.getMessage()
                );

            } else {

                fail(
                        "Duplicate payment rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // CREATE ANOTHER FRESH TEST BILL
        // FOR ROLLBACK TEST
        // ==========================================

        LocalDate rollbackBillingMonth =
                findUnusedBillingMonth(
                        billingService,
                        subscriptionId
                );

        Bill rollbackBill =
                billingService.generateBill(
                        subscriptionId,
                        rollbackBillingMonth,
                        18.0,
                        0.00
                );

        // ==========================================
        // TEST 8: INVALID AMOUNT
        // ==========================================

        System.out.println(
                "\n=== TEST 8: INVALID PAYMENT AMOUNT ==="
        );

        BigDecimal wrongAmount =
                rollbackBill.getTotalAmount()
                        .subtract(
                                new BigDecimal("1.00")
                        );

        try {

            paymentService.processPayment(
                    rollbackBill.getBillId(),
                    wrongAmount,
                    PaymentMode.UPI
            );

            fail(
                    "Invalid amount rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Payment amount does not match bill amount."
                    )) {

                pass(
                        "Invalid amount rejection"
                );

                System.out.println(
                        "Reason: " +
                                e.getMessage()
                );

            } else {

                fail(
                        "Invalid amount rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 9: VERIFY ROLLBACK
        // ==========================================

        System.out.println(
                "\n=== TEST 9: VERIFY ROLLBACK ==="
        );

        try {

            Bill afterFailedPayment =
                    billingService.findById(
                            rollbackBill.getBillId()
                    );

            List<Payment> paymentsAfterFailure =
                    paymentService.findByBillId(
                            rollbackBill.getBillId()
                    );

            if (afterFailedPayment != null &&
                    afterFailedPayment.getBillStatus()
                            == BillStatus.UNPAID &&
                    paymentsAfterFailure.isEmpty()) {

                pass(
                        "Rollback verification"
                );

                System.out.println(
                        "Bill remains: " +
                                afterFailedPayment
                                        .getBillStatus()
                );

                System.out.println(
                        "Payments created: " +
                                paymentsAfterFailure.size()
                );

            } else {

                fail(
                        "Rollback verification"
                );
            }

        } catch (Exception e) {

            fail(
                    "Rollback verification: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 10: FIND PAYMENTS BY CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 10: FIND PAYMENTS BY CUSTOMER ==="
        );

        try {

            List<Payment> customerPayments =
                    paymentService
                            .findByCustomerId(
                                    3
                            );

            final long successfulPaymentId =
                    payment.getPaymentId();

            boolean found =
                    customerPayments.stream()
                            .anyMatch(existing ->
                                    existing.getPaymentId()
                                            == successfulPaymentId
                            );

            if (found) {

                pass(
                        "Find payments by customer"
                );

                System.out.println(
                        "Customer payments found: " +
                                customerPayments.size()
                );

            } else {

                fail(
                        "Find payments by customer"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find payments by customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 11: DECLINED PAYMENT
        // ==========================================

        System.out.println(
                "\n=== TEST 11: DECLINED PAYMENT ==="
        );

        LocalDate declinedBillingMonth =
                findUnusedBillingMonth(
                        billingService,
                        subscriptionId
                );

        Bill declinedBill =
                billingService.generateBill(
                        subscriptionId,
                        declinedBillingMonth,
                        18.0,
                        0.00
                );

        Payment declinedPayment = null;

        try {

            declinedPayment =
                    paymentService.processPayment(
                            declinedBill.getBillId(),
                            declinedBill.getTotalAmount(),
                            PaymentMode.CARD,
                            PaymentStatus.DECLINED
                    );

            if (declinedPayment != null &&
                    declinedPayment.getPaymentId() > 0 &&
                    declinedPayment.getPaymentStatus()
                            == PaymentStatus.DECLINED) {

                pass(
                        "Declined payment"
                );

                System.out.println(
                        "Payment ID: " +
                                declinedPayment.getPaymentId()
                );

                System.out.println(
                        "Transaction Reference: " +
                                declinedPayment
                                        .getTransactionReference()
                );

                System.out.println(
                        "Payment Status: " +
                                declinedPayment.getPaymentStatus()
                );

            } else {

                fail(
                        "Declined payment"
                );
            }

        } catch (Exception e) {

            fail(
                    "Declined payment: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 12: DECLINED PAYMENT - BILL + AUDIT
        // ==========================================

        System.out.println(
                "\n=== TEST 12: DECLINED PAYMENT - BILL + AUDIT ==="
        );

        try {

            Bill afterDeclinedPayment =
                    billingService.findById(
                            declinedBill.getBillId()
                    );

            List<Payment> declinedPayments =
                    paymentService.findByBillId(
                            declinedBill.getBillId()
                    );

            if (afterDeclinedPayment == null) {

                fail(
                        "Declined payment bill verification"
                );

            } else if (afterDeclinedPayment.getBillStatus()
                    != BillStatus.UNPAID) {

                fail(
                        "Declined payment bill remains UNPAID"
                );

            } else if (declinedPayments.isEmpty()) {

                fail(
                        "Declined payment record"
                );

            } else {

                AuditLogDAO auditLogDAO =
                        new AuditLogDAOImpl();

                List<AuditLog> auditLogs =
                        auditLogDAO.findByPaymentId(
                                declinedPayment.getPaymentId()
                        );

                boolean auditFound =
                        auditLogs.stream()
                                .anyMatch(audit ->
                                        "PAYMENT_DECLINED"
                                                .equals(
                                                        audit.getAction()
                                                )
                                );

                if (auditFound) {

                    pass(
                            "Declined payment bill and audit"
                    );

                    System.out.println(
                            "Payment Status: " +
                                    declinedPayment
                                            .getPaymentStatus()
                    );

                    System.out.println(
                            "Bill Status: " +
                                    afterDeclinedPayment
                                            .getBillStatus()
                    );

                    System.out.println(
                            "Audit Action: PAYMENT_DECLINED"
                    );

                } else {

                    fail(
                            "Declined payment audit"
                    );
                }
            }

        } catch (Exception e) {

            fail(
                    "Declined payment bill/audit verification: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 13: DECLINED PAYMENT → SUCCESS RETRY
        // ==========================================

        System.out.println(
                "\n=== TEST 13: DECLINED PAYMENT - SUCCESS RETRY ==="
        );

        Payment retryPayment = null;

        try {

            retryPayment =
                    paymentService.processPayment(
                            declinedBill.getBillId(),
                            declinedBill.getTotalAmount(),
                            PaymentMode.UPI,
                            PaymentStatus.SUCCESS
                    );

            if (retryPayment != null &&
                    retryPayment.getPaymentId() > 0 &&
                    retryPayment.getPaymentStatus()
                            == PaymentStatus.SUCCESS) {

                pass(
                        "Declined payment success retry"
                );

                System.out.println(
                        "Retry Payment ID: " +
                                retryPayment.getPaymentId()
                );

                System.out.println(
                        "Retry Status: " +
                                retryPayment.getPaymentStatus()
                );

            } else {

                fail(
                        "Declined payment success retry"
                );
            }

        } catch (Exception e) {

            fail(
                    "Declined payment success retry: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 14: VERIFY RETRY RESULT
        // ==========================================

        System.out.println(
                "\n=== TEST 14: VERIFY SUCCESS RETRY RESULT ==="
        );

        if (declinedPayment == null ||
                retryPayment == null) {

            fail("Success retry result");
        } else {
            try {

                Bill updatedRetryBill =
                        billingService.findById(
                                declinedBill.getBillId()
                        );

                List<Payment> retryPayments =
                        paymentService.findByBillId(
                                declinedBill.getBillId()
                        );

                AuditLogDAO auditLogDAO =
                        new AuditLogDAOImpl();

                List<AuditLog> retryAuditLogs =
                        auditLogDAO.findByPaymentId(
                                retryPayment.getPaymentId()
                        );

                boolean successAuditFound =
                        retryAuditLogs.stream()
                                .anyMatch(audit ->
                                        "PAYMENT_SUCCESS"
                                                .equals(
                                                        audit.getAction()
                                                )
                                );
                final long declinedPaymentId =
                        declinedPayment.getPaymentId();

                final long retryPaymentId =
                        retryPayment.getPaymentId();

                boolean declinedStillExists =
                        retryPayments.stream()
                                .anyMatch(existing ->
                                        existing.getPaymentId()
                                                == declinedPaymentId
                                );

                boolean successRetryExists =
                        retryPayments.stream()
                                .anyMatch(existing ->
                                        existing.getPaymentId()
                                                == retryPaymentId
                                                &&
                                                existing.getPaymentStatus()
                                                        == PaymentStatus.SUCCESS
                                );

                if (updatedRetryBill != null &&
                        updatedRetryBill.getBillStatus()
                                == BillStatus.PAID &&
                        declinedStillExists &&
                        successRetryExists &&
                        successAuditFound) {

                    pass(
                            "Success retry result"
                    );

                    System.out.println(
                            "Bill Status: " +
                                    updatedRetryBill
                                            .getBillStatus()
                    );

                    System.out.println(
                            "Payments for bill: " +
                                    retryPayments.size()
                    );

                    System.out.println(
                            "Declined payment retained: YES"
                    );

                    System.out.println(
                            "Successful retry retained: YES"
                    );

                    System.out.println(
                            "PAYMENT_SUCCESS audit: YES"
                    );

                } else {

                    fail(
                            "Success retry result"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Success retry result: " +
                                e.getMessage()
                );
            }
        }


        // ==========================================
        // FINAL RESULT
        // ==========================================

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "TOTAL PASSED: " +
                        passed
        );

        System.out.println(
                "TOTAL FAILED: " +
                        failed
        );

        System.out.println(
                "=========================================="
        );

        if (failed == 0) {

            System.out.println(
                    "PAYMENT TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "PAYMENT TEST SUITE: FAILED"
            );
        }
    }

    private static LocalDate findUnusedBillingMonth(
            BillingService billingService,
            long subscriptionId) {

        LocalDate billingMonth =
                LocalDate.of(
                        2026,
                        9,
                        1
                );

        while (
                billingService
                        .findBySubscriptionAndMonth(
                                subscriptionId,
                                billingMonth
                        ) != null
        ) {

            billingMonth =
                    billingMonth.plusMonths(1);
        }

        return billingMonth;
    }

    private static void pass(
            String testName) {

        passed++;

        System.out.println(
                testName +
                        ": PASSED"
        );
    }

    private static void fail(
            String testName) {

        failed++;

        System.out.println(
                testName +
                        ": FAILED"
        );
    }
}