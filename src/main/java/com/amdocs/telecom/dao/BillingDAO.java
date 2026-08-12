package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.Bill;

import java.time.LocalDate;
import java.util.List;
import java.sql.Connection;

public interface BillingDAO {

    void save(Bill bill);

    Bill findById(long billId);

    Bill findById(
            long billId,
            Connection connection
    );

    Bill findByBillNumber(String billNumber);

    Bill findBySubscriptionAndMonth(
            long subscriptionId,
            LocalDate billingMonth
    );

    List<Bill> findBySubscriptionId(
            long subscriptionId
    );

    List<Bill> findAll();

    void update(Bill bill);

    void update(
            Bill bill,
            Connection connection
    );

    void delete(long billId);
}