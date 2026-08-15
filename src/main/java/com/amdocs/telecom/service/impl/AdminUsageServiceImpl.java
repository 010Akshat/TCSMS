package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.enums.UsageType;

import com.amdocs.telecom.security.AdminAuthorizationUtil;

import com.amdocs.telecom.service.AdminUsageService;
import com.amdocs.telecom.service.UsageService;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;


public class AdminUsageServiceImpl
        implements AdminUsageService {


    private final UsageService usageService;


    public AdminUsageServiceImpl() {

        this.usageService =
                new UsageServiceImpl();

    }



    @Override
    public UsageRecord findUsageById(
            Admin admin,
            long usageId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(usageId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid usage id."
            );
        }


        return usageService.findById(
                usageId
        );
    }





    @Override
    public List<UsageRecord> viewUsageBySubscription(
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


        return usageService
                .findBySubscriptionId(
                        subscriptionId
                );
    }





    @Override
    public List<UsageRecord> viewAllUsage(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return usageService.findAll();
    }





    @Override
    public Map<UsageType, BigDecimal> viewMonthlyUsage(
            Admin admin,
            YearMonth month) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(month == null) {

            throw new IllegalArgumentException(
                    "Month cannot be null."
            );
        }


        return usageService
                .calculateMonthlyUsage(
                        month
                );
    }





    @Override
    public Map<UsageType, BigDecimal> viewUsageByType(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return usageService
                .calculateUsageByType();
    }





    @Override
    public BigDecimal viewTotalDataUsage(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return usageService
                .calculateTotalDataUsage();
    }





    @Override
    public BigDecimal viewTotalVoiceUsage(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return usageService
                .calculateTotalVoiceUsage();
    }





    @Override
    public BigDecimal viewTotalSmsUsage(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return usageService
                .calculateTotalSmsUsage();
    }

}