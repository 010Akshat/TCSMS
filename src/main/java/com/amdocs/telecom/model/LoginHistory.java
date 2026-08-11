package com.amdocs.telecom.model;

import java.time.LocalDateTime;

public class LoginHistory {

    private long loginHistoryId;
    private long customerId;
    private LocalDateTime loginTime;
    private String loginStatus;

    public LoginHistory(long loginHistoryId,
                        long customerId,
                        LocalDateTime loginTime,
                        String loginStatus) {

        this.loginHistoryId = loginHistoryId;
        this.customerId = customerId;
        this.loginTime = loginTime;
        this.loginStatus = loginStatus;
    }

    public long getLoginHistoryId() {
        return loginHistoryId;
    }

    public void setLoginHistoryId(long loginHistoryId) {
        this.loginHistoryId = loginHistoryId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}