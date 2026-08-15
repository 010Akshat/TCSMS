package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.security.AdminAuthorizationUtil;
import com.amdocs.telecom.service.AdminSubscriptionService;
import com.amdocs.telecom.service.SubscriptionService;


public class AdminSubscriptionServiceImpl
        implements AdminSubscriptionService {


    private final SubscriptionService subscriptionService;


    public AdminSubscriptionServiceImpl() {

        this.subscriptionService =
                new SubscriptionServiceImpl();
    }



    @Override
    public void upgradePlan(
            Admin admin,
            long subscriptionId,
            long newPlanId,
            String reason) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        subscriptionService.upgradePlan(
                subscriptionId,
                newPlanId,
                reason,
                admin.getAdminUsername()
        );
    }



    @Override
    public void downgradePlan(
            Admin admin,
            long subscriptionId,
            long newPlanId,
            String reason) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        subscriptionService.downgradePlan(
                subscriptionId,
                newPlanId,
                reason,
                admin.getAdminUsername()
        );
    }



    @Override
    public void changeSubscriptionType(
            Admin admin,
            long subscriptionId,
            String newType,
            String reason) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        subscriptionService.changeSubscriptionType(
                subscriptionId,
                newType,
                reason,
                admin.getAdminUsername()
        );
    }



    @Override
    public void activateSubscription(
            Admin admin,
            long subscriptionId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        subscriptionService.activateSubscription(
                subscriptionId
        );
    }



    @Override
    public void deactivateSubscription(
            Admin admin,
            long subscriptionId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        subscriptionService.deactivateSubscription(
                subscriptionId
        );
    }

}