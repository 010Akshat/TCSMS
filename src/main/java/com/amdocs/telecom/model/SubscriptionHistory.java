package com.amdocs.telecom.model;

import java.time.LocalDateTime;

public class SubscriptionHistory {

    private long historyId;
    private long subscriptionId;
    private long oldPlanId;
    private long newPlanId;
    private LocalDateTime changeDate;
    private String changeReason;
    private String changedBy;

    public SubscriptionHistory(long historyId,
                               long subscriptionId,
                               long oldPlanId,
                               long newPlanId,
                               LocalDateTime changeDate,
                               String changeReason,
                               String changedBy) {

        this.historyId = historyId;
        this.subscriptionId = subscriptionId;
        this.oldPlanId = oldPlanId;
        this.newPlanId = newPlanId;
        this.changeDate = changeDate;
        this.changeReason = changeReason;
        this.changedBy = changedBy;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getOldPlanId() {
        return oldPlanId;
    }

    public void setOldPlanId(long oldPlanId) {
        this.oldPlanId = oldPlanId;
    }

    public long getNewPlanId() {
        return newPlanId;
    }

    public void setNewPlanId(long newPlanId) {
        this.newPlanId = newPlanId;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
}