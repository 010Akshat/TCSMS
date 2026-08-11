package com.amdocs.telecom.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    public static String hashPassword(String password) {

        try {
            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashBytes =
                    messageDigest.digest(
                            password.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "SHA-256 algorithm not available", e
            );
        }
    }

    public static boolean verifyPassword(
            String password,
            String storedHash) {

        return hashPassword(password).equals(storedHash);
    }
}