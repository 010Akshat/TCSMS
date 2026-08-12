package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.BillingDAO;
import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.dao.UsageDAO;
import com.amdocs.telecom.dao.impl.BillingDAOImpl;
import com.amdocs.telecom.dao.impl.PlanDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionDAOImpl;
import com.amdocs.telecom.dao.impl.UsageDAOImpl;
import com.amdocs.telecom.model.AccountStatus;
import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.BillStatus;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.service.BillingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

public class BillingServiceImpl
        implements BillingService {

    private final BillingDAO billingDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final PlanDAO planDAO;
    private final UsageDAO usageDAO;

    public BillingServiceImpl() {

        this.billingDAO =
                new BillingDAOImpl();

        this.subscriptionDAO =
                new SubscriptionDAOImpl();

        this.planDAO =
                new PlanDAOImpl();

        this.usageDAO =
                new UsageDAOImpl();
    }

    @Override
    public Bill generateBill(
            long subscriptionId,
            LocalDate billingMonth,
            double taxRate,
            double discount) {

        // 1. Validate billing month
        if (billingMonth == null) {

            throw new IllegalArgumentException(
                    "Billing month is mandatory."
            );
        }

        // Normalize billing month to first day
        LocalDate normalizedBillingMonth =
                billingMonth.withDayOfMonth(1);

        // 2. Validate tax rate
        if (taxRate < 0) {

            throw new IllegalArgumentException(
                    "Tax rate cannot be negative."
            );
        }

        // 3. Validate discount
        if (discount < 0) {

            throw new IllegalArgumentException(
                    "Discount cannot be negative."
            );
        }

        // 4. Subscription must exist
        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        // 5. Plan must exist
        TelecomPlan plan =
                planDAO.findById(
                        subscription.getPlanId()
                );

        if (plan == null) {

            throw new IllegalArgumentException(
                    "Plan not found for subscription."
            );
        }

        // 6. Prevent duplicate monthly bill
        Bill existingBill =
                billingDAO.findBySubscriptionAndMonth(
                        subscriptionId,
                        normalizedBillingMonth
                );

        if (existingBill != null) {

            throw new IllegalArgumentException(
                    "Bill already exists for this subscription and billing month."
            );
        }

        // 7. Plan rental
        BigDecimal planRental =
                plan.getMonthlyRental();

        if (planRental == null) {
            planRental = BigDecimal.ZERO;
        }

        // 8. Calculate usage charges
        BigDecimal usageCharges =
                usageDAO.findBySubscriptionId(
                                subscriptionId
                        )
                        .stream()
                        .filter(record ->
                                record.getUsageDate() != null &&
                                        record.getUsageDate()
                                                .getYear()
                                                ==
                                                normalizedBillingMonth.getYear() &&
                                        record.getUsageDate()
                                                .getMonthValue()
                                                ==
                                                normalizedBillingMonth
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

        // 9. Taxable amount
        BigDecimal taxableAmount =
                planRental.add(
                        usageCharges
                );

        // 10. Calculate tax
        BigDecimal taxRateDecimal =
                BigDecimal.valueOf(
                                taxRate
                        )
                        .divide(
                                BigDecimal.valueOf(100),
                                10,
                                RoundingMode.HALF_UP
                        );

        BigDecimal taxAmount =
                taxableAmount
                        .multiply(taxRateDecimal)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        // 11. Discount
        BigDecimal discountAmount =
                BigDecimal.valueOf(
                                discount
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        // Prevent discount greater than bill base
        if (discountAmount.compareTo(
                taxableAmount
        ) > 0) {

            discountAmount =
                    taxableAmount.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        // 12. Total amount
        BigDecimal totalAmount =
                taxableAmount
                        .add(taxAmount)
                        .subtract(discountAmount)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        // 13. Due date
        LocalDate dueDate =
                normalizedBillingMonth
                        .plusMonths(1)
                        .withDayOfMonth(20);

        // 14. Generate bill number
        String billNumber =
                generateBillNumber(
                        normalizedBillingMonth
                );

        // 15. Create bill
        Bill bill =
                new Bill(
                        0,
                        billNumber,
                        subscriptionId,
                        normalizedBillingMonth,
                        planRental,
                        usageCharges,
                        taxAmount,
                        discountAmount,
                        totalAmount,
                        dueDate,
                        BillStatus.UNPAID,
                        null,
                        null
                );

        // 16. Save bill
        billingDAO.save(bill);

        return bill;
    }

    @Override
    public Bill findById(
            long billId) {

        return billingDAO.findById(
                billId
        );
    }

    @Override
    public Bill findByBillNumber(
            String billNumber) {

        return billingDAO.findByBillNumber(
                billNumber
        );
    }

    @Override
    public Bill findBySubscriptionAndMonth(
            long subscriptionId,
            LocalDate billingMonth) {

        if (billingMonth == null) {

            throw new IllegalArgumentException(
                    "Billing month is mandatory."
            );
        }

        return billingDAO
                .findBySubscriptionAndMonth(
                        subscriptionId,
                        billingMonth.withDayOfMonth(1)
                );
    }

    @Override
    public List<Bill> findBySubscriptionId(
            long subscriptionId) {

        return billingDAO.findBySubscriptionId(
                subscriptionId
        );
    }

    @Override
    public List<Bill> findAll() {

        return billingDAO.findAll();
    }

    @Override
    public void update(
            Bill bill) {

        billingDAO.update(
                bill
        );
    }

    @Override
    public void delete(
            long billId) {

        billingDAO.delete(
                billId
        );
    }

    private String generateBillNumber(
            LocalDate billingMonth) {

        String prefix =
                "INV-" +
                        billingMonth.getYear() +
                        "-" +
                        String.format(
                                "%02d",
                                billingMonth.getMonthValue()
                        ) +
                        "-";

        List<Bill> bills =
                billingDAO.findAll();

        long nextNumber =
                10001L + bills.size();

        String billNumber;

        do {

            billNumber =
                    prefix +
                            nextNumber;

            nextNumber++;

        } while (
                billingDAO.findByBillNumber(
                        billNumber
                ) != null
        );

        return billNumber;
    }
}