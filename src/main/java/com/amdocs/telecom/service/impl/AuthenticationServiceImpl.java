package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.LoginHistoryDAO;
import com.amdocs.telecom.dao.PasswordOTPDAO;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.LoginHistoryDAOImpl;
import com.amdocs.telecom.dao.impl.PasswordOTPDAOImpl;
import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.LoginHistory;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.security.CaptchaGenerator;
import com.amdocs.telecom.security.PasswordUtil;
import com.amdocs.telecom.service.AuthenticationService;
import com.amdocs.telecom.service.OTPService;
import com.amdocs.telecom.validation.CustomerValidator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationServiceImpl
        implements AuthenticationService {

    private final CustomerDAO customerDAO;
    private final LoginHistoryDAO loginHistoryDAO;
    private final PasswordOTPDAO passwordOTPDAO;
    private final CaptchaGenerator captchaGenerator;
    private final OTPService otpService;

    /*
     * Stores customers whose OTP has been successfully
     * verified for the current password-reset flow.
     *
     * synchronizedSet makes access safe if multiple
     * threads use the authentication service.
     */
    private final Set<Long> verifiedOTPCustomers =
            Collections.synchronizedSet(
                    new HashSet<Long>()
            );

    public AuthenticationServiceImpl() {

        this.customerDAO =
                new CustomerDAOImpl();

        this.loginHistoryDAO =
                new LoginHistoryDAOImpl();

        this.passwordOTPDAO =
                new PasswordOTPDAOImpl();

        this.captchaGenerator =
                new CaptchaGenerator();

        this.otpService =
                new OTPServiceImpl();
    }

    @Override
    public Customer login(
            String username,
            String password,
            String captcha,
            int captchaAnswer) {

        // ==========================================
        // 1. VALIDATE CAPTCHA
        // ==========================================

        if (!captchaGenerator.validateCaptcha(
                captcha,
                captchaAnswer
        )) {

            System.out.println(
                    "Invalid CAPTCHA."
            );

            return null;
        }

        // ==========================================
        // 2. FIND CUSTOMER
        // ==========================================

        Customer customer =
                customerDAO.findByUsername(
                        username
                );

        if (customer == null) {

            System.out.println(
                    "Invalid username or password."
            );

            return null;
        }

        // ==========================================
        // 3. CHECK ACCOUNT STATUS
        // ==========================================

        if (customer.getAccountStatus()
                != AccountStatus.ACTIVE) {

            System.out.println(
                    "Account is not active."
            );

            return null;
        }

        // ==========================================
        // 4. CHECK TEMPORARY LOCK
        // ==========================================

        if (customer.getLockedUntil() != null &&
                customer.getLockedUntil().isAfter(
                        LocalDateTime.now()
                )) {

            System.out.println(
                    "Account is temporarily locked."
            );

            System.out.println(
                    "Locked until: " +
                            customer.getLockedUntil()
            );

            return null;
        }

        // ==========================================
        // 5. VERIFY PASSWORD
        // ==========================================

        if (!PasswordUtil.verifyPassword(
                password,
                customer.getPasswordHash()
        )) {

            int failedAttempts =
                    customer.getFailedLoginAttempts() + 1;

            customerDAO.updateFailedLoginAttempts(
                    customer.getCustomerId(),
                    failedAttempts
            );

            // Record failed login
            loginHistoryDAO.save(
                    new LoginHistory(
                            0,
                            customer.getCustomerId(),
                            LocalDateTime.now(),
                            "FAILED"
                    )
            );

            // ==========================================
            // 6. LOCK AFTER 3 FAILED ATTEMPTS
            // ==========================================

            if (failedAttempts >= 3) {

                LocalDateTime lockedUntil =
                        LocalDateTime.now()
                                .plusMinutes(10);

                customerDAO.updateLockStatus(
                        customer.getCustomerId(),
                        lockedUntil
                );

                System.out.println(
                        "Maximum failed attempts reached."
                );

                System.out.println(
                        "Account locked until: " +
                                lockedUntil
                );

            } else {

                System.out.println(
                        "Invalid username or password."
                );

                System.out.println(
                        "Failed attempts: " +
                                failedAttempts
                );
            }

            return null;
        }

        // ==========================================
        // 7. SUCCESSFUL LOGIN
        // ==========================================

        customerDAO.updateFailedLoginAttempts(
                customer.getCustomerId(),
                0
        );

        customerDAO.updateLockStatus(
                customer.getCustomerId(),
                null
        );

        LocalDateTime loginTime =
                LocalDateTime.now();

        customerDAO.updateLastLogin(
                customer.getCustomerId(),
                loginTime
        );

        // ==========================================
        // 8. RECORD SUCCESSFUL LOGIN
        // ==========================================

        loginHistoryDAO.save(
                new LoginHistory(
                        0,
                        customer.getCustomerId(),
                        loginTime,
                        "SUCCESS"
                )
        );

        System.out.println(
                "Login successful!"
        );

        System.out.println(
                "Welcome, " +
                        customer.getFirstName() +
                        " " +
                        customer.getLastName()
        );

        return customer;
    }

    @Override
    public PasswordOTP generatePasswordResetOTP(
            long customerId) {

        Customer customer =
                customerDAO.findById(
                        customerId
                );

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return null;
        }

        /*
         * Generating a new OTP starts a new reset flow.
         * Remove any previous in-memory verification state.
         */
        verifiedOTPCustomers.remove(
                customerId
        );

        return otpService.generateOTP(
                customerId
        );
    }

    @Override
    public boolean verifyOTP(
            long customerId,
            String otpCode) {

        boolean verified =
                otpService.verifyOTP(
                        customerId,
                        otpCode
                );

        if (verified) {

            verifiedOTPCustomers.add(
                    customerId
            );
        }

        return verified;
    }

    @Override
    public boolean resetPassword(
            long customerId,
            String newPassword) {

        Customer customer =
                customerDAO.findById(
                        customerId
                );

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return false;
        }

        // ==========================================
        // OTP VERIFICATION REQUIRED
        // ==========================================

        if (!verifiedOTPCustomers.contains(
                customerId
        )) {

            System.out.println(
                    "OTP verification required."
            );

            return false;
        }

        // ==========================================
        // VALIDATE NEW PASSWORD
        // ==========================================

        CustomerValidator.validatePassword(
                newPassword
        );

        // ==========================================
        // HASH NEW PASSWORD
        // ==========================================

        String passwordHash =
                PasswordUtil.hashPassword(
                        newPassword
                );

        // ==========================================
        // UPDATE PASSWORD
        // ==========================================

        customerDAO.updatePassword(
                customerId,
                passwordHash
        );

        /*
         * OTP authorization is one-time use.
         */
        verifiedOTPCustomers.remove(
                customerId
        );

        System.out.println(
                "Password reset successful."
        );

        return true;
    }

    @Override
    public void logout(
            long customerId) {

        Customer customer =
                customerDAO.findById(
                        customerId
                );

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        System.out.println(
                "Customer " +
                        customer.getFirstName() +
                        " " +
                        customer.getLastName() +
                        " logged out successfully."
        );
    }
}