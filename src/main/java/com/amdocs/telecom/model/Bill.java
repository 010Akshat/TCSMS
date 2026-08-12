package com.amdocs.telecom.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bill {

    private long billId;
    private String billNumber;
    private long subscriptionId;
    private LocalDate billingMonth;
    private BigDecimal planRental;
    private BigDecimal usageCharges;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private LocalDate dueDate;
    private BillStatus billStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Bill(
            long billId,
            String billNumber,
            long subscriptionId,
            LocalDate billingMonth,
            BigDecimal planRental,
            BigDecimal usageCharges,
            BigDecimal taxAmount,
            BigDecimal discount,
            BigDecimal totalAmount,
            LocalDate dueDate,
            BillStatus billStatus,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.billId = billId;
        this.billNumber = billNumber;
        this.subscriptionId = subscriptionId;
        this.billingMonth = billingMonth;
        this.planRental = planRental;
        this.usageCharges = usageCharges;
        this.taxAmount = taxAmount;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.dueDate = dueDate;
        this.billStatus = billStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDate getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(LocalDate billingMonth) {
        this.billingMonth = billingMonth;
    }

    public BigDecimal getPlanRental() {
        return planRental;
    }

    public void setPlanRental(BigDecimal planRental) {
        this.planRental = planRental;
    }

    public BigDecimal getUsageCharges() {
        return usageCharges;
    }

    public void setUsageCharges(BigDecimal usageCharges) {
        this.usageCharges = usageCharges;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(BillStatus billStatus) {
        this.billStatus = billStatus;
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