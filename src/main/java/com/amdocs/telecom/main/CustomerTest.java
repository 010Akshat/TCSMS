package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.service.CustomerService;
import com.amdocs.telecom.service.impl.CustomerServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        CustomerService customerService =
                new CustomerServiceImpl();

        System.out.println(
                "=== CUSTOMER TEST SUITE ==="
        );

        // ==========================================
        // FIND AN EXISTING CUSTOMER
        // ==========================================

        List<Customer> existingCustomers =
                customerService.findAll();

        if (existingCustomers == null ||
                existingCustomers.isEmpty()) {

            System.out.println(
                    "No existing customer found."
            );

            return;
        }

        Customer existingCustomer =
                existingCustomers.get(0);

        // ==========================================
        // TEST 1: FIND CUSTOMER BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 1: FIND CUSTOMER BY ID ==="
        );

        try {

            Customer found =
                    customerService.findById(
                            existingCustomer.getCustomerId()
                    );

            if (found != null &&
                    found.getCustomerId()
                            == existingCustomer.getCustomerId()) {

                pass(
                        "Find customer by ID"
                );

            } else {

                fail(
                        "Find customer by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find customer by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: FIND CUSTOMER BY EMAIL
        // ==========================================

        System.out.println(
                "\n=== TEST 2: FIND CUSTOMER BY EMAIL ==="
        );

        try {

            Customer found =
                    customerService.findByEmail(
                            existingCustomer.getEmail()
                    );

            if (found != null &&
                    found.getCustomerId()
                            == existingCustomer.getCustomerId()) {

                pass(
                        "Find customer by email"
                );

            } else {

                fail(
                        "Find customer by email"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find customer by email: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: FIND CUSTOMER BY MOBILE
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND CUSTOMER BY MOBILE ==="
        );

        try {

            Customer found =
                    customerService.findByMobileNumber(
                            existingCustomer.getMobileNumber()
                    );

            if (found != null &&
                    found.getCustomerId()
                            == existingCustomer.getCustomerId()) {

                pass(
                        "Find customer by mobile"
                );

            } else {

                fail(
                        "Find customer by mobile"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find customer by mobile: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: FIND CUSTOMER BY USERNAME
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND CUSTOMER BY USERNAME ==="
        );

        try {

            Customer found =
                    customerService.findByUsername(
                            existingCustomer.getUsername()
                    );

            if (found != null &&
                    found.getCustomerId()
                            == existingCustomer.getCustomerId()) {

                pass(
                        "Find customer by username"
                );

            } else {

                fail(
                        "Find customer by username"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find customer by username: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: FIND ALL CUSTOMERS
        // ==========================================

        System.out.println(
                "\n=== TEST 5: FIND ALL CUSTOMERS ==="
        );

        try {

            List<Customer> customers =
                    customerService.findAll();

            if (customers != null &&
                    !customers.isEmpty()) {

                pass(
                        "Find all customers"
                );

                System.out.println(
                        "Customers found: " +
                                customers.size()
                );

            } else {

                fail(
                        "Find all customers"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find all customers: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // CREATE UNIQUE TEST CUSTOMER
        // ==========================================

        String uniqueSuffix =
                String.valueOf(
                        System.currentTimeMillis()
                );

        String email =
                "test.customer." +
                        uniqueSuffix +
                        "@example.com";

        String mobileNumber =
                "9" +
                        uniqueSuffix.substring(
                                uniqueSuffix.length() - 9
                        );

        String username =
                "testuser_" +
                        uniqueSuffix;

        String customerNumber =
                "TEST" +
                        uniqueSuffix;

        Customer testCustomer =
                new Customer(
                        0,
                        customerNumber,
                        "Test",
                        "Customer",
                        LocalDate.of(
                                2000,
                                1,
                                1
                        ),
                        email,
                        mobileNumber,
                        "Test Address",
                        "Jaipur",
                        "India",
                        username,
                        null,
                        LocalDateTime.now(),
                        null
                );

        String password =
                "Test@12345";

        // ==========================================
        // TEST 6: REGISTER CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 6: REGISTER CUSTOMER ==="
        );

        try {

            customerService.register(
                    testCustomer,
                    password
            );

            if (testCustomer.getCustomerId() > 0 &&
                    testCustomer.getPasswordHash() != null &&
                    !testCustomer.getPasswordHash()
                            .equals(password)) {

                pass(
                        "Customer registration"
                );

                System.out.println(
                        "Customer ID: " +
                                testCustomer.getCustomerId()
                );

                System.out.println(
                        "Customer Number: " +
                                testCustomer.getCustomerNumber()
                );

                System.out.println(
                        "Account Status: " +
                                testCustomer.getAccountStatus()
                );

            } else {

                fail(
                        "Customer registration"
                );
            }

        } catch (Exception e) {

            fail(
                    "Customer registration: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 7: VERIFY REGISTERED CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 7: VERIFY REGISTERED CUSTOMER ==="
        );

        try {

            Customer registered =
                    customerService.findById(
                            testCustomer.getCustomerId()
                    );

            if (registered != null &&
                    registered.getEmail()
                            .equals(email) &&
                    registered.getUsername()
                            .equals(username)) {

                pass(
                        "Verify registered customer"
                );

            } else {

                fail(
                        "Verify registered customer"
                );
            }

        } catch (Exception e) {

            fail(
                    "Verify registered customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 8: DUPLICATE EMAIL
        // ==========================================

        System.out.println(
                "\n=== TEST 8: DUPLICATE EMAIL ==="
        );

        Customer duplicateEmailCustomer =
                new Customer(
                        0,
                        "DUP" + uniqueSuffix,
                        "Duplicate",
                        "Email",
                        LocalDate.of(
                                2000,
                                2,
                                2
                        ),
                        email,
                        "8" +
                                uniqueSuffix.substring(
                                        uniqueSuffix.length() - 9
                                ),
                        "Duplicate Address",
                        "Jaipur",
                        "India",
                        "duplicate_" + uniqueSuffix,
                        null,
                        LocalDateTime.now(),
                        null
                );

        try {

            customerService.register(
                    duplicateEmailCustomer,
                    password
            );

            fail(
                    "Duplicate email rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Email is already registered"
                    )) {

                pass(
                        "Duplicate email rejection"
                );

            } else {

                fail(
                        "Duplicate email rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 9: UPDATE CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 9: UPDATE CUSTOMER ==="
        );

        try {

            testCustomer.setCity(
                    "Udaipur"
            );

            testCustomer.setAddress(
                    "Updated Test Address"
            );

            customerService.update(
                    testCustomer
            );

            Customer updated =
                    customerService.findById(
                            testCustomer.getCustomerId()
                    );

            if (updated != null &&
                    "Udaipur".equals(
                            updated.getCity()
                    ) &&
                    "Updated Test Address".equals(
                            updated.getAddress()
                    )) {

                pass(
                        "Customer update"
                );

            } else {

                fail(
                        "Customer update"
                );
            }

        } catch (Exception e) {

            fail(
                    "Customer update: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 10: DELETE CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 10: DELETE CUSTOMER ==="
        );

        try {

            long customerId =
                    testCustomer.getCustomerId();

            customerService.delete(
                    customerId
            );

            Customer deleted =
                    customerService.findById(
                            customerId
                    );

            if (deleted == null) {

                pass(
                        "Customer deletion"
                );

            } else {

                fail(
                        "Customer deletion"
                );
            }

        } catch (Exception e) {

            fail(
                    "Customer deletion: " +
                            e.getMessage()
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
                    "CUSTOMER TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "CUSTOMER TEST SUITE: FAILED"
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