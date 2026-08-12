package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.dao.SubscriptionHistoryDAO;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.PlanDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionHistoryDAOImpl;
import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.enums.SimType;
import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.service.SubscriptionService;

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

        this.customerDAO = new CustomerDAOImpl();
        this.planDAO = new PlanDAOImpl();
        this.subscriptionDAO = new SubscriptionDAOImpl();
        this.historyDAO = new SubscriptionHistoryDAOImpl();
    }

    @Override
    public MobileSubscription subscribe(
            long customerId,
            long planId,
            String mobileNumber,
            String simNumber,
            String simType,
            String subscriptionType) {

        // 1. Validate required subscription inputs
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

        // 2. Customer must exist
        Customer customer =
                customerDAO.findById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found."
            );
        }

        // 3. Plan must exist
        TelecomPlan plan =
                planDAO.findById(planId);

        if (plan == null) {
            throw new IllegalArgumentException(
                    "Plan not found."
            );
        }

        // 4. Inactive plan cannot be selected
        if (plan.getStatus() != AccountStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Inactive plan cannot be selected."
            );
        }

        // 5. Mobile number cannot already be subscribed
        MobileSubscription existingMobile =
                subscriptionDAO.findByMobileNumber(
                        mobileNumber.trim()
                );

        if (existingMobile != null) {

            throw new IllegalArgumentException(
                    "Mobile number is already subscribed."
            );
        }

        // 6. SIM number cannot already be used
        List<MobileSubscription> allSubscriptions =
                subscriptionDAO.findAll();

        boolean simAlreadyUsed =
                allSubscriptions.stream()
                        .anyMatch(subscription ->
                                subscription.getSimNumber()
                                        .equals(simNumber.trim())
                        );

        if (simAlreadyUsed) {

            throw new IllegalArgumentException(
                    "SIM number is already in use."
            );
        }

        // 7. Parse SIM type
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

        // 8. Parse subscription type
        SubscriptionType subscriptionTypeValue;

        try {

            subscriptionTypeValue =
                    SubscriptionType.valueOf(
                            subscriptionType.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid subscription type."
            );
        }

        // 9. Generate subscription number
        String subscriptionNumber =
                generateSubscriptionNumber();

        // 10. Create subscription
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

        // 11. Save subscription
        subscriptionDAO.save(subscription);

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
                changedBy
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
                changedBy
        );
    }

    private void changePlan(
            long subscriptionId,
            long newPlanId,
            String changeReason,
            String changedBy) {

        // 1. Subscription must exist
        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        // 2. New plan must exist
        TelecomPlan newPlan =
                planDAO.findById(newPlanId);

        if (newPlan == null) {

            throw new IllegalArgumentException(
                    "New plan not found."
            );
        }

        // 3. New plan must be active
        if (newPlan.getStatus() != AccountStatus.ACTIVE) {

            throw new IllegalArgumentException(
                    "Inactive plan cannot be selected."
            );
        }

        // 4. New plan must differ from current plan
        if (subscription.getPlanId() == newPlanId) {

            throw new IllegalArgumentException(
                    "Connection is already using this plan."
            );
        }

        long oldPlanId =
                subscription.getPlanId();

        // 5. Update subscription plan
        subscription.setPlanId(newPlanId);

        subscriptionDAO.update(subscription);

        // 6. Record plan change history
        SubscriptionHistory history =
                new SubscriptionHistory(
                        0,
                        subscriptionId,
                        oldPlanId,
                        newPlanId,
                        LocalDateTime.now(),
                        changeReason,
                        changedBy
                );

        historyDAO.save(history);
    }

    @Override
    public void changeSubscriptionType(
            long subscriptionId,
            String newSubscriptionType,
            String changeReason,
            String changedBy) {

        // 1. Validate input
        if (newSubscriptionType == null ||
                newSubscriptionType.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Subscription type is mandatory."
            );
        }

        // 2. Subscription must exist
        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        // 3. Parse subscription type
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

        // 4. Reject same type
        if (subscription.getSubscriptionType()
                == newType) {

            throw new IllegalArgumentException(
                    "Connection is already of this type."
            );
        }

        // 5. Update subscription type
        subscription.setSubscriptionType(newType);

        subscriptionDAO.update(subscription);
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

        subscription.setStatus(
                SubscriptionStatus.ACTIVE
        );

        subscriptionDAO.update(subscription);
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

        subscription.setStatus(
                SubscriptionStatus.INACTIVE
        );

        subscriptionDAO.update(subscription);
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
                .findByCustomerId(customerId);
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
                100001L + subscriptions.size();

        String subscriptionNumber;

        do {

            subscriptionNumber =
                    "SUB" + nextNumber;

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