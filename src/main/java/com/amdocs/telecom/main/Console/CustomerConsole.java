package com.amdocs.telecom.main.Console;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.service.CustomerService;
import com.amdocs.telecom.service.impl.CustomerServiceImpl;

import java.time.LocalDate;

import java.util.List;
import java.util.Scanner;


public class CustomerConsole {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final CustomerService customerService =
            new CustomerServiceImpl();


    public static void main(String[] args) {

        while (true) {

            printMenu();

            int choice = readInt(
                    "Enter your choice: "
            );


            try {

                switch (choice) {

                    // ==================================================
                    // 1. FIND CUSTOMER BY ID
                    // ==================================================
                    case 1:

                        /*
                         * VALID INPUT:
                         * 3
                         *
                         * WRONG INPUT:
                         * 999999
                         *
                         * OR:
                         * abc
                         *
                         * EXPECTED:
                         * - Existing ID -> customer details
                         * - 999999 -> Customer not found.
                         * - abc -> Invalid customer ID.
                         */

                        int customerId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        Customer customerById =
                                customerService.findById(
                                        customerId
                                );


                        if (customerById != null) {

                            printCustomer(
                                    customerById
                            );

                        } else {

                            System.out.println(
                                    "Customer not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 2. FIND CUSTOMER BY EMAIL
                    // ==================================================
                    case 2:

                        /*
                         * VALID INPUT:
                         * akshat.test2@gmail.com
                         *
                         * WRONG INPUT:
                         * wrong@email.com
                         *
                         * EXPECTED:
                         * Customer not found.
                         */

                        System.out.print(
                                "Enter customer email: "
                        );

                        String email =
                                scanner.nextLine().trim();


                        Customer customerByEmail =
                                customerService.findByEmail(
                                        email
                                );


                        if (customerByEmail != null) {

                            printCustomer(
                                    customerByEmail
                            );

                        } else {

                            System.out.println(
                                    "Customer not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 3. FIND CUSTOMER BY MOBILE
                    // ==================================================
                    case 3:

                        /*
                         * VALID INPUT:
                         * 9556068595
                         *
                         * WRONG INPUT:
                         * 1111111111
                         *
                         * EXPECTED:
                         * Customer not found.
                         */

                        System.out.print(
                                "Enter customer mobile number: "
                        );

                        String mobileNumber =
                                scanner.nextLine().trim();


                        Customer customerByMobile =
                                customerService
                                        .findByMobileNumber(
                                                mobileNumber
                                        );


                        if (customerByMobile != null) {

                            printCustomer(
                                    customerByMobile
                            );

                        } else {

                            System.out.println(
                                    "Customer not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 4. FIND CUSTOMER BY USERNAME
                    // ==================================================
                    case 4:

                        /*
                         * VALID INPUT:
                         * registration_test_999
                         * akshat_test2
                         *
                         * WRONG INPUT:
                         * unknown_user
                         *
                         * EXPECTED:
                         * Customer not found.
                         */

                        System.out.print(
                                "Enter username: "
                        );

                        String username =
                                scanner.nextLine().trim();


                        Customer customerByUsername =
                                customerService.findByUsername(
                                        username
                                );


                        if (customerByUsername != null) {

                            printCustomer(
                                    customerByUsername
                            );

                        } else {

                            System.out.println(
                                    "Customer not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 5. FIND ALL CUSTOMERS
                    // ==================================================
                    case 5:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * VALID:
                         * Just select 5.
                         *
                         * EXPECTED:
                         * All customers printed.
                         */

                        List<Customer> customers =
                                customerService.findAll();


                        if (customers != null &&
                                !customers.isEmpty()) {

                            System.out.println(
                                    "\nCustomers found: " +
                                            customers.size()
                            );

                            System.out.println(
                                    "\n========== CUSTOMER DETAILS =========="
                            );


                            for (Customer customer :
                                    customers) {

                                printCustomer(
                                        customer
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No customers found."
                            );
                        }

                        break;


                    // ==================================================
                    // 6. REGISTER CUSTOMER
                    // ==================================================
                    case 6:

                        /*
                         * VALID INPUT:
                         *
                         * TEST10001
                         * Akshat
                         * Jain
                         * 01-01-2000
                         * newcustomer@example.com
                         * 9876543210
                         * Jaipur Address
                         * Jaipur
                         * India
                         * akshat_test_01
                         * Test@12345
                         *
                         *
                         * WRONG INPUT EXAMPLES:
                         *
                         * 1. Invalid date:
                         * 31-99-2000
                         *
                         * 2. Weak password:
                         * 123
                         *
                         * 3. Duplicate email:
                         * existing@email.com
                         *
                         * 4. Duplicate mobile:
                         * 9876543210
                         *
                         * 5. Duplicate username:
                         * existing_username
                         *
                         * EXPECTED:
                         * CustomerValidator / CustomerService
                         * displays the relevant validation error.
                         */


                        String uniqueSuffix =
                                String.valueOf(
                                        System.currentTimeMillis()
                                );
                        String customerNumber =
                                "TEST" +
                                        uniqueSuffix;


                        System.out.print(
                                "Enter first name: "
                        );

                        String firstName =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter last name: "
                        );

                        String lastName =
                                scanner.nextLine().trim();


                        LocalDate dateOfBirth =
                                readDate(
                                        "Enter date of birth (dd-MM-yyyy): "
                                );


                        System.out.print(
                                "Enter email: "
                        );

                        String customerEmail =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter mobile number: "
                        );

                        String customerMobile =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter address: "
                        );

                        String address =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter city: "
                        );

                        String city =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter country: "
                        );

                        String country =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter username: "
                        );

                        String customerUsername =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter password: "
                        );

                        String password =
                                scanner.nextLine();


                        Customer newCustomer =
                                new Customer(
                                        0,
                                        customerNumber,
                                        firstName,
                                        lastName,
                                        dateOfBirth,
                                        customerEmail,
                                        customerMobile,
                                        address,
                                        city,
                                        country,
                                        customerUsername,
                                        null,
                                        null,
                                        null
                                );


                        customerService.register(
                                newCustomer,
                                password
                        );


                        System.out.println(
                                "\nCustomer registered successfully."
                        );


                        printCustomer(
                                newCustomer
                        );

                        break;



                    // ==================================================
// 7. UPDATE CUSTOMER
// ==================================================
                    case 7
                            :

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Customer ID:
                         * 3
                         *
                         * New Username:
                         * akshat_updated
                         *
                         * New Email:
                         * akshat_updated@example.com
                         *
                         * New Mobile:
                         * 9876543210
                         *
                         * New City:
                         * Udaipur
                         *
                         * New Address:
                         * Updated Customer Address
                         *
                         *
                         * WRONG INPUT EXAMPLE:
                         *
                         * Customer ID:
                         * 3
                         *
                         * New Username:
                         * existing_username
                         *
                         * New Email:
                         * existing@email.com
                         *
                         * New Mobile:
                         * 9876543210
                         *
                         * Expected:
                         * Username / email / mobile already registered.
                         *
                         *
                         * WRONG MOBILE EXAMPLE:
                         *
                         * 98765
                         *
                         * Expected:
                         * Mobile number must be a valid 10-digit mobile number.
                         *
                         *
                         * IMPORTANT:
                         * This console does NOT directly update the database.
                         *
                         * It modifies the Customer object and then calls:
                         *
                         * customerService.update(customerToUpdate);
                         *
                         * The ServiceImpl performs the uniqueness checks.
                         * Only after those checks pass does it call:
                         *
                         * customerDAO.update(customerToUpdate);
                         */

                        int updateId =
                                readInt(
                                        "Enter customer ID: "
                                );


                        Customer customerToUpdate =
                                customerService.findById(
                                        updateId
                                );


                        if (customerToUpdate == null) {

                            System.out.println(
                                    "Customer not found."
                            );

                            break;
                        }


                        System.out.println(
                                "\nCurrent customer details:"
                        );


                        printCustomer(
                                customerToUpdate
                        );


                        // ==========================================
                        // GET NEW USERNAME
                        // ==========================================

                        System.out.print(
                                "\nEnter new username: "
                        );

                        String updatedUsername =
                                scanner.nextLine().trim();


                        // ==========================================
                        // GET NEW EMAIL
                        // ==========================================

                        System.out.print(
                                "Enter new email: "
                        );

                        String updatedEmail =
                                scanner.nextLine().trim();


                        // ==========================================
                        // GET NEW MOBILE
                        // ==========================================

                        System.out.print(
                                "Enter new mobile number: "
                        );

                        String updatedMobile =
                                scanner.nextLine().trim();


                        // ==========================================
                        // GET NEW CITY
                        // ==========================================

                        System.out.print(
                                "Enter new city: "
                        );

                        String updatedCity =
                                scanner.nextLine().trim();


                        // ==========================================
                        // GET NEW ADDRESS
                        // ==========================================

                        System.out.print(
                                "Enter new address: "
                        );

                        String updatedAddress =
                                scanner.nextLine().trim();


                        // ==========================================
                        // SET NEW VALUES IN OBJECT
                        // ==========================================

                        customerToUpdate.setUsername(
                                updatedUsername
                        );

                        customerToUpdate.setEmail(
                                updatedEmail
                        );

                        customerToUpdate.setMobileNumber(
                                updatedMobile
                        );

                        customerToUpdate.setCity(
                                updatedCity
                        );

                        customerToUpdate.setAddress(
                                updatedAddress
                        );


                        // ==========================================
                        // SERVICE LAYER VALIDATION
                        // ==========================================

                        customerService.update(
                                customerToUpdate
                        );


                        System.out.println(
                                "\nCustomer updated successfully."
                        );


                        Customer updatedCustomer =
                                customerService.findById(
                                        updateId
                                );


                        printCustomer(
                                updatedCustomer
                        );

                        break;




                    // ==================================================
                    // 8. DELETE CUSTOMER
                    // ==================================================
                    case 8:

                        /*
                         * VALID INPUT:
                         * 3
                         *
                         * WRONG INPUT:
                         * 999999
                         *
                         * EXPECTED:
                         * Database/service error or no customer
                         * depending on the current DAO behavior.
                         *
                         * WARNING:
                         * Do not use an important existing customer
                         * during the interview.
                         * Use a test customer created through option 6.
                         */

                        int deleteId =
                                readInt(
                                        "Enter customer ID to delete: "
                                );


                        Customer customerToDelete =
                                customerService.findById(
                                        deleteId
                                );


                        if (customerToDelete == null) {

                            System.out.println(
                                    "Customer not found."
                            );

                            break;
                        }


                        System.out.println(
                                "\nCustomer to delete:"
                        );


                        printCustomer(
                                customerToDelete
                        );


                        System.out.print(
                                "Confirm deletion (YES/NO): "
                        );


                        String confirmation =
                                scanner.nextLine()
                                        .trim();


                        if (!"YES".equalsIgnoreCase(
                                confirmation
                        )) {

                            System.out.println(
                                    "Deletion cancelled."
                            );

                            break;
                        }


                        customerService.delete(
                                deleteId
                        );


                        Customer deleted =
                                customerService.findById(
                                        deleteId
                                );


                        if (deleted == null) {

                            System.out.println(
                                    "Customer deleted successfully."
                            );

                        } else {

                            System.out.println(
                                    "Customer could not be deleted."
                            );
                        }

                        break;


                    // ==================================================
                    // 0. EXIT
                    // ==================================================
                    case 0:

                        System.out.println(
                                "\nExiting Customer Console..."
                        );


                        scanner.close();

                        return;


                    default:

                        System.out.println(
                                "Invalid menu choice."
                        );

                        System.out.println(
                                "Please select a number from 0 to 9."
                        );
                }


            } catch (Exception e) {

                System.out.println(
                        "\n=========================================="
                );

                System.out.println(
                        "OPERATION FAILED"
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
                "             CUSTOMER CONSOLE"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Find Customer By ID"
        );

        System.out.println(
                "2. Find Customer By Email"
        );

        System.out.println(
                "3. Find Customer By Mobile"
        );

        System.out.println(
                "4. Find Customer By Username"
        );

        System.out.println(
                "5. Find All Customers"
        );

        System.out.println(
                "6. Register Customer"
        );

        System.out.println(
                "7. Update Customer"
        );

        System.out.println(
                "8. Delete Customer"
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
                 * EXPECTED OUTPUT:
                 * Invalid input. Please enter a number.
                 */

                System.out.println(
                        "Invalid input. Please enter a number."
                );
            }
        }
    }



    // ==========================================================
    // SAFE DATE INPUT
    // ==========================================================

    private static LocalDate readDate(
            String message) {

        while (true) {

            System.out.print(
                    message
            );


            String input =
                    scanner.nextLine().trim();


            try {

                String[] parts =
                        input.split("-");


                if (parts.length != 3) {

                    throw new IllegalArgumentException(
                            "Invalid date format."
                    );
                }


                int day =
                        Integer.parseInt(
                                parts[0]
                        );


                int month =
                        Integer.parseInt(
                                parts[1]
                        );


                int year =
                        Integer.parseInt(
                                parts[2]
                        );


                return LocalDate.of(
                        year,
                        month,
                        day
                );


            } catch (Exception e) {

                /*
                 * WRONG INPUT EXAMPLES:
                 *
                 * 32-15-2020
                 * 2020/01/01
                 * abc
                 *
                 * EXPECTED OUTPUT:
                 * Invalid date.
                 */

                System.out.println(
                        "Invalid date."
                );

                System.out.println(
                        "Use format: dd-MM-yyyy"
                );
            }
        }
    }



    // ==========================================================
    // PRINT CUSTOMER
    // ==========================================================

    private static void printCustomer(
            Customer customer) {

        System.out.println(
                "\n========== CUSTOMER DETAILS =========="
        );


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
                "Date of Birth: " +
                        customer.getDateOfBirth()
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
                "Address: " +
                        customer.getAddress()
        );


        System.out.println(
                "City: " +
                        customer.getCity()
        );


        System.out.println(
                "Country: " +
                        customer.getCountry()
        );


        System.out.println(
                "Username: " +
                        customer.getUsername()
        );


        System.out.println(
                "Account Status: " +
                        customer.getAccountStatus()
        );


        System.out.println(
                "Registration Date: " +
                        customer.getRegistrationDate()
        );


        System.out.println(
                "======================================"
        );
    }
}