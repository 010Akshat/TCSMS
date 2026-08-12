package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.UsageRecord;

import java.util.List;

public interface UsageDAO {

    void save(UsageRecord usageRecord);

    UsageRecord findById(long usageId);

    List<UsageRecord> findBySubscriptionId(
            long subscriptionId
    );

    List<UsageRecord> findAll();

    void update(UsageRecord usageRecord);

    void delete(long usageId);
}