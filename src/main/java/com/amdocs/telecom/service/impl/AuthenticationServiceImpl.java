package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.LoginHistoryDAO;
import com.amdocs.telecom.dao.PasswordOTPDAO;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.LoginHistoryDAOImpl;
import com.amdocs.telecom.dao.impl.PasswordOTPDAOImpl;
import com.amdocs.telecom.model.AccountStatus;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.LoginHistory;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.security.CaptchaGenerator;
import com.amdocs.telecom.security.PasswordUtil;
import com.amdocs.telecom.service.AuthenticationService;
import com.amdocs.telecom.service.OTPService;
import com.amdocs.telecom.validation.CustomerValidator;

import java.time.LocalDateTime;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerDAO customerDAO;
    private final LoginHistoryDAO loginHistoryDAO;
    private final PasswordOTPDAO passwordOTPDAO;
    private final CaptchaGenerator captchaGenerator;
    private final OTPService otpService;

    public AuthenticationServiceImpl() {

        this.customerDAO = new CustomerDAOImpl();
        this.loginHistoryDAO = new LoginHistoryDAOImpl();
        this.passwordOTPDAO = new PasswordOTPDAOImpl();
        this.captchaGenerator = new CaptchaGenerator();
        this.otpService = new OTPServiceImpl();
    }

    @Override
    public Customer login(
            String username,
            String password,
            String captcha,
            int captchaAnswer) {

        // 1. Validate CAPTCHA
        if (!captchaGenerator.validateCaptcha(captcha, captchaAnswer)) {

            System.out.println("Invalid CAPTCHA.");
            return null;
        }

        // 2. Find customer
        Customer customer =
                customerDAO.findByUsername(username);

        if (customer == null) {

            System.out.println(
                    "Invalid username or password."
            );

            return null;
        }

        // 3. Check account status
        if (customer.getAccountStatus() != AccountStatus.ACTIVE) {

            System.out.println(
                    "Account is not active."
            );

            return null;
        }

        // 4. Check temporary account lock
        if (customer.getLockedUntil() != null &&
                customer.getLockedUntil().isAfter(
                        LocalDateTime.now())) {

            System.out.println(
                    "Account is temporarily locked."
            );

            System.out.println(
                    "Locked until: " +
                            customer.getLockedUntil()
            );

            return null;
        }

        // 5. Verify password
        if (!PasswordUtil.verifyPassword(
                password,
                customer.getPasswordHash())) {

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

            // 6. Lock after 3 failed attempts
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

        // 7. Successful login
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

        // 8. Record successful login
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
                customerDAO.findById(customerId);

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return null;
        }

        return otpService.generateOTP(customerId);
    }

    @Override
    public boolean verifyOTP(
            long customerId,
            String otpCode) {

        return otpService.verifyOTP(
                customerId,
                otpCode
        );
    }

    @Override
    public boolean resetPassword(
            long customerId,
            String newPassword) {

        Customer customer =
                customerDAO.findById(customerId);

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return false;
        }

        // Validate new password
        CustomerValidator.validatePassword(
                newPassword
        );

        // Hash new password
        String passwordHash =
                PasswordUtil.hashPassword(
                        newPassword
                );

        // Update password in database
        customerDAO.updatePassword(
                customerId,
                passwordHash
        );

        System.out.println(
                "Password reset successful."
        );

        return true;
    }

    @Override
    public void logout(long customerId) {

        Customer customer =
                customerDAO.findById(customerId);

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