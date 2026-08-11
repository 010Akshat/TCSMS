package com.amdocs.telecom.service;

import com.amdocs.telecom.model.PasswordOTP;

public interface OTPService {

    PasswordOTP generateOTP(long customerId);

    boolean verifyOTP(long customerId, String otpCode);
}