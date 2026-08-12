package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.BillStatus;
import com.amdocs.telecom.service.BillingService;
import com.amdocs.telecom.service.impl.BillingServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillingTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        BillingService billingService =
                new BillingServiceImpl();

        System.out.println(
                "=== BILLING TEST SUITE ==="
        );

        long subscriptionId = 1;

        LocalDate billingMonth =
                LocalDate.of(
                        2026,
                        8,
                        1
                );

        BigDecimal expectedPlanRental =
                new BigDecimal("699.00");

        BigDecimal expectedUsageCharges =
                new BigDecimal("525.00");

        BigDecimal expectedTax =
                new BigDecimal("220.32");

        BigDecimal expectedDiscount =
                new BigDecimal("50.00");

        BigDecimal expectedTotal =
                new BigDecimal("1394.32");

        LocalDate expectedDueDate =
                LocalDate.of(
                        2026,
                        9,
                        20
                );

        Bill bill = null;


        // ==========================================
        // TEST 1: GENERATE BILL
        // ==========================================

        System.out.println(
                "\n=== TEST 1: GENERATE BILL ==="
        );

        try {

            /*
             * Make the test reasonably rerunnable.
             * If the August bill already exists,
             * retrieve it instead of creating a
             * duplicate bill.
             */
            bill =
                    billingService
                            .findBySubscriptionAndMonth(
                                    subscriptionId,
                                    billingMonth
                            );

            if (bill == null) {

                bill =
                        billingService.generateBill(
                                subscriptionId,
                                billingMonth,
                                18.0,
                                50.00
                        );

                System.out.println(
                        "New bill generated."
                );

            } else {

                System.out.println(
                        "August bill already exists. " +
                                "Using existing bill for verification."
                );
            }

            if (bill != null &&
                    bill.getBillId() > 0) {

                pass(
                        "Bill generation/retrieval"
                );

                printBill(
                        bill
                );

            } else {

                fail(
                        "Bill generation/retrieval"
                );
            }

        } catch (Exception e) {

            fail(
                    "Bill generation: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 2: VERIFY PLAN RENTAL
        // ==========================================

        System.out.println(
                "\n=== TEST 2: VERIFY PLAN RENTAL ==="
        );

        try {

            if (bill != null &&
                    bill.getPlanRental()
                            .compareTo(
                                    expectedPlanRental
                            ) == 0) {

                pass(
                        "Plan rental calculation"
                );

                System.out.println(
                        "Plan rental: ₹" +
                                bill.getPlanRental()
                );

            } else {

                fail(
                        "Plan rental calculation"
                );

                printExpectedActual(
                        "Plan rental",
                        expectedPlanRental,
                        bill != null
                                ? bill.getPlanRental()
                                : null
                );
            }

        } catch (Exception e) {

            fail(
                    "Plan rental calculation: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 3: VERIFY USAGE CHARGES
        // ==========================================

        System.out.println(
                "\n=== TEST 3: VERIFY USAGE CHARGES ==="
        );

        try {

            if (bill != null &&
                    bill.getUsageCharges()
                            .compareTo(
                                    expectedUsageCharges
                            ) == 0) {

                pass(
                        "Usage charges calculation"
                );

                System.out.println(
                        "Usage charges: ₹" +
                                bill.getUsageCharges()
                );

            } else {

                fail(
                        "Usage charges calculation"
                );

                printExpectedActual(
                        "Usage charges",
                        expectedUsageCharges,
                        bill != null
                                ? bill.getUsageCharges()
                                : null
                );
            }

        } catch (Exception e) {

            fail(
                    "Usage charges calculation: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 4: VERIFY TAX
        // ==========================================

        System.out.println(
                "\n=== TEST 4: VERIFY TAX ==="
        );

        try {

            if (bill != null &&
                    bill.getTaxAmount()
                            .compareTo(
                                    expectedTax
                            ) == 0) {

                pass(
                        "Tax calculation"
                );

                System.out.println(
                        "Tax: ₹" +
                                bill.getTaxAmount()
                );

            } else {

                fail(
                        "Tax calculation"
                );

                printExpectedActual(
                        "Tax",
                        expectedTax,
                        bill != null
                                ? bill.getTaxAmount()
                                : null
                );
            }

        } catch (Exception e) {

            fail(
                    "Tax calculation: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 5: VERIFY DISCOUNT
        // ==========================================

        System.out.println(
                "\n=== TEST 5: VERIFY DISCOUNT ==="
        );

        try {

            if (bill != null &&
                    bill.getDiscount()
                            .compareTo(
                                    expectedDiscount
                            ) == 0) {

                pass(
                        "Discount"
                );

                System.out.println(
                        "Discount: ₹" +
                                bill.getDiscount()
                );

            } else {

                fail(
                        "Discount"
                );

                printExpectedActual(
                        "Discount",
                        expectedDiscount,
                        bill != null
                                ? bill.getDiscount()
                                : null
                );
            }

        } catch (Exception e) {

            fail(
                    "Discount: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 6: VERIFY TOTAL AMOUNT
        // ==========================================

        System.out.println(
                "\n=== TEST 6: VERIFY TOTAL AMOUNT ==="
        );

        try {

            if (bill != null &&
                    bill.getTotalAmount()
                            .compareTo(
                                    expectedTotal
                            ) == 0) {

                pass(
                        "Total amount calculation"
                );

                System.out.println(
                        "Total amount: ₹" +
                                bill.getTotalAmount()
                );

            } else {

                fail(
                        "Total amount calculation"
                );

                printExpectedActual(
                        "Total amount",
                        expectedTotal,
                        bill != null
                                ? bill.getTotalAmount()
                                : null
                );
            }

        } catch (Exception e) {

            fail(
                    "Total amount calculation: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 7: VERIFY DUE DATE
        // ==========================================

        System.out.println(
                "\n=== TEST 7: VERIFY DUE DATE ==="
        );

        try {

            if (bill != null &&
                    expectedDueDate.equals(
                            bill.getDueDate()
                    )) {

                pass(
                        "Due date calculation"
                );

                System.out.println(
                        "Due date: " +
                                bill.getDueDate()
                );

            } else {

                fail(
                        "Due date calculation"
                );

                System.out.println(
                        "Expected due date: " +
                                expectedDueDate
                );

                System.out.println(
                        "Actual due date: " +
                                (
                                        bill != null
                                                ? bill.getDueDate()
                                                : null
                                )
                );
            }

        } catch (Exception e) {

            fail(
                    "Due date calculation: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 8: VERIFY BILL STATUS
        // ==========================================

        System.out.println(
                "\n=== TEST 8: VERIFY BILL STATUS ==="
        );

        try {

            if (bill != null &&
                    bill.getBillStatus()
                            == BillStatus.UNPAID) {

                pass(
                        "Initial bill status"
                );

                System.out.println(
                        "Bill status: " +
                                bill.getBillStatus()
                );

            } else {

                fail(
                        "Initial bill status"
                );

                System.out.println(
                        "Actual status: " +
                                (
                                        bill != null
                                                ? bill.getBillStatus()
                                                : null
                                )
                );
            }

        } catch (Exception e) {

            fail(
                    "Initial bill status: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 9: FIND BILL BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 9: FIND BILL BY ID ==="
        );

        try {

            Bill foundBill =
                    billingService.findById(
                            bill.getBillId()
                    );

            if (foundBill != null &&
                    foundBill.getBillId()
                            == bill.getBillId()) {

                pass(
                        "Find bill by ID"
                );

                System.out.println(
                        "Bill ID: " +
                                foundBill.getBillId()
                );

            } else {

                fail(
                        "Find bill by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find bill by ID: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 10: FIND BILL BY NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 10: FIND BILL BY NUMBER ==="
        );

        try {

            Bill foundBill =
                    billingService.findByBillNumber(
                            bill.getBillNumber()
                    );

            if (foundBill != null &&
                    foundBill.getBillNumber()
                            .equals(
                                    bill.getBillNumber()
                            )) {

                pass(
                        "Find bill by number"
                );

                System.out.println(
                        "Bill number: " +
                                foundBill.getBillNumber()
                );

            } else {

                fail(
                        "Find bill by number"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find bill by number: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 11: FIND BILL BY
        // SUBSCRIPTION + MONTH
        // ==========================================

        System.out.println(
                "\n=== TEST 11: FIND BILL BY SUBSCRIPTION + MONTH ==="
        );

        try {

            Bill foundBill =
                    billingService
                            .findBySubscriptionAndMonth(
                                    subscriptionId,
                                    billingMonth
                            );

            if (foundBill != null &&
                    foundBill.getBillId()
                            == bill.getBillId()) {

                pass(
                        "Find bill by subscription and month"
                );

                System.out.println(
                        "Billing month: " +
                                foundBill.getBillingMonth()
                );

            } else {

                fail(
                        "Find bill by subscription and month"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find bill by subscription and month: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 12: FIND BY SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 12: FIND BILLS BY SUBSCRIPTION ==="
        );

        try {

            List<Bill> bills =
                    billingService
                            .findBySubscriptionId(
                                    subscriptionId
                            );

            if (!bills.isEmpty()) {

                pass(
                        "Find bills by subscription"
                );

                System.out.println(
                        "Bills found: " +
                                bills.size()
                );

            } else {

                fail(
                        "Find bills by subscription"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find bills by subscription: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 13: FIND ALL BILLS
        // ==========================================

        System.out.println(
                "\n=== TEST 13: FIND ALL BILLS ==="
        );

        try {

            List<Bill> bills =
                    billingService.findAll();

            final long billId =
                    bill.getBillId();

            boolean found =
                    bills.stream()
                            .anyMatch(existing ->
                                    existing.getBillId()
                                            == billId
                            );

            if (found) {

                pass(
                        "Find all bills"
                );

                System.out.println(
                        "Total bills: " +
                                bills.size()
                );

            } else {

                fail(
                        "Find all bills"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find all bills: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // TEST 14: DUPLICATE MONTHLY BILL
        // ==========================================

        System.out.println(
                "\n=== TEST 14: DUPLICATE MONTHLY BILL ==="
        );

        try {

            billingService.generateBill(
                    subscriptionId,
                    billingMonth,
                    18.0,
                    50.00
            );

            fail(
                    "Duplicate monthly bill rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Bill already exists"
                    )) {

                pass(
                        "Duplicate monthly bill rejection"
                );

                System.out.println(
                        "Reason: " +
                                e.getMessage()
                );

            } else {

                fail(
                        "Duplicate monthly bill rejection: " +
                                e.getMessage()
                );
            }
        }


        // ==========================================
        // TEST 15: UPDATE BILL STATUS
        // ==========================================

        System.out.println(
                "\n=== TEST 15: UPDATE BILL STATUS ==="
        );

        try {

            bill.setBillStatus(
                    BillStatus.OVERDUE
            );

            billingService.update(
                    bill
            );

            Bill updatedBill =
                    billingService.findById(
                            bill.getBillId()
                    );

            if (updatedBill != null &&
                    updatedBill.getBillStatus()
                            == BillStatus.OVERDUE) {

                pass(
                        "Bill update"
                );

                System.out.println(
                        "Updated status: " +
                                updatedBill.getBillStatus()
                );

            } else {

                fail(
                        "Bill update"
                );
            }

        } catch (Exception e) {

            fail(
                    "Bill update: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // RESTORE BILL STATUS
        // ==========================================

        try {

            bill.setBillStatus(
                    BillStatus.UNPAID
            );

            billingService.update(
                    bill
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not restore bill status: " +
                            e.getMessage()
            );
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
                    "BILLING TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "BILLING TEST SUITE: FAILED"
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

    private static void printExpectedActual(
            String field,
            BigDecimal expected,
            BigDecimal actual) {

        System.out.println(
                "Expected " +
                        field +
                        ": " +
                        expected
        );

        System.out.println(
                "Actual " +
                        field +
                        ": " +
                        actual
        );
    }

    private static void printBill(
            Bill bill) {

        System.out.println(
                "Bill ID: " +
                        bill.getBillId()
        );

        System.out.println(
                "Bill Number: " +
                        bill.getBillNumber()
        );

        System.out.println(
                "Subscription ID: " +
                        bill.getSubscriptionId()
        );

        System.out.println(
                "Billing Month: " +
                        bill.getBillingMonth()
        );

        System.out.println(
                "Plan Rental: ₹" +
                        bill.getPlanRental()
        );

        System.out.println(
                "Usage Charges: ₹" +
                        bill.getUsageCharges()
        );

        System.out.println(
                "Tax: ₹" +
                        bill.getTaxAmount()
        );

        System.out.println(
                "Discount: ₹" +
                        bill.getDiscount()
        );

        System.out.println(
                "Total: ₹" +
                        bill.getTotalAmount()
        );

        System.out.println(
                "Due Date: " +
                        bill.getDueDate()
        );

        System.out.println(
                "Status: " +
                        bill.getBillStatus()
        );

        System.out.println(
                "Created At: " +
                        bill.getCreatedAt()
        );

        System.out.println(
                "Updated At: " +
                        bill.getUpdatedAt()
        );
    }
}