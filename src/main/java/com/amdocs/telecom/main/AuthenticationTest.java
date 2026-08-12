package com.amdocs.telecom.main;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.LoginHistoryDAO;
import com.amdocs.telecom.dao.PasswordOTPDAO;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.LoginHistoryDAOImpl;
import com.amdocs.telecom.dao.impl.PasswordOTPDAOImpl;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.LoginHistory;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.security.CaptchaGenerator;
import com.amdocs.telecom.service.AuthenticationService;
import com.amdocs.telecom.service.CustomerService;
import com.amdocs.telecom.service.impl.AuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.CustomerServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AuthenticationTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        AuthenticationService authenticationService =
                new AuthenticationServiceImpl();

        CustomerService customerService =
                new CustomerServiceImpl();

        CustomerDAO customerDAO =
                new CustomerDAOImpl();

        LoginHistoryDAO loginHistoryDAO =
                new LoginHistoryDAOImpl();

        PasswordOTPDAO passwordOTPDAO =
                new PasswordOTPDAOImpl();

        CaptchaGenerator captchaGenerator =
                new CaptchaGenerator();

        System.out.println(
                "=== AUTHENTICATION TEST SUITE ==="
        );

        String runId =
                String.valueOf(
                        System.currentTimeMillis()
                );

        String username =
                "auth_test_" + runId;

        String email =
                "auth.test." +
                        runId +
                        "@example.com";

        String mobile =
                "9" +
                        runId.substring(
                                runId.length() - 9
                        );

        String customerNumber =
                "AUTH" +
                        runId;

        String originalPassword =
                "Test@12345";

        String resetPassword =
                "NewPass@123";

        Customer testCustomer =
                new Customer(
                        0,
                        customerNumber,
                        "Auth",
                        "Tester",
                        LocalDate.of(
                                2000,
                                1,
                                1
                        ),
                        email,
                        mobile,
                        "Test Address",
                        "Jaipur",
                        "India",
                        username,
                        null,
                        LocalDateTime.now(),
                        null
                );

        Customer lockTestCustomer = null;

        try {

            // ==========================================
            // CREATE TEST CUSTOMER
            // ==========================================

            customerService.register(
                    testCustomer,
                    originalPassword
            );

            System.out.println(
                    "Test Customer ID: " +
                            testCustomer.getCustomerId()
            );

            // ==========================================
            // TEST 1: VALID CAPTCHA
            // ==========================================

            System.out.println(
                    "\n=== TEST 1: VALID CAPTCHA ==="
            );

            try {

                String captcha =
                        captchaGenerator.generateCaptcha();

                int answer =
                        solveCaptcha(
                                captcha
                        );

                if (captchaGenerator.validateCaptcha(
                        captcha,
                        answer
                )) {

                    pass(
                            "Valid CAPTCHA"
                    );

                    System.out.println(
                            "CAPTCHA: " +
                                    captcha
                    );

                    System.out.println(
                            "Answer: " +
                                    answer
                    );

                } else {

                    fail(
                            "Valid CAPTCHA"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Valid CAPTCHA: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 2: INVALID CAPTCHA
            // ==========================================

            System.out.println(
                    "\n=== TEST 2: INVALID CAPTCHA ==="
            );

            try {

                String captcha =
                        captchaGenerator.generateCaptcha();

                int correctAnswer =
                        solveCaptcha(
                                captcha
                        );

                boolean valid =
                        captchaGenerator.validateCaptcha(
                                captcha,
                                correctAnswer + 1
                        );

                if (!valid) {

                    pass(
                            "Invalid CAPTCHA rejection"
                    );

                } else {

                    fail(
                            "Invalid CAPTCHA rejection"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Invalid CAPTCHA rejection: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 3: SUCCESSFUL LOGIN
            // ==========================================

            System.out.println(
                    "\n=== TEST 3: SUCCESSFUL LOGIN ==="
            );

            try {

                String captcha =
                        captchaGenerator.generateCaptcha();

                int answer =
                        solveCaptcha(
                                captcha
                        );

                Customer loggedIn =
                        authenticationService.login(
                                username,
                                originalPassword,
                                captcha,
                                answer
                        );

                if (loggedIn != null &&
                        loggedIn.getCustomerId()
                                == testCustomer.getCustomerId()) {

                    pass(
                            "Successful login"
                    );

                } else {

                    fail(
                            "Successful login"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Successful login: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 4: LOGIN HISTORY AFTER SUCCESS
            // ==========================================

            System.out.println(
                    "\n=== TEST 4: SUCCESS LOGIN HISTORY ==="
            );

            try {

                List<LoginHistory> history =
                        loginHistoryDAO.findByCustomerId(
                                testCustomer.getCustomerId()
                        );

                boolean found =
                        history.stream()
                                .anyMatch(record ->
                                        "SUCCESS".equals(
                                                record.getLoginStatus()
                                        )
                                );

                if (found) {

                    pass(
                            "Successful login history"
                    );

                } else {

                    fail(
                            "Successful login history"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Successful login history: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 5: WRONG PASSWORD
            // ==========================================

            System.out.println(
                    "\n=== TEST 5: WRONG PASSWORD ==="
            );

            try {

                String captcha =
                        captchaGenerator.generateCaptcha();

                int answer =
                        solveCaptcha(
                                captcha
                        );

                Customer loggedIn =
                        authenticationService.login(
                                username,
                                "Wrong@12345",
                                captcha,
                                answer
                        );

                if (loggedIn == null) {

                    pass(
                            "Wrong password rejection"
                    );

                } else {

                    fail(
                            "Wrong password rejection"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Wrong password rejection: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 6: FAILED LOGIN HISTORY
            // ==========================================

            System.out.println(
                    "\n=== TEST 6: FAILED LOGIN HISTORY ==="
            );

            try {

                List<LoginHistory> history =
                        loginHistoryDAO.findByCustomerId(
                                testCustomer.getCustomerId()
                        );

                boolean found =
                        history.stream()
                                .anyMatch(record ->
                                        "FAILED".equals(
                                                record.getLoginStatus()
                                        )
                                );

                if (found) {

                    pass(
                            "Failed login history"
                    );

                } else {

                    fail(
                            "Failed login history"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Failed login history: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 7: GENERATE OTP
            // ==========================================

            System.out.println(
                    "\n=== TEST 7: GENERATE PASSWORD RESET OTP ==="
            );

            PasswordOTP otp =
                    authenticationService
                            .generatePasswordResetOTP(
                                    testCustomer.getCustomerId()
                            );

            if (otp != null &&
                    otp.getOtpId() > 0 &&
                    otp.getOtpCode() != null &&
                    otp.getOtpCode().matches(
                            "\\d{6}"
                    ) &&
                    !otp.isUsed() &&
                    otp.getExpiresAt()
                            .isAfter(
                                    LocalDateTime.now()
                            )) {

                pass(
                        "OTP generation"
                );

                System.out.println(
                        "OTP ID: " +
                                otp.getOtpId()
                );

                System.out.println(
                        "OTP: " +
                                otp.getOtpCode()
                );

                System.out.println(
                        "Expires At: " +
                                otp.getExpiresAt()
                );

            } else {

                fail(
                        "OTP generation"
                );
            }

            // ==========================================
            // TEST 8: RESET WITHOUT OTP
            // ==========================================

            System.out.println(
                    "\n=== TEST 8: RESET WITHOUT OTP ==="
            );

            try {

                boolean reset =
                        authenticationService.resetPassword(
                                testCustomer.getCustomerId(),
                                resetPassword
                        );

                if (!reset) {

                    pass(
                            "Password reset without OTP rejection"
                    );

                } else {

                    fail(
                            "Password reset without OTP rejection"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Password reset without OTP rejection: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 9: INVALID OTP
            // ==========================================

            System.out.println(
                    "\n=== TEST 9: INVALID OTP ==="
            );

            try {

                boolean verified =
                        authenticationService.verifyOTP(
                                testCustomer.getCustomerId(),
                                "000000"
                        );

                if (!verified) {

                    pass(
                            "Invalid OTP rejection"
                    );

                } else {

                    fail(
                            "Invalid OTP rejection"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Invalid OTP rejection: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 10: VALID OTP
            // ==========================================

            System.out.println(
                    "\n=== TEST 10: VALID OTP ==="
            );

            boolean otpVerified =
                    authenticationService.verifyOTP(
                            testCustomer.getCustomerId(),
                            otp.getOtpCode()
                    );

            if (otpVerified) {

                pass(
                        "Valid OTP verification"
                );

            } else {

                fail(
                        "Valid OTP verification"
                );
            }

            // ==========================================
            // TEST 11: VERIFY OTP MARKED USED
            // ==========================================

            System.out.println(
                    "\n=== TEST 11: OTP MARKED AS USED ==="
            );

            try {

                PasswordOTP usedOtp =
                        passwordOTPDAO
                                .findLatestByCustomerId(
                                        testCustomer
                                                .getCustomerId()
                                );

                if (usedOtp != null &&
                        usedOtp.isUsed()) {

                    pass(
                            "OTP marked as used"
                    );

                } else {

                    fail(
                            "OTP marked as used"
                    );
                }

            } catch (Exception e) {

                fail(
                        "OTP marked as used: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 12: USED OTP CANNOT BE REUSED
            // ==========================================

            System.out.println(
                    "\n=== TEST 12: USED OTP REUSE REJECTION ==="
            );

            try {

                boolean secondVerification =
                        authenticationService.verifyOTP(
                                testCustomer.getCustomerId(),
                                otp.getOtpCode()
                        );

                if (!secondVerification) {

                    pass(
                            "Used OTP reuse rejection"
                    );

                } else {

                    fail(
                            "Used OTP reuse rejection"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Used OTP reuse rejection: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 13: RESET PASSWORD AFTER OTP
            // ==========================================

            System.out.println(
                    "\n=== TEST 13: RESET PASSWORD AFTER OTP ==="
            );

            /*
             * Generate a fresh OTP because the first one
             * has already been consumed.
             */
            PasswordOTP secondOtp =
                    authenticationService
                            .generatePasswordResetOTP(
                                    testCustomer
                                            .getCustomerId()
                            );

            boolean secondOtpVerified =
                    authenticationService.verifyOTP(
                            testCustomer.getCustomerId(),
                            secondOtp.getOtpCode()
                    );

            if (!secondOtpVerified) {

                fail(
                        "Fresh OTP verification for reset"
                );

            } else {

                try {

                    boolean reset =
                            authenticationService
                                    .resetPassword(
                                            testCustomer
                                                    .getCustomerId(),
                                            resetPassword
                                    );

                    if (reset) {

                        pass(
                                "Password reset after OTP"
                        );

                    } else {

                        fail(
                                "Password reset after OTP"
                        );
                    }

                } catch (Exception e) {

                    fail(
                            "Password reset after OTP: " +
                                    e.getMessage()
                    );
                }
            }

            // ==========================================
            // TEST 14: OTP AUTHORIZATION IS ONE-TIME
            // ==========================================

            System.out.println(
                    "\n=== TEST 14: OTP AUTHORIZATION ONE-TIME ==="
            );

            try {

                boolean secondReset =
                        authenticationService.resetPassword(
                                testCustomer.getCustomerId(),
                                "Another@123"
                        );

                if (!secondReset) {

                    pass(
                            "OTP authorization one-time use"
                    );

                } else {

                    fail(
                            "OTP authorization one-time use"
                    );
                }

            } catch (Exception e) {

                fail(
                        "OTP authorization one-time use: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 15: LOGIN WITH RESET PASSWORD
            // ==========================================

            System.out.println(
                    "\n=== TEST 15: LOGIN WITH RESET PASSWORD ==="
            );

            try {

                String captcha =
                        captchaGenerator.generateCaptcha();

                int answer =
                        solveCaptcha(
                                captcha
                        );

                Customer loggedIn =
                        authenticationService.login(
                                username,
                                resetPassword,
                                captcha,
                                answer
                        );

                if (loggedIn != null &&
                        loggedIn.getCustomerId()
                                == testCustomer.getCustomerId()) {

                    pass(
                            "Login with reset password"
                    );

                } else {

                    fail(
                            "Login with reset password"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Login with reset password: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // CREATE SECOND CUSTOMER FOR LOCK TEST
            // ==========================================

            String lockRunId =
                    String.valueOf(
                            System.currentTimeMillis()
                    );

            String lockUsername =
                    "lock_test_" +
                            lockRunId;

            String lockEmail =
                    "lock.test." +
                            lockRunId +
                            "@example.com";

            String lockMobile =
                    "8" +
                            lockRunId.substring(
                                    lockRunId.length() - 9
                            );

            lockTestCustomer =
                    new Customer(
                            0,
                            "LOCK" + lockRunId,
                            "Lock",
                            "Tester",
                            LocalDate.of(
                                    2000,
                                    1,
                                    1
                            ),
                            lockEmail,
                            lockMobile,
                            "Lock Address",
                            "Jaipur",
                            "India",
                            lockUsername,
                            null,
                            LocalDateTime.now(),
                            null
                    );

            customerService.register(
                    lockTestCustomer,
                    originalPassword
            );

            // ==========================================
            // TEST 16: ACCOUNT LOCK
            // ==========================================

            System.out.println(
                    "\n=== TEST 16: ACCOUNT LOCK AFTER 3 FAILURES ==="
            );

            try {

                for (int i = 1; i <= 3; i++) {

                    String captcha =
                            captchaGenerator.generateCaptcha();

                    int answer =
                            solveCaptcha(
                                    captcha
                            );

                    authenticationService.login(
                            lockUsername,
                            "Wrong@12345",
                            captcha,
                            answer
                    );
                }

                Customer locked =
                        customerDAO.findById(
                                lockTestCustomer.getCustomerId()
                        );

                if (locked != null &&
                        locked.getFailedLoginAttempts()
                                >= 3 &&
                        locked.getLockedUntil() != null &&
                        locked.getLockedUntil()
                                .isAfter(
                                        LocalDateTime.now()
                                )) {

                    pass(
                            "Account lock after 3 failures"
                    );

                    System.out.println(
                            "Failed attempts: " +
                                    locked.getFailedLoginAttempts()
                    );

                    System.out.println(
                            "Locked until: " +
                                    locked.getLockedUntil()
                    );

                } else {

                    fail(
                            "Account lock after 3 failures"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Account lock after 3 failures: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 17: LOCKED ACCOUNT CANNOT LOGIN
            // ==========================================

            System.out.println(
                    "\n=== TEST 17: LOCKED ACCOUNT LOGIN REJECTION ==="
            );

            try {

                String captcha =
                        captchaGenerator.generateCaptcha();

                int answer =
                        solveCaptcha(
                                captcha
                        );

                Customer loggedIn =
                        authenticationService.login(
                                lockUsername,
                                originalPassword,
                                captcha,
                                answer
                        );

                if (loggedIn == null) {

                    pass(
                            "Locked account login rejection"
                    );

                } else {

                    fail(
                            "Locked account login rejection"
                    );
                }

            } catch (Exception e) {

                fail(
                        "Locked account login rejection: " +
                                e.getMessage()
                );
            }

            // ==========================================
            // TEST 18: LOGOUT
            // ==========================================

            System.out.println(
                    "\n=== TEST 18: LOGOUT ==="
            );

            try {

                authenticationService.logout(
                        testCustomer.getCustomerId()
                );

                pass(
                        "Logout"
                );

            } catch (Exception e) {

                fail(
                        "Logout: " +
                                e.getMessage()
                );
            }

        } finally {

            // ==========================================
            // CLEANUP
            // ==========================================

            cleanupCustomer(
                    testCustomer,
                    customerDAO,
                    customerService
            );

            cleanupCustomer(
                    lockTestCustomer,
                    customerDAO,
                    customerService
            );
        }

        // ==========================================
        // FINAL RESULT
        // ==========================================

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "TOTAL PASSED: " +
                        passed
        );

        System.out.println(
                "TOTAL FAILED: " +
                        failed
        );

        System.out.println(
                "=========================================="
        );

        if (failed == 0) {

            System.out.println(
                    "AUTHENTICATION TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "AUTHENTICATION TEST SUITE: FAILED"
            );
        }
    }

    private static int solveCaptcha(
            String captcha) {

        String[] parts =
                captcha.split(" ");

        int first =
                Integer.parseInt(
                        parts[0]
                );

        int second =
                Integer.parseInt(
                        parts[2]
                );

        return first + second;
    }

    private static void cleanupCustomer(
            Customer customer,
            CustomerDAO customerDAO,
            CustomerService customerService) {

        if (customer == null ||
                customer.getCustomerId() <= 0) {

            return;
        }

        try {

            // Delete dependent OTP records first
            PasswordOTPDAO passwordOTPDAO =
                    new PasswordOTPDAOImpl();

            passwordOTPDAO.deleteByCustomerId(
                    customer.getCustomerId()
            );

            // Delete dependent login history
            LoginHistoryDAO loginHistoryDAO =
                    new LoginHistoryDAOImpl();

            loginHistoryDAO.deleteByCustomerId(
                    customer.getCustomerId()
            );

            // Reset lock fields just in case
            customerDAO.updateFailedLoginAttempts(
                    customer.getCustomerId(),
                    0
            );

            customerDAO.updateLockStatus(
                    customer.getCustomerId(),
                    null
            );

            // Finally delete customer
            customerService.delete(
                    customer.getCustomerId()
            );

        } catch (Exception e) {

            System.out.println(
                    "Cleanup warning for customer " +
                            customer.getCustomerId() +
                            ": " +
                            e.getMessage()
            );
        }
    }

    private static void pass(
            String testName) {

        passed++;

        System.out.println(
                testName +
                        ": PASSED"
        );
    }

    private static void fail(
            String testName) {

        failed++;

        System.out.println(
                testName +
                        ": FAILED"
        );
    }
}