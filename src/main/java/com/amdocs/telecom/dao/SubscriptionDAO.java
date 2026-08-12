package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.MobileSubscription;

import java.util.List;

public interface SubscriptionDAO {

    void save(MobileSubscription subscription);

    MobileSubscription findById(long subscriptionId);

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

    void update(MobileSubscription subscription);

    void delete(long subscriptionId);
}