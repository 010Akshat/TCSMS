package com.amdocs.telecom.service;

import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.SubscriptionHistory;

import java.util.List;

public interface SubscriptionService {

    MobileSubscription subscribe(
            long customerId,
            long planId,
            String mobileNumber,
            String simNumber,
            String simType,
            String subscriptionType
    );

    void upgradePlan(
            long subscriptionId,
            long newPlanId,
            String changeReason,
            String changedBy
    );

    void downgradePlan(
            long subscriptionId,
            long newPlanId,
            String changeReason,
            String changedBy
    );

    void changeSubscriptionType(
            long subscriptionId,
            String newSubscriptionType,
            String changeReason,
            String changedBy
    );

    void activateSubscription(
            long subscriptionId
    );

    void deactivateSubscription(
            long subscriptionId
    );

    MobileSubscription findById(
            long subscriptionId
    );

    MobileSubscription findBySubscriptionNumber(
            String subscriptionNumber
    );

    MobileSubscription findByMobileNumber(
            String mobileNumber
    );

    List<MobileSubscription> findByCustomerId(
            long customerId
    );

    List<MobileSubscription> findAll();

    List<SubscriptionHistory> findHistory(
            long subscriptionId
    );
}