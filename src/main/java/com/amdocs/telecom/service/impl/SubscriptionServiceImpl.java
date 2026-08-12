package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.dao.SubscriptionHistoryDAO;

import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.PlanDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionHistoryDAOImpl;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.model.TelecomPlan;

import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.enums.SimType;
import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;

import com.amdocs.telecom.service.SubscriptionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SubscriptionServiceImpl
        implements SubscriptionService {

    private final CustomerDAO customerDAO;
    private final PlanDAO planDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final SubscriptionHistoryDAO historyDAO;

    public SubscriptionServiceImpl() {

        this.customerDAO =
                new CustomerDAOImpl();

        this.planDAO =
                new PlanDAOImpl();

        this.subscriptionDAO =
                new SubscriptionDAOImpl();

        this.historyDAO =
                new SubscriptionHistoryDAOImpl();
    }

    @Override
    public MobileSubscription subscribe(
            long customerId,
            long planId,
            String mobileNumber,
            String simNumber,
            String simType,
            String subscriptionType) {

        // ==========================================
        // 1. VALIDATE INPUTS
        // ==========================================

        if (mobileNumber == null ||
                mobileNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Mobile number is mandatory."
            );
        }

        if (simNumber == null ||
                simNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "SIM number is mandatory."
            );
        }

        if (simType == null ||
                simType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "SIM type is mandatory."
            );
        }

        if (subscriptionType == null ||
                subscriptionType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Subscription type is mandatory."
            );
        }

        // ==========================================
        // 2. CUSTOMER VALIDATION
        // ==========================================

        Customer customer =
                customerDAO.findById(
                        customerId
                );

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Customer not found."
            );
        }

        // ==========================================
        // 3. PLAN VALIDATION
        // ==========================================

        TelecomPlan plan =
                planDAO.findById(
                        planId
                );

        if (plan == null) {

            throw new IllegalArgumentException(
                    "Plan not found."
            );
        }

        if (plan.getStatus()
                != AccountStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Inactive plan cannot be selected."
            );
        }

        // ==========================================
        // 4. MOBILE NUMBER UNIQUENESS
        // ==========================================

        MobileSubscription existingMobile =
                subscriptionDAO.findByMobileNumber(
                        mobileNumber.trim()
                );

        if (existingMobile != null) {

            throw new IllegalArgumentException(
                    "Mobile number is already subscribed."
            );
        }

        // ==========================================
        // 5. SIM NUMBER UNIQUENESS
        // ==========================================

        List<MobileSubscription> allSubscriptions =
                subscriptionDAO.findAll();

        boolean simAlreadyUsed =
                allSubscriptions.stream()
                        .anyMatch(subscription ->
                                simNumber.trim()
                                        .equals(
                                                subscription.getSimNumber()
                                        )
                        );

        if (simAlreadyUsed) {

            throw new IllegalArgumentException(
                    "SIM number is already in use."
            );
        }

        // ==========================================
        // 6. PARSE SIM TYPE
        // ==========================================

        SimType simTypeValue;

        try {

            simTypeValue =
                    SimType.valueOf(
                            simType.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid SIM type."
            );
        }

        // ==========================================
        // 7. PARSE SUBSCRIPTION TYPE
        // ==========================================

        SubscriptionType subscriptionTypeValue;

        try {

            subscriptionTypeValue =
                    SubscriptionType.valueOf(
                            subscriptionType
                                    .trim()
                                    .toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid subscription type."
            );
        }

        // ==========================================
        // 8. GENERATE SUBSCRIPTION NUMBER
        // ==========================================

        String subscriptionNumber =
                generateSubscriptionNumber();

        // ==========================================
        // 9. CREATE SUBSCRIPTION
        // ==========================================

        MobileSubscription subscription =
                new MobileSubscription(
                        0,
                        subscriptionNumber,
                        customerId,
                        planId,
                        mobileNumber.trim(),
                        simNumber.trim(),
                        simTypeValue,
                        LocalDate.now(),
                        subscriptionTypeValue,
                        SubscriptionStatus.ACTIVE,
                        null,
                        null
                );

        // ==========================================
        // 10. SAVE
        // ==========================================

        subscriptionDAO.save(
                subscription
        );

        return subscription;
    }

    @Override
    public void upgradePlan(
            long subscriptionId,
            long newPlanId,
            String changeReason,
            String changedBy) {

        changePlan(
                subscriptionId,
                newPlanId,
                changeReason,
                changedBy,
                true
        );
    }

    @Override
    public void downgradePlan(
            long subscriptionId,
            long newPlanId,
            String changeReason,
            String changedBy) {

        changePlan(
                subscriptionId,
                newPlanId,
                changeReason,
                changedBy,
                false
        );
    }

    private void changePlan(
            long subscriptionId,
            long newPlanId,
            String changeReason,
            String changedBy,
            boolean upgrade) {

        // ==========================================
        // 1. VALIDATE CHANGE DETAILS
        // ==========================================

        if (changeReason == null ||
                changeReason.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Change reason is mandatory."
            );
        }

        if (changedBy == null ||
                changedBy.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Changed by is mandatory."
            );
        }

        // ==========================================
        // 2. FIND SUBSCRIPTION
        // ==========================================

        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        // ==========================================
        // 3. FIND NEW PLAN
        // ==========================================

        TelecomPlan newPlan =
                planDAO.findById(
                        newPlanId
                );

        if (newPlan == null) {

            throw new IllegalArgumentException(
                    "New plan not found."
            );
        }

        // ==========================================
        // 4. NEW PLAN MUST BE ACTIVE
        // ==========================================

        if (newPlan.getStatus()
                != AccountStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Inactive plan cannot be selected."
            );
        }

        // ==========================================
        // 5. PLAN MUST BE DIFFERENT
        // ==========================================

        long oldPlanId =
                subscription.getPlanId();

        if (oldPlanId == newPlanId) {

            throw new IllegalArgumentException(
                    "Connection is already using this plan."
            );
        }

        // ==========================================
        // 6. GET OLD PLAN
        // ==========================================

        TelecomPlan oldPlan =
                planDAO.findById(
                        oldPlanId
                );

        if (oldPlan == null) {

            throw new IllegalArgumentException(
                    "Current plan not found."
            );
        }

        BigDecimal oldRental =
                oldPlan.getMonthlyRental();

        BigDecimal newRental =
                newPlan.getMonthlyRental();

        if (oldRental == null ||
                newRental == null) {

            throw new IllegalArgumentException(
                    "Plan rental is missing."
            );
        }

        // ==========================================
        // 7. UPGRADE VALIDATION
        // ==========================================

        if (upgrade &&
                newRental.compareTo(oldRental) <= 0) {

            throw new IllegalArgumentException(
                    "Upgrade plan must have a higher monthly rental."
            );
        }

        // ==========================================
        // 8. DOWNGRADE VALIDATION
        // ==========================================

        if (!upgrade &&
                newRental.compareTo(oldRental) >= 0) {

            throw new IllegalArgumentException(
                    "Downgrade plan must have a lower monthly rental."
            );
        }

        // ==========================================
        // 9. UPDATE SUBSCRIPTION
        // ==========================================

        subscription.setPlanId(
                newPlanId
        );

        subscriptionDAO.update(
                subscription
        );

        // ==========================================
        // 10. RECORD HISTORY
        // ==========================================

        SubscriptionHistory history =
                new SubscriptionHistory(
                        0,
                        subscriptionId,
                        oldPlanId,
                        newPlanId,
                        LocalDateTime.now(),
                        changeReason.trim(),
                        changedBy.trim()
                );

        historyDAO.save(
                history
        );
    }

    @Override
    public void changeSubscriptionType(
            long subscriptionId,
            String newSubscriptionType,
            String changeReason,
            String changedBy) {

        // ==========================================
        // 1. VALIDATE INPUT
        // ==========================================

        if (newSubscriptionType == null ||
                newSubscriptionType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Subscription type is mandatory."
            );
        }

        if (changeReason == null ||
                changeReason.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Change reason is mandatory."
            );
        }

        if (changedBy == null ||
                changedBy.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Changed by is mandatory."
            );
        }

        // ==========================================
        // 2. FIND SUBSCRIPTION
        // ==========================================

        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        // ==========================================
        // 3. PARSE NEW TYPE
        // ==========================================

        SubscriptionType newType;

        try {

            newType =
                    SubscriptionType.valueOf(
                            newSubscriptionType
                                    .trim()
                                    .toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid subscription type."
            );
        }

        // ==========================================
        // 4. SAME TYPE CHECK
        // ==========================================

        if (subscription.getSubscriptionType()
                == newType) {

            throw new IllegalArgumentException(
                    "Connection is already of this type."
            );
        }

        // ==========================================
        // 5. UPDATE
        // ==========================================

        subscription.setSubscriptionType(
                newType
        );

        subscriptionDAO.update(
                subscription
        );
    }

    @Override
    public void activateSubscription(
            long subscriptionId) {

        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        if (subscription.getStatus()
                == SubscriptionStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Subscription is already active."
            );
        }

        subscription.setStatus(
                SubscriptionStatus.ACTIVE
        );

        subscriptionDAO.update(
                subscription
        );
    }

    @Override
    public void deactivateSubscription(
            long subscriptionId) {

        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        if (subscription.getStatus()
                == SubscriptionStatus.INACTIVE) {

            throw new IllegalArgumentException(
                    "Subscription is already inactive."
            );
        }

        subscription.setStatus(
                SubscriptionStatus.INACTIVE
        );

        subscriptionDAO.update(
                subscription
        );
    }

    @Override
    public MobileSubscription findById(
            long subscriptionId) {

        return subscriptionDAO.findById(
                subscriptionId
        );
    }

    @Override
    public MobileSubscription findBySubscriptionNumber(
            String subscriptionNumber) {

        return subscriptionDAO
                .findBySubscriptionNumber(
                        subscriptionNumber
                );
    }

    @Override
    public MobileSubscription findByMobileNumber(
            String mobileNumber) {

        return subscriptionDAO
                .findByMobileNumber(
                        mobileNumber
                );
    }

    @Override
    public List<MobileSubscription> findByCustomerId(
            long customerId) {

        return subscriptionDAO
                .findByCustomerId(
                        customerId
                );
    }

    @Override
    public List<MobileSubscription> findAll() {

        return subscriptionDAO.findAll();
    }

    @Override
    public List<SubscriptionHistory> findHistory(
            long subscriptionId) {

        return historyDAO
                .findBySubscriptionId(
                        subscriptionId
                );
    }

    private String generateSubscriptionNumber() {

        List<MobileSubscription> subscriptions =
                subscriptionDAO.findAll();

        long nextNumber =
                100001L +
                        subscriptions.size();

        String subscriptionNumber;

        do {

            subscriptionNumber =
                    "SUB" +
                            nextNumber;

            nextNumber++;

        } while (
                subscriptionDAO
                        .findBySubscriptionNumber(
                                subscriptionNumber
                        ) != null
        );

        return subscriptionNumber;
    }
}