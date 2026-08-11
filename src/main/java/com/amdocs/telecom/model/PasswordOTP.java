package com.amdocs.telecom.model;

import java.time.LocalDateTime;

public class PasswordOTP {

    private long otpId;
    private long customerId;
    private String otpCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;

    public PasswordOTP(long otpId,
                       long customerId,
                       String otpCode,
                       LocalDateTime createdAt,
                       LocalDateTime expiresAt,
                       boolean used) {

        this.otpId = otpId;
        this.customerId = customerId;
        this.otpCode = otpCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.used = used;
    }

    public long getOtpId() {
        return otpId;
    }

    public void setOtpId(long otpId) {
        this.otpId = otpId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}