package com.amdocs.telecom.main.Console;

import com.amdocs.telecom.dao.LoginHistoryDAO;
import com.amdocs.telecom.dao.impl.LoginHistoryDAOImpl;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.LoginHistory;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.security.CaptchaGenerator;
import com.amdocs.telecom.service.AuthenticationService;
import com.amdocs.telecom.service.impl.AuthenticationServiceImpl;

import java.util.List;
import java.util.Scanner;

public class AuthenticationConsole {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final AuthenticationService authenticationService =
            new AuthenticationServiceImpl();

    private static final LoginHistoryDAO loginHistoryDAO =
            new LoginHistoryDAOImpl();

    private static final CaptchaGenerator captchaGenerator =
            new CaptchaGenerator();


    public static void main(String[] args) {

        while (true) {

            printMenu();

            int choice =
                    readInt(
                            "Enter your choice: "
                    );

            try {

                switch (choice) {

                    // ==========================================
                    // 1. GENERATE CAPTCHA
                    // ==========================================
                    case 1:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 1
                         *
                         * Example output:
                         * CAPTCHA: 5 + 7
                         *
                         * NOTE:
                         * Remember the CAPTCHA because option 2
                         * requires the answer.
                         */

                        String generatedCaptcha =
                                captchaGenerator.generateCaptcha();

                        System.out.println(
                                "\nGenerated CAPTCHA: " +
                                        generatedCaptcha
                        );

                        break;


                    // ==========================================
                    // 2. VALIDATE CAPTCHA
                    // ==========================================
                    case 2:

                        /*
                         * VALID EXAMPLE:
                         *
                         * If CAPTCHA shown is:
                         * 5 + 7
                         *
                         * Input:
                         * 12
                         *
                         *
                         * WRONG EXAMPLE:
                         *
                         * 13
                         *
                         * Expected:
                         * CAPTCHA rejected.
                         */

                        System.out.print(
                                "Enter CAPTCHA expression: "
                        );

                        String captcha =
                                scanner.nextLine().trim();

                        System.out.print(
                                "Enter CAPTCHA answer: "
                        );

                        int captchaAnswer =
                                readIntWithoutPrompt();

                        boolean captchaValid =
                                captchaGenerator.validateCaptcha(
                                        captcha,
                                        captchaAnswer
                                );

                        if (captchaValid) {

                            System.out.println(
                                    "CAPTCHA validation successful."
                            );

                        } else {

                            System.out.println(
                                    "Invalid CAPTCHA."
                            );
                        }

                        break;


                    // ==========================================
                    // 3. CUSTOMER LOGIN
                    // ==========================================
                    case 3:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Don't Change anything for
                         * 21 -> akshat_jpr : Akshat@333
                         * Username:
                         * 2 , akshat_test2
                         *
                         * Password:
                         * Test@12345
                         *
                         * CAPTCHA:
                         * 5 + 7
                         *
                         * CAPTCHA Answer:
                         * 12
                         *
                         *
                         * WRONG PASSWORD EXAMPLE:
                         *
                         * Wrong@12345
                         *
                         *
                         * WRONG CAPTCHA EXAMPLE:
                         *
                         * Correct CAPTCHA displayed:
                         * 5 + 7
                         *
                         * Enter:
                         * 13
                         *
                         *
                         * WRONG USERNAME EXAMPLE:
                         *
                         * unknown_user
                         *
                         *
                         * IMPORTANT:
                         * After repeated wrong passwords, the
                         * authentication service may lock the
                         * customer account according to your
                         * existing AuthenticationServiceImpl.
                         */

                        System.out.print(
                                "Enter username: "
                        );

                        String loginUsername =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter password: "
                        );

                        String loginPassword =
                                scanner.nextLine();


                        String loginCaptcha =
                                captchaGenerator.generateCaptcha();


                        System.out.println(
                                "CAPTCHA: " +
                                        loginCaptcha
                        );


                        System.out.print(
                                "Enter CAPTCHA answer: "
                        );

                        int loginCaptchaAnswer =
                                readIntWithoutPrompt();


                        Customer loggedIn =
                                authenticationService.login(
                                        loginUsername,
                                        loginPassword,
                                        loginCaptcha,
                                        loginCaptchaAnswer
                                );


                        if (loggedIn != null) {

                            System.out.println(
                                    "\nLOGIN SUCCESSFUL"
                            );

                            printCustomerSummary(
                                    loggedIn
                            );

                        } else {

                            System.out.println(
                                    "\nLOGIN FAILED"
                            );
                        }

                        break;


                    // ==========================================
                    // 4. VIEW LOGIN HISTORY
                    // ==========================================
                    case 4:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Customer ID:
                         * 3
                         *
                         * WRONG EXAMPLE:
                         *
                         * 999999
                         *
                         * Expected:
                         * No login history found.
                         *
                         * This uses the existing LoginHistoryDAO
                         * because your current AuthenticationService
                         * interface does not expose a login-history
                         * retrieval method.
                         */

                        int historyCustomerId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        List<LoginHistory> history =
                                loginHistoryDAO
                                        .findByCustomerId(
                                                historyCustomerId
                                        );


                        if (history != null &&
                                !history.isEmpty()) {

                            System.out.println(
                                    "\n========== LOGIN HISTORY =========="
                            );


                            for (LoginHistory record : history) {

                                System.out.println(
                                        "Login History ID: " +
                                                record.getLoginHistoryId()
                                );

                                System.out.println(
                                        "Customer ID: " +
                                                record.getCustomerId()
                                );

                                System.out.println(
                                        "Login Time: " +
                                                record.getLoginTime()
                                );

                                System.out.println(
                                        "Login Status: " +
                                                record.getLoginStatus()
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No login history found."
                            );
                        }

                        break;


                    // ==========================================
                    // 5. GENERATE PASSWORD RESET OTP
                    // ==========================================
                    case 5:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Customer ID:
                         * 3
                         *
                         *
                         * WRONG EXAMPLE:
                         *
                         * 999999
                         *
                         * Expected:
                         * Appropriate customer/OTP validation
                         * message from the service.
                         */

                        int otpCustomerId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        PasswordOTP otp =
                                authenticationService
                                        .generatePasswordResetOTP(
                                                otpCustomerId
                                        );


                        if (otp != null) {

                            System.out.println(
                                    "\nPASSWORD RESET OTP GENERATED"
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

                            System.out.println(
                                    "OTP generation failed."
                            );
                        }

                        break;


                    // ==========================================
                    // 6. VERIFY OTP
                    // ==========================================
                    case 6:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Customer ID:
                         * 3
                         *
                         * OTP:
                         * 123456
                         *
                         *
                         * WRONG EXAMPLE:
                         *
                         * 000000
                         *
                         * Expected:
                         * Invalid OTP.
                         *
                         * NOTE:
                         * Use the OTP printed by option 5.
                         */

                        int verifyCustomerId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        System.out.print(
                                "Enter OTP: "
                        );

                        String otpCode =
                                scanner.nextLine().trim();


                        boolean otpVerified =
                                authenticationService.verifyOTP(
                                        verifyCustomerId,
                                        otpCode
                                );


                        if (otpVerified) {

                            System.out.println(
                                    "OTP verified successfully."
                            );

                        } else {

                            System.out.println(
                                    "Invalid or expired OTP."
                            );
                        }

                        break;


                    // ==========================================
                    // 7. RESET PASSWORD
                    // ==========================================
                    case 7:

                        /*
                         * VALID FLOW:
                         *
                         * First use option 5:
                         * Generate OTP
                         *
                         * Then use option 6:
                         * Verify OTP
                         *
                         * Then use option 7:
                         *
                         * Customer ID:
                         * 3
                         *
                         * New Password:
                         * NewPass@123
                         *
                         *
                         * WRONG PASSWORD EXAMPLE:
                         *
                         * 123
                         *
                         * Expected:
                         * Password validation error.
                         *
                         * IMPORTANT:
                         * Password reset will only succeed if the
                         * authentication service has authorized
                         * the reset through the valid OTP flow.
                         */

                        int resetCustomerId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        System.out.print(
                                "Enter new password: "
                        );

                        String newPassword =
                                scanner.nextLine();


                        boolean passwordReset =
                                authenticationService.resetPassword(
                                        resetCustomerId,
                                        newPassword
                                );


                        if (passwordReset) {

                            System.out.println(
                                    "Password reset successfully."
                            );

                        } else {

                            System.out.println(
                                    "Password reset failed."
                            );
                        }

                        break;


                    // ==========================================
                    // 8. LOGOUT
                    // ==========================================
                    case 8:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Customer ID:
                         * 3
                         *
                         *
                         * WRONG EXAMPLE:
                         *
                         * 0
                         *
                         * Expected:
                         * Appropriate validation message.
                         *
                         * NOTE:
                         * Your current AuthenticationService.logout()
                         * receives only customer ID.
                         */

                        int logoutCustomerId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        authenticationService.logout(
                                logoutCustomerId
                        );


                        System.out.println(
                                "Logout completed."
                        );

                        break;


                    // ==========================================
                    // 0. EXIT
                    // ==========================================
                    case 0:

                        System.out.println(
                                "Exiting Authentication Console..."
                        );

                        scanner.close();

                        return;


                    default:

                        System.out.println(
                                "Invalid menu choice."
                        );

                        System.out.println(
                                "Choose a number from 0 to 8."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "\n=========================================="
                );

                System.out.println(
                        "AUTHENTICATION OPERATION FAILED"
                );

                System.out.println(
                        "Reason: " +
                                e.getMessage()
                );

                System.out.println(
                        "=========================================="
                );
            }


            System.out.println(
                    "\nPress ENTER to continue..."
            );

            scanner.nextLine();
        }
    }


    // ==========================================================
    // MENU
    // ==========================================================

    private static void printMenu() {

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "        AUTHENTICATION CONSOLE"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Generate CAPTCHA"
        );

        System.out.println(
                "2. Validate CAPTCHA"
        );

        System.out.println(
                "3. Customer Login"
        );

        System.out.println(
                "4. View Login History"
        );

        System.out.println(
                "5. Generate Password Reset OTP"
        );

        System.out.println(
                "6. Verify OTP"
        );

        System.out.println(
                "7. Reset Password"
        );

        System.out.println(
                "8. Logout"
        );

        System.out.println(
                "0. Exit"
        );

        System.out.println(
                "=========================================="
        );
    }


    // ==========================================================
    // SAFE INTEGER INPUT
    // ==========================================================

    private static int readInt(
            String message) {

        while (true) {

            System.out.print(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(
                        input
                );

            } catch (NumberFormatException e) {

                /*
                 * WRONG INPUT EXAMPLE:
                 * abc
                 *
                 * EXPECTED:
                 * Invalid numeric input.
                 */

                System.out.println(
                        "Invalid input. Please enter a number."
                );
            }
        }
    }


    private static int readIntWithoutPrompt() {

        while (true) {

            String input =
                    scanner.nextLine().trim();

            try {

                return Integer.parseInt(
                        input
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number."
                );
            }
        }
    }


    // ==========================================================
    // CUSTOMER SUMMARY
    // ==========================================================

    private static void printCustomerSummary(
            Customer customer) {

        System.out.println(
                "Customer ID: " +
                        customer.getCustomerId()
        );

        System.out.println(
                "Customer Number: " +
                        customer.getCustomerNumber()
        );

        System.out.println(
                "Name: " +
                        customer.getFirstName() +
                        " " +
                        customer.getLastName()
        );

        System.out.println(
                "Email: " +
                        customer.getEmail()
        );

        System.out.println(
                "Mobile Number: " +
                        customer.getMobileNumber()
        );

        System.out.println(
                "Account Status: " +
                        customer.getAccountStatus()
        );
    }
}