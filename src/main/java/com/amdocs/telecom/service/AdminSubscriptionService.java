package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;

public interface AdminSubscriptionService {


    void upgradePlan(
            Admin admin,
            long subscriptionId,
            long newPlanId,
            String reason
    );


    void downgradePlan(
            Admin admin,
            long subscriptionId,
            long newPlanId,
            String reason
    );


    void changeSubscriptionType(
            Admin admin,
            long subscriptionId,
            String newType,
            String reason
    );


    void activateSubscription(
            Admin admin,
            long subscriptionId
    );


    void deactivateSubscription(
            Admin admin,
            long subscriptionId
    );
}