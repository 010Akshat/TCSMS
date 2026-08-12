package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Bill;

import java.time.LocalDate;
import java.util.List;

public interface BillingService {

    Bill generateBill(
            long subscriptionId,
            LocalDate billingMonth,
            double taxRate,
            double discount
    );

    Bill findById(
            long billId
    );

    Bill findByBillNumber(
            String billNumber
    );

    Bill findBySubscriptionAndMonth(
            long subscriptionId,
            LocalDate billingMonth
    );

    List<Bill> findBySubscriptionId(
            long subscriptionId
    );

    List<Bill> findAll();

    void update(
            Bill bill
    );

    void delete(
            long billId
    );
}