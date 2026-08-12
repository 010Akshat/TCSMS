package com.amdocs.telecom.model;

import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.enums.PlanType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TelecomPlan {

    private long planId;
    private String planCode;
    private String planName;
    private PlanType planType;
    private BigDecimal monthlyRental;
    private BigDecimal dataAllowanceGB;
    private int voiceMinutes;
    private int smsAllowance;
    private int validityDays;
    private boolean internationalRoaming;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TelecomPlan(long planId,
                       String planCode,
                       String planName,
                       PlanType planType,
                       BigDecimal monthlyRental,
                       BigDecimal dataAllowanceGB,
                       int voiceMinutes,
                       int smsAllowance,
                       int validityDays,
                       boolean internationalRoaming,
                       AccountStatus status,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt) {

        this.planId = planId;
        this.planCode = planCode;
        this.planName = planName;
        this.planType = planType;
        this.monthlyRental = monthlyRental;
        this.dataAllowanceGB = dataAllowanceGB;
        this.voiceMinutes = voiceMinutes;
        this.smsAllowance = smsAllowance;
        this.validityDays = validityDays;
        this.internationalRoaming = internationalRoaming;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    public BigDecimal getMonthlyRental() {
        return monthlyRental;
    }

    public void setMonthlyRental(BigDecimal monthlyRental) {
        this.monthlyRental = monthlyRental;
    }

    public BigDecimal getDataAllowanceGB() {
        return dataAllowanceGB;
    }

    public void setDataAllowanceGB(BigDecimal dataAllowanceGB) {
        this.dataAllowanceGB = dataAllowanceGB;
    }

    public int getVoiceMinutes() {
        return voiceMinutes;
    }

    public void setVoiceMinutes(int voiceMinutes) {
        this.voiceMinutes = voiceMinutes;
    }

    public int getSmsAllowance() {
        return smsAllowance;
    }

    public void setSmsAllowance(int smsAllowance) {
        this.smsAllowance = smsAllowance;
    }

    public int getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(int validityDays) {
        this.validityDays = validityDays;
    }

    public boolean isInternationalRoaming() {
        return internationalRoaming;
    }

    public void setInternationalRoaming(boolean internationalRoaming) {
        this.internationalRoaming = internationalRoaming;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
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