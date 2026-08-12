package com.amdocs.telecom.validation;

import com.amdocs.telecom.model.Customer;

import java.time.LocalDate;
import java.time.Period;

public class CustomerValidator {

    private CustomerValidator() {
        // Utility class
    }

    public static void validateMandatoryFields(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }

        if (isBlank(customer.getCustomerNumber())) {
            throw new IllegalArgumentException(
                    "Customer number is mandatory."
            );
        }

        if (isBlank(customer.getFirstName())) {
            throw new IllegalArgumentException(
                    "First name is mandatory."
            );
        }

        if (isBlank(customer.getLastName())) {
            throw new IllegalArgumentException(
                    "Last name is mandatory."
            );
        }

        if (customer.getDateOfBirth() == null) {
            throw new IllegalArgumentException(
                    "Date of birth is mandatory."
            );
        }

        if (isBlank(customer.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is mandatory."
            );
        }

        if (isBlank(customer.getMobileNumber())) {
            throw new IllegalArgumentException(
                    "Mobile number is mandatory."
            );
        }

        if (isBlank(customer.getAddress())) {
            throw new IllegalArgumentException(
                    "Address is mandatory."
            );
        }

        if (isBlank(customer.getCity())) {
            throw new IllegalArgumentException(
                    "City is mandatory."
            );
        }

        if (isBlank(customer.getCountry())) {
            throw new IllegalArgumentException(
                    "Country is mandatory."
            );
        }

        if (isBlank(customer.getUsername())) {
            throw new IllegalArgumentException(
                    "Username is mandatory."
            );
        }
    }

    public static void validateAge(LocalDate dateOfBirth) {

        if (dateOfBirth == null) {
            throw new IllegalArgumentException(
                    "Date of birth is mandatory."
            );
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date of birth cannot be in the future."
            );
        }

        int age = Period.between(
                dateOfBirth,
                LocalDate.now()
        ).getYears();

        if (age < 18) {
            throw new IllegalArgumentException(
                    "Customer must be at least 18 years old."
            );
        }
    }

    public static void validatePassword(String password) {

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException(
                    "Password must contain at least one uppercase letter."
            );
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException(
                    "Password must contain at least one lowercase letter."
            );
        }

        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException(
                    "Password must contain at least one digit."
            );
        }

        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            throw new IllegalArgumentException(
                    "Password must contain at least one special character."
            );
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}