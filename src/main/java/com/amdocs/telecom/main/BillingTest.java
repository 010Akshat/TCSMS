package com.amdocs.telecom.main;

import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.dao.UsageDAO;
import com.amdocs.telecom.dao.impl.PlanDAOImpl;
import com.amdocs.telecom.dao.impl.UsageDAOImpl;
import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.enums.BillStatus;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.service.BillingService;
import com.amdocs.telecom.service.impl.BillingServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class BillingTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        BillingService billingService =
                new BillingServiceImpl();

        PlanDAO planDAO =
                new PlanDAOImpl();

        UsageDAO usageDAO =
                new UsageDAOImpl();

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

        double taxRate = 18.0;

        BigDecimal discount =
                new BigDecimal("50.00");

        Bill bill = null;

        // ==========================================
        // GET CURRENT PLAN
        // ==========================================

        TelecomPlan plan =
                planDAO.findById(2);

        if (plan == null) {

            System.out.println(
                    "Required plan not found."
            );

            return;
        }

        // ==========================================
        // CALCULATE EXPECTED USAGE CHARGES
        // ==========================================

        BigDecimal expectedUsageCharges =
                usageDAO.findBySubscriptionId(
                                subscriptionId
                        )
                        .stream()
                        .filter(record ->
                                record.getUsageDate() != null &&
                                        record.getUsageDate()
                                                .getYear()
                                                ==
                                                billingMonth.getYear() &&
                                        record.getUsageDate()
                                                .getMonthValue()
                                                ==
                                                billingMonth
                                                        .getMonthValue()
                        )
                        .map(UsageRecord::getCharge)
                        .filter(charge ->
                                charge != null
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal expectedPlanRental =
                plan.getMonthlyRental() != null
                        ? plan.getMonthlyRental()
                        : BigDecimal.ZERO;

        BigDecimal taxableAmount =
                expectedPlanRental
                        .add(expectedUsageCharges);

        BigDecimal taxRateDecimal =
                BigDecimal.valueOf(taxRate)
                        .divide(
                                BigDecimal.valueOf(100),
                                10,
                                RoundingMode.HALF_UP
                        );

        BigDecimal expectedTax =
                taxableAmount
                        .multiply(taxRateDecimal)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal expectedTotal =
                taxableAmount
                        .add(expectedTax)
                        .subtract(discount)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        LocalDate expectedDueDate =
                billingMonth
                        .plusMonths(1)
                        .withDayOfMonth(20);

        // ==========================================
        // TEST 1: GENERATE / RETRIEVE BILL
        // ==========================================

        System.out.println(
                "\n=== TEST 1: GENERATE BILL ==="
        );

        try {

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
                                taxRate,
                                discount.doubleValue()
                        );

                System.out.println(
                        "New bill generated."
                );

            } else {

                System.out.println(
                        "Bill already exists. " +
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

                System.out.println(
                        "Expected: ₹" +
                                expectedUsageCharges
                );

                System.out.println(
                        "Actual: ₹" +
                                (
                                        bill != null
                                                ? bill.getUsageCharges()
                                                : null
                                )
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

                System.out.println(
                        "Expected: ₹" +
                                expectedTax
                );

                System.out.println(
                        "Actual: ₹" +
                                (
                                        bill != null
                                                ? bill.getTaxAmount()
                                                : null
                                )
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
                                    discount
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

                System.out.println(
                        "Expected: ₹" +
                                expectedTotal
                );

                System.out.println(
                        "Actual: ₹" +
                                (
                                        bill != null
                                                ? bill.getTotalAmount()
                                                : null
                                )
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
                        "Expected: " +
                                expectedDueDate
                );

                System.out.println(
                        "Actual: " +
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
        // TEST 11: FIND BY SUBSCRIPTION + MONTH
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
                    billingService.findBySubscriptionId(
                            subscriptionId
                    );

            final long currentBillId =
                    bill.getBillId();

            boolean found =
                    bills.stream()
                            .anyMatch(existing ->
                                    existing.getBillId()
                                            == currentBillId
                            );

            if (found) {

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
                    taxRate,
                    discount.doubleValue()
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
        // RESTORE STATUS
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