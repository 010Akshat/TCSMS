package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Bill;

import java.time.LocalDate;
import java.util.List;

public interface AdminBillingService {


    Bill generateBill(
            Admin admin,
            long subscriptionId,
            LocalDate billingMonth,
            double taxRate,
            double discount
    );


    Bill findBillById(
            Admin admin,
            long billId
    );


    Bill findBillByNumber(
            Admin admin,
            String billNumber
    );


    List<Bill> findAllBills(
            Admin admin
    );


    List<Bill> findBillsBySubscription(
            Admin admin,
            long subscriptionId
    );

}