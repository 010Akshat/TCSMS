package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Customer;
import java.time.LocalDateTime;
import com.amdocs.telecom.model.enums.AccountStatus;

import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminCustomerService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminCustomerServiceImpl;

import java.time.LocalDate;
import java.util.List;


public class AdminCustomerTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminCustomerService adminCustomerService =
                new AdminCustomerServiceImpl();


        System.out.println(
                "=== ADMIN CUSTOMER TEST SUITE ==="
        );


        Admin admin = null;


        // ==========================================
        // TEST 1: ADMIN LOGIN
        // ==========================================

        System.out.println(
                "\n=== TEST 1: ADMIN LOGIN ==="
        );


        try {


            admin =
                    authenticationService.login(
                            "admin",
                            "admin123"
                    );


            if(admin != null &&
                    admin.getAdminId() > 0) {


                pass(
                        "Admin login"
                );


                System.out.println(
                        "Welcome " +
                                admin.getFirstName()
                );


            } else {


                fail(
                        "Admin login"
                );
            }


        } catch(Exception e) {


            fail(
                    "Admin login: " +
                            e.getMessage()
            );
        }



        // ==========================================
        // TEST 2: VIEW ALL CUSTOMERS
        // ==========================================

        System.out.println(
                "\n=== TEST 2: VIEW ALL CUSTOMERS ==="
        );


        try {


            List<Customer> customers =
                    adminCustomerService
                            .findAllCustomers(
                                    admin
                            );


            if(customers != null &&
                    !customers.isEmpty()) {


                pass(
                        "View all customers"
                );


                System.out.println(
                        "Total customers: " +
                                customers.size()
                );


            } else {


                fail(
                        "View all customers"
                );
            }


        } catch(Exception e) {


            fail(
                    "View all customers: " +
                            e.getMessage()
            );
        }


        // ==========================================
// TEST 3: FIND CUSTOMER BY ID
// ==========================================

        System.out.println(
                "\n=== TEST 3: FIND CUSTOMER BY ID ==="
        );


        try {


            Customer firstCustomer =
                    adminCustomerService
                            .findAllCustomers(admin)
                            .get(0);


            Customer found =
                    adminCustomerService
                            .findCustomerById(
                                    admin,
                                    firstCustomer.getCustomerId()
                            );


            if(found != null &&
                    found.getCustomerId()
                            == firstCustomer.getCustomerId()) {


                pass(
                        "Find customer by ID"
                );


            } else {


                fail(
                        "Find customer by ID"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find customer by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 4: FIND CUSTOMER BY EMAIL
// ==========================================

        System.out.println(
                "\n=== TEST 4: FIND CUSTOMER BY EMAIL ==="
        );


        try {


            Customer customer =
                    adminCustomerService
                            .findAllCustomers(admin)
                            .get(0);


            Customer found =
                    adminCustomerService
                            .findCustomerByEmail(
                                    admin,
                                    customer.getEmail()
                            );


            if(found != null &&
                    found.getCustomerId()
                            == customer.getCustomerId()) {


                pass(
                        "Find customer by email"
                );


            } else {


                fail(
                        "Find customer by email"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find customer by email: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 5: FIND CUSTOMER BY MOBILE NUMBER
// ==========================================

        System.out.println(
                "\n=== TEST 5: FIND CUSTOMER BY MOBILE ==="
        );


        try {


            Customer customer =
                    adminCustomerService
                            .findAllCustomers(admin)
                            .get(0);


            Customer found =
                    adminCustomerService
                            .findCustomerByMobile(
                                    admin,
                                    customer.getMobileNumber()
                            );


            if(found != null &&
                    found.getCustomerId()
                            == customer.getCustomerId()) {


                pass(
                        "Find customer by mobile"
                );


            } else {


                fail(
                        "Find customer by mobile"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find customer by mobile: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 6: CREATE CUSTOMER
// ==========================================

        System.out.println(
                "\n=== TEST 6: CREATE CUSTOMER ==="
        );


        Customer createdCustomer = null;


        try {


            String runId =
                    String.valueOf(
                            System.currentTimeMillis()
                    );


            createdCustomer =
                    new Customer(
                            0,
                            "CUST-TEST-" + runId,
                            "Test",
                            "Admin",
                            LocalDate.of(
                                    2000,
                                    1,
                                    1
                            ),
                            "testadmin" + runId + "@mail.com",
                            "88888" +
                                    runId.substring(
                                            runId.length()-5
                                    ),
                            "Test Address",
                            "Jaipur",
                            "India",
                            "testadmin" + runId,
                            null,
                            null,
                            null
                    );


            adminCustomerService.createCustomer(
                    admin,
                    createdCustomer,
                    "Password@123"
            );


            if(createdCustomer.getCustomerId() > 0) {


                pass(
                        "Create customer"
                );


                System.out.println(
                        "Customer ID: " +
                                createdCustomer.getCustomerId()
                );


            } else {


                fail(
                        "Create customer"
                );
            }


        } catch(Exception e) {


            fail(
                    "Create customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 7: UPDATE CUSTOMER
// ==========================================

        System.out.println(
                "\n=== TEST 7: UPDATE CUSTOMER ==="
        );


        try {


            createdCustomer.setAddress(
                    "Updated Admin Address"
            );


            adminCustomerService.updateCustomer(
                    admin,
                    createdCustomer
            );


            Customer updated =
                    adminCustomerService.findCustomerById(
                            admin,
                            createdCustomer.getCustomerId()
                    );


            if(updated != null &&
                    "Updated Admin Address"
                            .equals(
                                    updated.getAddress()
                            )) {


                pass(
                        "Update customer"
                );


            } else {


                fail(
                        "Update customer"
                );
            }


        } catch(Exception e) {


            fail(
                    "Update customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 8: DELETE CUSTOMER
// ==========================================

        System.out.println(
                "\n=== TEST 8: DELETE CUSTOMER ==="
        );


        try {


            long customerId =
                    createdCustomer.getCustomerId();


            adminCustomerService.deleteCustomer(
                    admin,
                    customerId
            );


            Customer deleted =
                    adminCustomerService
                            .findAllCustomers(admin)
                            .stream()
                            .filter(customer ->
                                    customer.getCustomerId()
                                            == customerId
                            )
                            .findFirst()
                            .orElse(null);



            if(deleted == null) {


                pass(
                        "Delete customer"
                );


            } else {


                fail(
                        "Delete customer"
                );
            }


        } catch(Exception e) {


            fail(
                    "Delete customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 9: NULL ADMIN REJECTION
// ==========================================

        System.out.println(
                "\n=== TEST 9: NULL ADMIN REJECTION ==="
        );


        try {


            adminCustomerService.findAllCustomers(
                    null
            );


            fail(
                    "Null admin rejection"
            );


        } catch(SecurityException e) {


            pass(
                    "Null admin rejection"
            );


        } catch(Exception e) {


            fail(
                    "Null admin rejection: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 10: NULL ADMIN UPDATE REJECTION
// ==========================================

        System.out.println(
                "\n=== TEST 10: NULL ADMIN UPDATE REJECTION ==="
        );


        try {


            Customer dummy =
                    new Customer(
                            1,
                            null,
                            "Dummy",
                            "Test",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );


            adminCustomerService.updateCustomer(
                    null,
                    dummy
            );


            fail(
                    "Null admin update rejection"
            );


        } catch(SecurityException e) {


            pass(
                    "Null admin update rejection"
            );


        } catch(Exception e) {


            fail(
                    "Null admin update rejection: " +
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