package com.amdocs.telecom.service;

import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.UsageType;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface UsageService {

    void save(UsageRecord usageRecord);

    UsageRecord findById(long usageId);

    List<UsageRecord> findBySubscriptionId(
            long subscriptionId
    );

    List<UsageRecord> findAll();

    void update(UsageRecord usageRecord);

    void delete(long usageId);

    BigDecimal calculateTotalDataUsage();

    BigDecimal calculateTotalVoiceUsage();

    BigDecimal calculateTotalSmsUsage();

    Map<UsageType, BigDecimal> calculateMonthlyUsage(
            YearMonth month
    );

    Map<UsageType, BigDecimal> calculateUsageByType();
}