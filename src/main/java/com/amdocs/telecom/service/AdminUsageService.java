package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.enums.UsageType;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;


public interface AdminUsageService {


    UsageRecord findUsageById(
            Admin admin,
            long usageId
    );


    List<UsageRecord> viewUsageBySubscription(
            Admin admin,
            long subscriptionId
    );


    List<UsageRecord> viewAllUsage(
            Admin admin
    );


    Map<UsageType, BigDecimal> viewMonthlyUsage(
            Admin admin,
            YearMonth month
    );


    Map<UsageType, BigDecimal> viewUsageByType(
            Admin admin
    );


    BigDecimal viewTotalDataUsage(
            Admin admin
    );


    BigDecimal viewTotalVoiceUsage(
            Admin admin
    );


    BigDecimal viewTotalSmsUsage(
            Admin admin
    );

}