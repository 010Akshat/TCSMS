package com.amdocs.telecom.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    private long paymentId;
    private String transactionReference;
    private long billId;
    private long customerId;
    private BigDecimal amount;
    private PaymentMode paymentMode;
    private LocalDateTime paymentDate;
    private PaymentStatus paymentStatus;

    public Payment(
            long paymentId,
            String transactionReference,
            long billId,
            long customerId,
            BigDecimal amount,
            PaymentMode paymentMode,
            LocalDateTime paymentDate,
            PaymentStatus paymentStatus) {

        this.paymentId = paymentId;
        this.transactionReference = transactionReference;
        this.billId = billId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(
            String transactionReference) {
        this.transactionReference = transactionReference;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(
            PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(
            LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(
            PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}