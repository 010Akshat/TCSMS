package com.amdocs.telecom.model;

import com.amdocs.telecom.model.enums.UsageType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UsageRecord {

    private long usageId;
    private long subscriptionId;
    private LocalDateTime usageDate;
    private UsageType usageType;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal charge;

    public UsageRecord(
            long usageId,
            long subscriptionId,
            LocalDateTime usageDate,
            UsageType usageType,
            BigDecimal quantity,
            String unit,
            BigDecimal charge) {

        this.usageId = usageId;
        this.subscriptionId = subscriptionId;
        this.usageDate = usageDate;
        this.usageType = usageType;
        this.quantity = quantity;
        this.unit = unit;
        this.charge = charge;
    }

    public long getUsageId() {
        return usageId;
    }

    public void setUsageId(long usageId) {
        this.usageId = usageId;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDateTime getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDateTime usageDate) {
        this.usageDate = usageDate;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
}