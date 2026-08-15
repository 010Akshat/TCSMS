package com.amdocs.telecom.main.Console;

import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.enums.BillStatus;
import com.amdocs.telecom.service.BillingService;
import com.amdocs.telecom.service.impl.BillingServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class BillingConsole {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final BillingService billingService =
            new BillingServiceImpl();


    public static void main(String[] args) {

        while (true) {

            printMenu();

            int choice =
                    readInt(
                            "Enter your choice: "
                    );

            try {

                switch (choice) {

                    // ==================================================
                    // 1. GENERATE BILL
                    // ==================================================
                    case 1:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Subscription ID:
                         * 1
                         *
                         * Billing Date:
                         * 01-08-2026
                         *
                         * Tax Rate:
                         * 18
                         *
                         * Discount:
                         * 50
                         *
                         *
                         * WRONG INPUT EXAMPLES:
                         *
                         * Subscription ID:
                         * 999999
                         *
                         * Expected:
                         * Subscription not found.
                         *
                         *
                         * Tax Rate:
                         * -10
                         *
                         * Expected:
                         * Tax rate cannot be negative.
                         *
                         *
                         * Discount:
                         * -50
                         *
                         * Expected:
                         * Discount cannot be negative.
                         *
                         *
                         * If bill already exists for that
                         * subscription/month:
                         *
                         * Bill already exists for this
                         * subscription and billing month.
                         */

                        long subscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        LocalDate billingMonth =
                                readDateFirstDay(
                                        "Enter billing month (dd-MM-yyyy): "
                                );


                        double taxRate =
                                readDouble(
                                        "Enter tax rate (%): "
                                );


                        double discount =
                                readDouble(
                                        "Enter discount: "
                                );


                        Bill generatedBill =
                                billingService.generateBill(
                                        subscriptionId,
                                        billingMonth,
                                        taxRate,
                                        discount
                                );


                        System.out.println(
                                "\nBill generated successfully."
                        );


                        printBill(
                                generatedBill
                        );

                        break;


                    // ==================================================
                    // 2. FIND BILL BY ID
                    // ==================================================
                    case 2:

                        /*
                         * VALID:
                         * 1
                         *
                         * WRONG:
                         * 999999
                         *
                         * Expected:
                         * Bill not found.
                         */

                        long billId =
                                readLong(
                                        "Enter bill ID: "
                                );


                        Bill billById =
                                billingService.findById(
                                        billId
                                );


                        if (billById != null) {

                            printBill(
                                    billById
                            );

                        } else {

                            System.out.println(
                                    "Bill not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 3. FIND BILL BY NUMBER
                    // ==================================================
                    case 3:

                        /*
                         * VALID EXAMPLE:
                         * INV-2026-08-10001
                         *
                         * WRONG EXAMPLE:
                         * INV-999999
                         *
                         * Expected:
                         * Bill not found.
                         */

                        System.out.print(
                                "Enter bill number: "
                        );

                        String billNumber =
                                scanner.nextLine().trim();


                        Bill billByNumber =
                                billingService.findByBillNumber(
                                        billNumber
                                );


                        if (billByNumber != null) {

                            printBill(
                                    billByNumber
                            );

                        } else {

                            System.out.println(
                                    "Bill not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 4. FIND BILL BY SUBSCRIPTION + MONTH
                    // ==================================================
                    case 4:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Subscription ID:
                         * 1
                         *
                         * Billing Month:
                         * 01-08-2026
                         *
                         *
                         * WRONG:
                         *
                         * Subscription ID:
                         * 999999
                         *
                         * or a month for which no bill exists.
                         */

                        long searchSubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        LocalDate searchBillingMonth =
                                readDateFirstDay(
                                        "Enter billing month (dd-MM-yyyy): "
                                );


                        Bill billBySubscriptionMonth =
                                billingService
                                        .findBySubscriptionAndMonth(
                                                searchSubscriptionId,
                                                searchBillingMonth
                                        );


                        if (billBySubscriptionMonth != null) {

                            printBill(
                                    billBySubscriptionMonth
                            );

                        } else {

                            System.out.println(
                                    "Bill not found for this subscription and month."
                            );
                        }

                        break;


                    // ==================================================
                    // 5. FIND BILLS BY SUBSCRIPTION
                    // ==================================================
                    case 5:

                        /*
                         * VALID:
                         * 1
                         *
                         * WRONG:
                         * 999999
                         *
                         * Expected:
                         * Empty result if no bills exist.
                         */

                        long customerSubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        List<Bill> subscriptionBills =
                                billingService.findBySubscriptionId(
                                        customerSubscriptionId
                                );


                        if (subscriptionBills != null &&
                                !subscriptionBills.isEmpty()) {

                            System.out.println(
                                    "\nBills found: " +
                                            subscriptionBills.size()
                            );


                            for (Bill bill :
                                    subscriptionBills) {

                                printBill(
                                        bill
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No bills found for this subscription."
                            );
                        }

                        break;


                    // ==================================================
                    // 6. FIND ALL BILLS
                    // ==================================================
                    case 6:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 6
                         *
                         * Expected:
                         * All bills displayed.
                         */

                        List<Bill> allBills =
                                billingService.findAll();


                        if (allBills != null &&
                                !allBills.isEmpty()) {

                            System.out.println(
                                    "\nTotal bills: " +
                                            allBills.size()
                            );


                            for (Bill bill :
                                    allBills) {

                                printBill(
                                        bill
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No bills found."
                            );
                        }

                        break;


                    // ==================================================
                    // 7. UPDATE BILL
                    // ==================================================
                    case 7:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Bill ID:
                         * 1
                         *
                         * New Status:
                         * OVERDUE
                         *
                         *
                         * WRONG:
                         *
                         * Bill ID:
                         * 999999
                         *
                         * Expected:
                         * Bill not found.
                         *
                         *
                         * Valid BillStatus values depend on
                         * your BillStatus enum.
                         */

                        long updateBillId =
                                readLong(
                                        "Enter bill ID: "
                                );


                        Bill billToUpdate =
                                billingService.findById(
                                        updateBillId
                                );


                        if (billToUpdate == null) {

                            System.out.println(
                                    "Bill not found."
                            );

                            break;
                        }


                        System.out.println(
                                "\nCurrent bill:"
                        );


                        printBill(
                                billToUpdate
                        );


                        System.out.print(
                                "Enter new bill status: "
                        );


                        BillStatus newStatus =
                                readBillStatus();


                        billToUpdate.setBillStatus(
                                newStatus
                        );


                        billingService.update(
                                billToUpdate
                        );


                        System.out.println(
                                "\nBill updated successfully."
                        );


                        printBill(
                                billingService.findById(
                                        updateBillId
                                )
                        );

                        break;


                    // ==================================================
                    // 8. DELETE BILL
                    // ==================================================
                    case 8:

                        /*
                         * VALID:
                         * Use a test bill ID.
                         *
                         * Example:
                         * 15
                         *
                         *
                         * WRONG:
                         * 999999
                         *
                         * Expected:
                         * No matching bill / DAO behavior.
                         *
                         *
                         * IMPORTANT:
                         * During interview use a test bill,
                         * not an important existing bill.
                         */

                        long deleteBillId =
                                readLong(
                                        "Enter bill ID to delete: "
                                );


                        Bill billToDelete =
                                billingService.findById(
                                        deleteBillId
                                );


                        if (billToDelete == null) {

                            System.out.println(
                                    "Bill not found."
                            );

                            break;
                        }


                        printBill(
                                billToDelete
                        );


                        System.out.print(
                                "Confirm deletion (YES/NO): "
                        );


                        String confirmation =
                                scanner.nextLine().trim();


                        if (!"YES".equalsIgnoreCase(
                                confirmation
                        )) {

                            System.out.println(
                                    "Deletion cancelled."
                            );

                            break;
                        }


                        billingService.delete(
                                deleteBillId
                        );


                        Bill deleted =
                                billingService.findById(
                                        deleteBillId
                                );


                        if (deleted == null) {

                            System.out.println(
                                    "Bill deleted successfully."
                            );

                        } else {

                            System.out.println(
                                    "Bill could not be deleted."
                            );
                        }

                        break;


                    // ==================================================
                    // 0. EXIT
                    // ==================================================
                    case 0:

                        System.out.println(
                                "Exiting Billing Console..."
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
                        "BILLING OPERATION FAILED"
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
                "             BILLING CONSOLE"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Generate Bill"
        );

        System.out.println(
                "2. Find Bill By ID"
        );

        System.out.println(
                "3. Find Bill By Number"
        );

        System.out.println(
                "4. Find Bill By Subscription + Month"
        );

        System.out.println(
                "5. Find Bills By Subscription"
        );

        System.out.println(
                "6. Find All Bills"
        );

        System.out.println(
                "7. Update Bill"
        );

        System.out.println(
                "8. Delete Bill"
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
                 * WRONG:
                 * abc
                 *
                 * Expected:
                 * Invalid integer.
                 */

                System.out.println(
                        "Invalid input. Please enter a valid integer."
                );
            }
        }
    }


    // ==========================================================
    // SAFE LONG INPUT
    // ==========================================================

    private static long readLong(
            String message) {

        while (true) {

            System.out.print(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            try {

                return Long.parseLong(
                        input
                );

            } catch (NumberFormatException e) {

                /*
                 * WRONG:
                 * abc
                 *
                 * Expected:
                 * Invalid number.
                 */

                System.out.println(
                        "Invalid input. Please enter a valid number."
                );
            }
        }
    }


    // ==========================================================
    // SAFE DOUBLE INPUT
    // ==========================================================

    private static double readDouble(
            String message) {

        while (true) {

            System.out.print(
                    message
            );

            String input =
                    scanner.nextLine().trim();

            try {

                return Double.parseDouble(
                        input
                );

            } catch (NumberFormatException e) {

                /*
                 * WRONG:
                 * abc
                 *
                 * Expected:
                 * Invalid decimal value.
                 */

                System.out.println(
                        "Invalid input. Please enter a valid decimal value."
                );
            }
        }
    }


    // ==========================================================
    // DATE INPUT
    // ==========================================================

    private static LocalDate readDateFirstDay(
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


                LocalDate date =
                        LocalDate.of(
                                year,
                                month,
                                day
                        );


                return date.withDayOfMonth(
                        1
                );


            } catch (Exception e) {

                /*
                 * VALID:
                 * 01-08-2026
                 *
                 * WRONG:
                 * 32-15-2026
                 * abc
                 *
                 * Expected:
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
    // BILL STATUS
    // ==========================================================

    private static BillStatus readBillStatus() {

        while (true) {

            String input =
                    scanner.nextLine().trim();

            try {

                return BillStatus.valueOf(
                        input.toUpperCase()
                );

            } catch (IllegalArgumentException e) {

                /*
                 * WRONG:
                 * INVALID
                 *
                 * Expected:
                 * Invalid bill status.
                 *
                 * Valid values are the values present
                 * in your BillStatus enum.
                 */

                System.out.println(
                        "Invalid bill status."
                );

                System.out.print(
                        "Enter bill status again: "
                );
            }
        }
    }


    // ==========================================================
    // PRINT BILL
    // ==========================================================

    private static void printBill(
            Bill bill) {

        System.out.println(
                "\n========== BILL DETAILS =========="
        );

        System.out.println(
                "Bill ID: " +
                        bill.getBillId()
        );

        System.out.println(
                "Bill Number: " +
                        bill.getBillNumber()
        );

        System.out.println(
                "Subscription ID: " +
                        bill.getSubscriptionId()
        );

        System.out.println(
                "Billing Month: " +
                        bill.getBillingMonth()
        );

        System.out.println(
                "Plan Rental: ₹" +
                        bill.getPlanRental()
        );

        System.out.println(
                "Usage Charges: ₹" +
                        bill.getUsageCharges()
        );

        System.out.println(
                "Tax Amount: ₹" +
                        bill.getTaxAmount()
        );

        System.out.println(
                "Discount: ₹" +
                        bill.getDiscount()
        );

        System.out.println(
                "Total Amount: ₹" +
                        bill.getTotalAmount()
        );

        System.out.println(
                "Due Date: " +
                        bill.getDueDate()
        );

        System.out.println(
                "Bill Status: " +
                        bill.getBillStatus()
        );

        System.out.println(
                "Created At: " +
                        bill.getCreatedAt()
        );

        System.out.println(
                "Updated At: " +
                        bill.getUpdatedAt()
        );

        System.out.println(
                "================================="
        );
    }
}