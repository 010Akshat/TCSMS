package com.amdocs.telecom.main.Console;

import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.enums.UsageType;
import com.amdocs.telecom.service.SubscriptionService;
import com.amdocs.telecom.service.UsageService;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;
import com.amdocs.telecom.service.impl.UsageServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UsageConsole {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final UsageService usageService =
            new UsageServiceImpl();

    private static final SubscriptionService subscriptionService =
            new SubscriptionServiceImpl();


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
                    // 1. CREATE USAGE RECORD
                    // ==================================================
                    case 1:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Subscription ID:
                         * 1
                         *
                         * Usage Type:
                         * DATA
                         *
                         * Quantity:
                         * 2.500
                         *
                         * Unit:
                         * GB
                         *
                         * Charge:
                         * 50.00
                         *
                         *
                         * WRONG INPUT EXAMPLES:
                         *
                         * Subscription ID:
                         * 999999
                         *
                         * Usage Type:
                         * INVALID
                         *
                         * Quantity:
                         * abc
                         *
                         * Charge:
                         * xyz
                         *
                         * The service/DAO layer will handle
                         * the corresponding error.
                         */

                        long subscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        // Check subscription before creating usage
                        MobileSubscription subscription =
                                subscriptionService.findById(
                                        subscriptionId
                                );


                        if (subscription == null) {

                            System.out.println(
                                    "Subscription not found."
                            );

                            break;
                        }


                        System.out.print(
                                "Enter usage type " +
                                        "(DATA/VOICE/SMS/ROAMING): "
                        );

                        UsageType usageType =
                                readUsageType();


                        BigDecimal quantity =
                                readBigDecimal(
                                        "Enter quantity: "
                                );


                        System.out.print(
                                "Enter unit: "
                        );

                        String unit =
                                scanner.nextLine().trim();


                        BigDecimal charge =
                                readBigDecimal(
                                        "Enter charge: "
                                );


                        UsageRecord usageRecord =
                                new UsageRecord(
                                        0,
                                        subscriptionId,
                                        LocalDateTime.now(),
                                        usageType,
                                        quantity,
                                        unit,
                                        charge
                                );


                        usageService.save(
                                usageRecord
                        );


                        System.out.println(
                                "\nUsage record created successfully."
                        );


                        printUsage(
                                usageRecord
                        );

                        break;


                    // ==================================================
                    // 2. FIND USAGE BY ID
                    // ==================================================
                    case 2:

                        /*
                         * VALID:
                         * 1
                         *
                         * WRONG:
                         * 999999
                         * abc
                         *
                         * Expected:
                         * Usage not found / invalid number.
                         */

                        long usageId =
                                readLong(
                                        "Enter usage ID: "
                                );


                        UsageRecord foundUsage =
                                usageService.findById(
                                        usageId
                                );


                        if (foundUsage != null) {

                            printUsage(
                                    foundUsage
                            );

                        } else {

                            System.out.println(
                                    "Usage record not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 3. FIND USAGE BY SUBSCRIPTION
                    // ==================================================
                    case 3:

                        /*
                         * VALID:
                         * 1
                         *
                         * WRONG:
                         * 999999
                         *
                         * Expected:
                         * No usage records found.
                         */

                        long searchSubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        List<UsageRecord> subscriptionUsage =
                                usageService.findBySubscriptionId(
                                        searchSubscriptionId
                                );


                        if (subscriptionUsage != null &&
                                !subscriptionUsage.isEmpty()) {

                            System.out.println(
                                    "\nUsage records found: " +
                                            subscriptionUsage.size()
                            );


                            for (UsageRecord record :
                                    subscriptionUsage) {

                                printUsage(
                                        record
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No usage records found for this subscription."
                            );
                        }

                        break;


                    // ==================================================
                    // 4. FIND ALL USAGE
                    // ==================================================
                    case 4:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 4
                         *
                         * Expected:
                         * All usage records displayed.
                         */

                        List<UsageRecord> allUsage =
                                usageService.findAll();


                        if (allUsage != null &&
                                !allUsage.isEmpty()) {

                            System.out.println(
                                    "\nTotal usage records: " +
                                            allUsage.size()
                            );


                            for (UsageRecord record :
                                    allUsage) {

                                printUsage(
                                        record
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No usage records found."
                            );
                        }

                        break;


                    // ==================================================
                    // 5. UPDATE USAGE
                    // ==================================================
                    case 5:

                        /*
                         * VALID EXAMPLE:
                         *
                         * Usage ID:
                         * 1
                         *
                         * New Quantity:
                         * 3.500
                         *
                         * New Unit:
                         * GB
                         *
                         * New Charge:
                         * 70
                         *
                         *
                         * WRONG:
                         *
                         * Usage ID:
                         * 999999
                         *
                         * Quantity:
                         * abc
                         *
                         * Charge:
                         * xyz
                         */

                        long updateUsageId =
                                readLong(
                                        "Enter usage ID: "
                                );


                        UsageRecord usageToUpdate =
                                usageService.findById(
                                        updateUsageId
                                );


                        if (usageToUpdate == null) {

                            System.out.println(
                                    "Usage record not found."
                            );

                            break;
                        }


                        System.out.println(
                                "\nCurrent usage:"
                        );


                        printUsage(
                                usageToUpdate
                        );


                        BigDecimal newQuantity =
                                readBigDecimal(
                                        "Enter new quantity: "
                                );


                        System.out.print(
                                "Enter new unit: "
                        );

                        String newUnit =
                                scanner.nextLine().trim();


                        BigDecimal newCharge =
                                readBigDecimal(
                                        "Enter new charge: "
                                );


                        usageToUpdate.setQuantity(
                                newQuantity
                        );

                        usageToUpdate.setUnit(
                                newUnit
                        );

                        usageToUpdate.setCharge(
                                newCharge
                        );


                        usageService.update(
                                usageToUpdate
                        );


                        System.out.println(
                                "\nUsage updated successfully."
                        );


                        printUsage(
                                usageService.findById(
                                        updateUsageId
                                )
                        );

                        break;


                    // ==================================================
                    // 6. DELETE USAGE
                    // ==================================================
                    case 6:

                        /*
                         * VALID:
                         * 1
                         *
                         * WRONG:
                         * 999999
                         *
                         * IMPORTANT:
                         * During interview use a test usage ID,
                         * not an important production/testing record.
                         */

                        long deleteUsageId =
                                readLong(
                                        "Enter usage ID to delete: "
                                );


                        UsageRecord usageToDelete =
                                usageService.findById(
                                        deleteUsageId
                                );


                        if (usageToDelete == null) {

                            System.out.println(
                                    "Usage record not found."
                            );

                            break;
                        }


                        printUsage(
                                usageToDelete
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


                        usageService.delete(
                                deleteUsageId
                        );


                        UsageRecord deleted =
                                usageService.findById(
                                        deleteUsageId
                                );


                        if (deleted == null) {

                            System.out.println(
                                    "Usage record deleted successfully."
                            );

                        } else {

                            System.out.println(
                                    "Usage record could not be deleted."
                            );
                        }

                        break;


                    // ==================================================
                    // 7. TOTAL DATA USAGE
                    // ==================================================
                    case 7:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 7
                         *
                         * Expected:
                         * Total DATA usage displayed.
                         */

                        BigDecimal totalData =
                                usageService
                                        .calculateTotalDataUsage();


                        System.out.println(
                                "Total DATA usage: " +
                                        totalData
                        );

                        break;


                    // ==================================================
                    // 8. TOTAL VOICE USAGE
                    // ==================================================
                    case 8:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 8
                         */

                        BigDecimal totalVoice =
                                usageService
                                        .calculateTotalVoiceUsage();


                        System.out.println(
                                "Total VOICE usage: " +
                                        totalVoice
                        );

                        break;


                    // ==================================================
                    // 9. TOTAL SMS USAGE
                    // ==================================================
                    case 9:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 9
                         */

                        BigDecimal totalSms =
                                usageService
                                        .calculateTotalSmsUsage();


                        System.out.println(
                                "Total SMS usage: " +
                                        totalSms
                        );

                        break;


                    // ==================================================
                    // 10. MONTHLY USAGE
                    // ==================================================
                    case 10:

                        /*
                         * VALID EXAMPLE:
                         * 8
                         * 2026
                         *
                         * This means:
                         * August 2026
                         *
                         *
                         * WRONG:
                         * 13
                         *
                         * Expected:
                         * Invalid month.
                         */

                        int month =
                                readInt(
                                        "Enter month (1-12): "
                                );


                        int year =
                                readInt(
                                        "Enter year: "
                                );


                        if (month < 1 ||
                                month > 12) {

                            throw new IllegalArgumentException(
                                    "Month must be between 1 and 12."
                            );
                        }


                        YearMonth yearMonth =
                                YearMonth.of(
                                        year,
                                        month
                                );


                        Map<UsageType, BigDecimal> monthlyUsage =
                                usageService.calculateMonthlyUsage(
                                        yearMonth
                                );


                        System.out.println(
                                "\nUsage for " +
                                        yearMonth +
                                        ":"
                        );


                        printUsageMap(
                                monthlyUsage
                        );

                        break;


                    // ==================================================
                    // 11. USAGE BY TYPE
                    // ==================================================
                    case 11:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 11
                         *
                         * Expected:
                         * DATA -> total
                         * VOICE -> total
                         * SMS -> total
                         * ROAMING -> total (if present)
                         */

                        Map<UsageType, BigDecimal> usageByType =
                                usageService.calculateUsageByType();


                        System.out.println(
                                "\n========== USAGE BY TYPE =========="
                        );


                        printUsageMap(
                                usageByType
                        );

                        break;


                    // ==================================================
                    // 0. EXIT
                    // ==================================================
                    case 0:

                        System.out.println(
                                "Exiting Usage Console..."
                        );

                        scanner.close();

                        return;


                    default:

                        System.out.println(
                                "Invalid menu choice."
                        );

                        System.out.println(
                                "Choose a number from 0 to 11."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "\n=========================================="
                );

                System.out.println(
                        "USAGE OPERATION FAILED"
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
                "             USAGE CONSOLE"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Create Usage Record"
        );

        System.out.println(
                "2. Find Usage By ID"
        );

        System.out.println(
                "3. Find Usage By Subscription"
        );

        System.out.println(
                "4. Find All Usage"
        );

        System.out.println(
                "5. Update Usage"
        );

        System.out.println(
                "6. Delete Usage"
        );

        System.out.println(
                "7. Calculate Total DATA Usage"
        );

        System.out.println(
                "8. Calculate Total VOICE Usage"
        );

        System.out.println(
                "9. Calculate Total SMS Usage"
        );

        System.out.println(
                "10. Calculate Monthly Usage"
        );

        System.out.println(
                "11. Calculate Usage By Type"
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
                 * WRONG INPUT:
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
                 * WRONG INPUT:
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
    // SAFE BIGDECIMAL INPUT
    // ==========================================================

    private static BigDecimal readBigDecimal(
            String message) {

        while (true) {

            System.out.print(
                    message
            );


            String input =
                    scanner.nextLine().trim();


            try {

                return new BigDecimal(
                        input
                );

            } catch (NumberFormatException e) {

                /*
                 * WRONG INPUT:
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
    // USAGE TYPE
    // ==========================================================

    private static UsageType readUsageType() {

        while (true) {

            String input =
                    scanner.nextLine().trim();


            try {

                return UsageType.valueOf(
                        input.toUpperCase()
                );

            } catch (IllegalArgumentException e) {

                /*
                 * VALID:
                 * DATA
                 * VOICE
                 * SMS
                 * ROAMING
                 *
                 * WRONG:
                 * INVALID
                 *
                 * Expected:
                 * Invalid usage type.
                 */

                System.out.println(
                        "Invalid usage type."
                );

                System.out.println(
                        "Enter DATA, VOICE, SMS or ROAMING."
                );

                System.out.print(
                        "Enter usage type again: "
                );
            }
        }
    }


    // ==========================================================
    // PRINT USAGE
    // ==========================================================

    private static void printUsage(
            UsageRecord usage) {

        System.out.println(
                "\n========== USAGE DETAILS =========="
        );

        System.out.println(
                "Usage ID: " +
                        usage.getUsageId()
        );

        System.out.println(
                "Subscription ID: " +
                        usage.getSubscriptionId()
        );

        System.out.println(
                "Usage Date: " +
                        usage.getUsageDate()
        );

        System.out.println(
                "Usage Type: " +
                        usage.getUsageType()
        );

        System.out.println(
                "Quantity: " +
                        usage.getQuantity()
        );

        System.out.println(
                "Unit: " +
                        usage.getUnit()
        );

        System.out.println(
                "Charge: ₹" +
                        usage.getCharge()
        );

        System.out.println(
                "==================================="
        );
    }


    // ==========================================================
    // PRINT USAGE MAP
    // ==========================================================

    private static void printUsageMap(
            Map<UsageType, BigDecimal> usageMap) {

        if (usageMap == null ||
                usageMap.isEmpty()) {

            System.out.println(
                    "No usage data found."
            );

            return;
        }


        for (Map.Entry<UsageType, BigDecimal> entry :
                usageMap.entrySet()) {

            System.out.println(
                    entry.getKey() +
                            " : " +
                            entry.getValue()
            );
        }
    }
}