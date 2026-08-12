package com.amdocs.telecom.model;

import java.time.LocalDateTime;

public class AuditLog {

    private long auditId;
    private String action;
    private long paymentId;
    private long billId;
    private long customerId;
    private LocalDateTime actionDate;
    private String details;

    public AuditLog(
            long auditId,
            String action,
            long paymentId,
            long billId,
            long customerId,
            LocalDateTime actionDate,
            String details) {

        this.auditId = auditId;
        this.action = action;
        this.paymentId = paymentId;
        this.billId = billId;
        this.customerId = customerId;
        this.actionDate = actionDate;
        this.details = details;
    }

    public long getAuditId() {
        return auditId;
    }

    public void setAuditId(long auditId) {
        this.auditId = auditId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}