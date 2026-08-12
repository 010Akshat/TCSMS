package com.amdocs.telecom.model;

import com.amdocs.telecom.model.enums.SimType;
import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MobileSubscription {

    private long subscriptionId;
    private String subscriptionNumber;
    private long customerId;
    private long planId;
    private String mobileNumber;
    private String simNumber;
    private SimType simType;
    private LocalDate activationDate;
    private SubscriptionType subscriptionType;
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MobileSubscription(
            long subscriptionId,
            String subscriptionNumber,
            long customerId,
            long planId,
            String mobileNumber,
            String simNumber,
            SimType simType,
            LocalDate activationDate,
            SubscriptionType subscriptionType,
            SubscriptionStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.subscriptionId = subscriptionId;
        this.subscriptionNumber = subscriptionNumber;
        this.customerId = customerId;
        this.planId = planId;
        this.mobileNumber = mobileNumber;
        this.simNumber = simNumber;
        this.simType = simType;
        this.activationDate = activationDate;
        this.subscriptionType = subscriptionType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionNumber() {
        return subscriptionNumber;
    }

    public void setSubscriptionNumber(String subscriptionNumber) {
        this.subscriptionNumber = subscriptionNumber;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public SimType getSimType() {
        return simType;
    }

    public void setSimType(SimType simType) {
        this.simType = simType;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(
            SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}