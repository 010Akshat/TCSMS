package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.Payment;
import com.amdocs.telecom.service.SubscriptionService;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;
import com.amdocs.telecom.model.MobileSubscription;

import com.amdocs.telecom.model.enums.PaymentMode;

import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminBillingService;
import com.amdocs.telecom.service.AdminPaymentService;
import com.amdocs.telecom.service.PaymentService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminBillingServiceImpl;
import com.amdocs.telecom.service.impl.AdminPaymentServiceImpl;
import com.amdocs.telecom.service.impl.PaymentServiceImpl;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class AdminBillingPaymentTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminBillingService adminBillingService =
                new AdminBillingServiceImpl();


        AdminPaymentService adminPaymentService =
                new AdminPaymentServiceImpl();


        PaymentService paymentService =
                new PaymentServiceImpl();

        SubscriptionService subscriptionService =
                new SubscriptionServiceImpl();


        System.out.println(
                "=== ADMIN BILLING PAYMENT TEST SUITE ==="
        );


        Admin admin = null;


        // ==========================================
        // TEST 1: ADMIN LOGIN
        // ==========================================


        System.out.println(
                "\n=== TEST 1: ADMIN LOGIN ==="
        );


        try {


            admin =
                    authenticationService.login(
                            "admin",
                            "admin123"
                    );


            if (admin != null &&
                    admin.getAdminId() > 0) {


                pass(
                        "Admin login"
                );


                System.out.println(
                        "Welcome " +
                                admin.getFirstName()
                );


            } else {


                fail(
                        "Admin login"
                );
            }


        } catch (Exception e) {


            fail(
                    "Admin login: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 2: GENERATE BILL AS ADMIN
        // ==========================================


        long subscriptionId = 0;

        List<MobileSubscription> subscriptions =
                subscriptionService.findAll();


        for(MobileSubscription sub : subscriptions) {

            Bill existing =
                    adminBillingService
                            .findBillsBySubscription(
                                    admin,
                                    sub.getSubscriptionId()
                            )
                            .stream()
                            .filter(item ->
                                    item.getBillingMonth()
                                            .equals(
                                                    LocalDate.now()
                                                            .withDayOfMonth(1)
                                            )
                            )
                            .findFirst()
                            .orElse(null);


            if(existing == null) {

                subscriptionId =
                        sub.getSubscriptionId();

                break;
            }
        }


        if(subscriptionId == 0) {

            System.out.println(
                    "No subscription available for bill generation test."
            );

            return;
        }

        Bill bill = null;


        System.out.println(
                "\n=== TEST 2: GENERATE BILL ==="
        );


        try {


            bill =
                    adminBillingService.generateBill(
                            admin,
                            subscriptionId,
                            LocalDate.now()
                                    .withDayOfMonth(1),
                            18,
                            0
                    );


            if (bill != null &&
                    bill.getBillId() > 0) {


                pass(
                        "Generate bill"
                );


                System.out.println(
                        "Bill ID: " +
                                bill.getBillId()
                );


            } else {


                fail(
                        "Generate bill"
                );
            }


        } catch (Exception e) {


            fail(
                    "Generate bill: " +
                            e.getMessage()
            );
        }



        // ==========================================
// TEST 3: VIEW ALL BILLS
// ==========================================

        System.out.println(
                "\n=== TEST 3: VIEW ALL BILLS ==="
        );


        try {


            List<Bill> bills =
                    adminBillingService.findAllBills(
                            admin
                    );


            if(bills != null &&
                    !bills.isEmpty()) {


                pass(
                        "View all bills"
                );


                System.out.println(
                        "Total bills: " +
                                bills.size()
                );


            } else {


                fail(
                        "View all bills"
                );
            }


        } catch(Exception e) {


            e.printStackTrace();

            fail(
                    "View all bills: " +
                            e.getClass().getName() +
                            " - " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 4: FIND BILL BY ID
// ==========================================

        System.out.println(
                "\n=== TEST 4: FIND BILL BY ID ==="
        );


        try {


            Bill found =
                    adminBillingService.findBillById(
                            admin,
                            bill.getBillId()
                    );


            if(found != null &&
                    found.getBillId()
                            == bill.getBillId()) {


                pass(
                        "Find bill by ID"
                );


            } else {


                fail(
                        "Find bill by ID"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find bill by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 5: FIND BILL BY NUMBER
// ==========================================

        System.out.println(
                "\n=== TEST 5: FIND BILL BY NUMBER ==="
        );


        try {


            Bill found =
                    adminBillingService.findBillByNumber(
                            admin,
                            bill.getBillNumber()
                    );


            if(found != null &&
                    found.getBillId()
                            == bill.getBillId()) {


                pass(
                        "Find bill by number"
                );


            } else {


                fail(
                        "Find bill by number"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find bill by number: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 6: CREATE PAYMENT TEST DATA
// ==========================================

        System.out.println(
                "\n=== TEST 6: CREATE PAYMENT TEST DATA ==="
        );


        Payment payment = null;


        try {


            payment =
                    paymentService.processPayment(
                            bill.getBillId(),
                            bill.getTotalAmount(),
                            PaymentMode.UPI
                    );


            if(payment != null &&
                    payment.getPaymentId() > 0) {


                pass(
                        "Create payment test data"
                );


                System.out.println(
                        "Payment ID: " +
                                payment.getPaymentId()
                );


            } else {


                fail(
                        "Create payment test data"
                );
            }


        } catch(Exception e) {


            fail(
                    "Create payment test data: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 7: VIEW ALL PAYMENTS
// ==========================================

        System.out.println(
                "\n=== TEST 7: VIEW ALL PAYMENTS ==="
        );


        try {


            List<Payment> payments =
                    adminPaymentService.findAllPayments(
                            admin
                    );


            if(payments != null &&
                    !payments.isEmpty()) {


                pass(
                        "View all payments"
                );


                System.out.println(
                        "Total payments: " +
                                payments.size()
                );


            } else {


                fail(
                        "View all payments"
                );
            }


        } catch(Exception e) {


            fail(
                    "View all payments: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 8: VIEW PAYMENTS BY BILL
// ==========================================

        System.out.println(
                "\n=== TEST 8: VIEW PAYMENTS BY BILL ==="
        );


        try {


            List<Payment> payments =
                    adminPaymentService.findPaymentsByBill(
                            admin,
                            bill.getBillId()
                    );


            final long paymentId =
                    payment.getPaymentId();


            boolean found =
                    payments.stream()
                            .anyMatch(item ->
                                    item.getPaymentId()
                                            == paymentId
                            );


            if(found) {


                pass(
                        "View payments by bill"
                );


            } else {


                fail(
                        "View payments by bill"
                );
            }


        } catch(Exception e) {


            fail(
                    "View payments by bill: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 9: VIEW PAYMENTS BY CUSTOMER
// ==========================================

        System.out.println(
                "\n=== TEST 9: VIEW PAYMENTS BY CUSTOMER ==="
        );


        try {


            List<Payment> payments =
                    adminPaymentService.findPaymentsByCustomer(
                            admin,
                            payment.getCustomerId()
                    );


            final long paymentId =
                    payment.getPaymentId();


            boolean found =
                    payments.stream()
                            .anyMatch(item ->
                                    item.getPaymentId()
                                            == paymentId
                            );


            if(found) {


                pass(
                        "View payments by customer"
                );


            } else {


                fail(
                        "View payments by customer"
                );
            }


        } catch(Exception e) {


            fail(
                    "View payments by customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 10: NULL ADMIN REJECTION
// ==========================================

        System.out.println(
                "\n=== TEST 10: NULL ADMIN REJECTION ==="
        );


        try {


            adminPaymentService.findAllPayments(
                    null
            );


            fail(
                    "Null admin rejection"
            );


        } catch(SecurityException e) {


            pass(
                    "Null admin rejection"
            );


        } catch(Exception e) {


            fail(
                    "Null admin rejection: " +
                            e.getMessage()
            );
        }

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