package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.PasswordOTP;

public interface PasswordOTPDAO {

    void save(PasswordOTP passwordOTP);

    PasswordOTP findLatestByCustomerId(long customerId);

    void markAsUsed(long otpId);
}