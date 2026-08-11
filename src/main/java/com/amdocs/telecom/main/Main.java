package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.service.AuthenticationService;
import com.amdocs.telecom.service.CustomerService;
import com.amdocs.telecom.service.impl.AuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.CustomerServiceImpl;
import com.amdocs.telecom.security.CaptchaGenerator;

public class Main {

    public static void main(String[] args) {

        AuthenticationService authenticationService =
                new AuthenticationServiceImpl();

        CustomerService customerService =
                new CustomerServiceImpl();

        CaptchaGenerator captchaGenerator =
                new CaptchaGenerator();

        String username = "registration_test_999";

        System.out.println("=== NEW CUSTOMER FAILED LOGIN TEST ===");

        // ==========================================
        // GET CUSTOMER BEFORE TEST
        // ==========================================

        Customer before =
                customerService.findByUsername(username);

        if (before == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println(
                "Customer ID: " +
                        before.getCustomerId()
        );

        System.out.println(
                "Failed attempts before: " +
                        before.getFailedLoginAttempts()
        );


        // ==========================================
        // GENERATE CAPTCHA
        // ==========================================

        String captcha =
                captchaGenerator.generateCaptcha();

        String[] parts = captcha.split(" ");

        int captchaAnswer =
                Integer.parseInt(parts[0]) +
                        Integer.parseInt(parts[2]);

        System.out.println(
                "CAPTCHA: " + captcha
        );


        // ==========================================
        // WRONG PASSWORD
        // ==========================================

        try {

            Customer result =
                    authenticationService.login(
                            username,
                            "Wrong@999",
                            captcha,
                            captchaAnswer
                    );

            if (result == null) {
                System.out.println(
                        "Wrong password rejected: PASSED"
                );
            } else {
                System.out.println(
                        "Wrong password rejected: FAILED"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Wrong password result: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // CHECK FAILED ATTEMPT COUNT
        // ==========================================

        Customer afterWrongPassword =
                customerService.findByUsername(username);

        System.out.println(
                "Failed attempts after wrong password: " +
                        afterWrongPassword.getFailedLoginAttempts()
        );


        // ==========================================
        // CORRECT PASSWORD
        // ==========================================

        String correctCaptcha =
                captchaGenerator.generateCaptcha();

        String[] correctParts =
                correctCaptcha.split(" ");

        int correctCaptchaAnswer =
                Integer.parseInt(correctParts[0]) +
                        Integer.parseInt(correctParts[2]);

        System.out.println(
                "Correct login CAPTCHA: " +
                        correctCaptcha
        );

        try {

            Customer result =
                    authenticationService.login(
                            username,
                            "Test@123",
                            correctCaptcha,
                            correctCaptchaAnswer
                    );

            if (result != null) {

                System.out.println(
                        "Correct password login: PASSED"
                );

            } else {

                System.out.println(
                        "Correct password login: FAILED"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Correct password exception: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // CHECK FINAL STATE
        // ==========================================

        Customer afterSuccessfulLogin =
                customerService.findByUsername(username);

        System.out.println(
                "Failed attempts after successful login: " +
                        afterSuccessfulLogin.getFailedLoginAttempts()
        );

        System.out.println(
                "Last login: " +
                        afterSuccessfulLogin.getLastLogin()
        );

        System.out.println(
                "\n=== FAILED LOGIN TEST COMPLETED ==="
        );
    }
}