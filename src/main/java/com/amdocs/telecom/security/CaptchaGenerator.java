package com.amdocs.telecom.security;

import java.util.Random;

public class CaptchaGenerator {

    private final Random random = new Random();

    public String generateCaptcha() {

        int firstNumber = random.nextInt(10);
        int secondNumber = random.nextInt(10);

        return firstNumber + " + " + secondNumber;
    }

    public boolean validateCaptcha(String captcha, int answer) {

        String[] parts = captcha.split(" ");

        int firstNumber = Integer.parseInt(parts[0]);
        int secondNumber = Integer.parseInt(parts[2]);

        return firstNumber + secondNumber == answer;
    }
}