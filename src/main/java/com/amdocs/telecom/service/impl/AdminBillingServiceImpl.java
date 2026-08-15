package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Bill;

import com.amdocs.telecom.security.AdminAuthorizationUtil;

import com.amdocs.telecom.service.AdminBillingService;
import com.amdocs.telecom.service.BillingService;

import java.time.LocalDate;
import java.util.List;


public class AdminBillingServiceImpl
        implements AdminBillingService {


    private final BillingService billingService;


    public AdminBillingServiceImpl() {

        this.billingService =
                new BillingServiceImpl();

    }



    @Override
    public Bill generateBill(
            Admin admin,
            long subscriptionId,
            LocalDate billingMonth,
            double taxRate,
            double discount) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(subscriptionId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid subscription id."
            );
        }


        return billingService.generateBill(
                subscriptionId,
                billingMonth,
                taxRate,
                discount
        );
    }





    @Override
    public Bill findBillById(
            Admin admin,
            long billId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(billId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid bill id."
            );
        }


        Bill bill =
                billingService.findById(
                        billId
                );


        if(bill == null) {

            throw new IllegalArgumentException(
                    "Bill not found."
            );
        }


        return bill;
    }





    @Override
    public Bill findBillByNumber(
            Admin admin,
            String billNumber) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(billNumber == null ||
                billNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Bill number is mandatory."
            );
        }


        return billingService.findByBillNumber(
                billNumber.trim()
        );
    }





    @Override
    public List<Bill> findAllBills(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return billingService.findAll();
    }





    @Override
    public List<Bill> findBillsBySubscription(
            Admin admin,
            long subscriptionId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(subscriptionId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid subscription id."
            );
        }


        return billingService.findBySubscriptionId(
                subscriptionId
        );
    }

}