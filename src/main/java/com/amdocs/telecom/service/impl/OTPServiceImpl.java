package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.PasswordOTPDAO;
import com.amdocs.telecom.dao.impl.PasswordOTPDAOImpl;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.service.OTPService;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class OTPServiceImpl implements OTPService {

    private final PasswordOTPDAO passwordOTPDAO;
    private final SecureRandom secureRandom;

    public OTPServiceImpl() {
        this.passwordOTPDAO = new PasswordOTPDAOImpl();
        this.secureRandom = new SecureRandom();
    }

    @Override
    public PasswordOTP generateOTP(long customerId) {

        // Generate a random 6-digit OTP
        int otpNumber = 100000 + secureRandom.nextInt(900000);

        String otpCode = String.valueOf(otpNumber);

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(5);

        PasswordOTP passwordOTP = new PasswordOTP(
                0,
                customerId,
                otpCode,
                createdAt,
                expiresAt,
                false
        );

        passwordOTPDAO.save(passwordOTP);

        return passwordOTP;
    }

    @Override
    public boolean verifyOTP(
            long customerId,
            String otpCode) {

        PasswordOTP latestOTP =
                passwordOTPDAO.findLatestByCustomerId(customerId);

        if (latestOTP == null) {
            return false;
        }

        // OTP already used
        if (latestOTP.isUsed()) {
            return false;
        }

        // OTP expired
        if (LocalDateTime.now()
                .isAfter(latestOTP.getExpiresAt())) {

            return false;
        }

        // OTP does not match
        if (!latestOTP.getOtpCode().equals(otpCode)) {
            return false;
        }

        // OTP is valid → mark it used
        passwordOTPDAO.markAsUsed(
                latestOTP.getOtpId()
        );

        return true;
    }
}