package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.service.CustomerService;
import com.amdocs.telecom.service.impl.CustomerServiceImpl;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        CustomerService customerService =
                new CustomerServiceImpl();

        long customerId = 2;

        System.out.println("=== CUSTOMER SERVICE TEST ===");

        // 1. Find by ID
        Customer byId =
                customerService.findById(customerId);

        if (byId != null) {
            System.out.println("\n=== FIND BY ID ===");
            System.out.println("Found: " +
                    byId.getFirstName() +
                    " " +
                    byId.getLastName());
        }

        // 2. Find by Email
        Customer byEmail =
                customerService.findByEmail(
                        "akshat.test2@gmail.com"
                );

        if (byEmail != null) {
            System.out.println("\n=== FIND BY EMAIL ===");
            System.out.println("Found: " +
                    byEmail.getFirstName());
        }

        // 3. Find by Mobile
        Customer byMobile =
                customerService.findByMobileNumber(
                        "9876500002"
                );

        if (byMobile != null) {
            System.out.println("\n=== FIND BY MOBILE ===");
            System.out.println("Found: " +
                    byMobile.getFirstName());
        }

        // 4. Find by Username
        Customer byUsername =
                customerService.findByUsername(
                        "akshat_test2"
                );

        if (byUsername != null) {
            System.out.println("\n=== FIND BY USERNAME ===");
            System.out.println("Found: " +
                    byUsername.getFirstName());
        }

        // 5. Find all
        List<Customer> customers =
                customerService.findAll();

        System.out.println("\n=== FIND ALL ===");
        System.out.println(
                "Total customers: " +
                        customers.size()
        );

        for (Customer customer : customers) {

            System.out.println(
                    customer.getCustomerId() +
                            " | " +
                            customer.getFirstName() +
                            " " +
                            customer.getLastName()
            );
        }
    }
}