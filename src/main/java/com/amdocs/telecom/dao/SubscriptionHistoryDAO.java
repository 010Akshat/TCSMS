package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.SubscriptionHistory;

import java.util.List;

public interface SubscriptionHistoryDAO {

    void save(SubscriptionHistory history);

    SubscriptionHistory findById(long historyId);

    List<SubscriptionHistory> findBySubscriptionId(
            long subscriptionId
    );

    List<SubscriptionHistory> findAll();
}