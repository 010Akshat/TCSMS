package com.amdocs.telecom.service;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.model.Customer;

public interface AuthenticationService {

    Customer login(
            String username,
            String password,
            String captcha,
            int captchaAnswer
    );

    PasswordOTP generatePasswordResetOTP(long customerId);

    boolean verifyOTP(
            long customerId,
            String otpCode
    );

    boolean resetPassword(
            long customerId,
            String newPassword
    );

    void logout(long customerId);
}