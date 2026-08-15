package com.amdocs.telecom.main.Console;

import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.enums.PlanType;
import com.amdocs.telecom.service.PlanService;
import com.amdocs.telecom.service.impl.PlanServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlanConsole {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final PlanService planService =
            new PlanServiceImpl();


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
                    // 1. FIND PLAN BY ID
                    // ==================================================
                    case 1:

                        /*
                         * VALID INPUT:
                         * 1
                         *
                         * WRONG INPUT:
                         * 999999
                         * OR:
                         * abc
                         *
                         * Expected:
                         * Invalid input / plan not found.
                         */

                        long planId =
                                readLong(
                                        "Enter plan ID: "
                                );


                        TelecomPlan plan =
                                planService.findById(
                                        planId
                                );


                        if (plan != null) {

                            printPlan(
                                    plan
                            );

                        } else {

                            System.out.println(
                                    "Plan not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 2. FIND PLAN BY CODE
                    // ==================================================
                    case 2:

                        /*
                         * VALID INPUT:
                         * PLAN-105
                         *
                         * WRONG INPUT:
                         * PLAN-999
                         *
                         * Expected:
                         * Plan not found.
                         */

                        System.out.print(
                                "Enter plan code: "
                        );

                        String planCode =
                                scanner.nextLine().trim();


                        TelecomPlan planByCode =
                                planService.findByCode(
                                        planCode
                                );


                        if (planByCode != null) {

                            printPlan(
                                    planByCode
                            );

                        } else {

                            System.out.println(
                                    "Plan not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 3. FIND ALL PLANS
                    // ==================================================
                    case 3:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Just select:
                         * 3
                         *
                         * Expected:
                         * All plans displayed.
                         */

                        List<TelecomPlan> allPlans =
                                planService.findAll();


                        if (allPlans != null &&
                                !allPlans.isEmpty()) {

                            System.out.println(
                                    "\nTotal plans: " +
                                            allPlans.size()
                            );


                            for (TelecomPlan p :
                                    allPlans) {

                                printPlan(p);

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No plans found."
                            );
                        }

                        break;


                    // ==================================================
                    // 4. FIND ACTIVE PLANS
                    // ==================================================
                    case 4:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Just select:
                         * 4
                         *
                         * Expected:
                         * Only ACTIVE plans are displayed.
                         */

                        List<TelecomPlan> activePlans =
                                planService.findActivePlans();


                        if (activePlans != null &&
                                !activePlans.isEmpty()) {

                            System.out.println(
                                    "\nActive plans: " +
                                            activePlans.size()
                            );


                            for (TelecomPlan p :
                                    activePlans) {

                                printPlan(p);

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No active plans found."
                            );
                        }

                        break;


                    // ==================================================
                    // 5. FILTER BY PRICE
                    // ==================================================
                    case 5:

                        /*
                         * VALID INPUT:
                         * 700
                         *
                         * WRONG INPUT:
                         * -100
                         *
                         * Also try:
                         * abc
                         *
                         * Expected:
                         * All plans with monthly rental <= limit.
                         */

                        BigDecimal maxPrice =
                                readBigDecimal(
                                        "Enter maximum monthly rental: "
                                );


                        List<TelecomPlan> priceFiltered =
                                planService.filterByPrice(
                                        maxPrice
                                );


                        if (priceFiltered != null &&
                                !priceFiltered.isEmpty()) {

                            System.out.println(
                                    "\nPlans within price limit:"
                            );


                            for (TelecomPlan p :
                                    priceFiltered) {

                                printPlan(p);

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No plans found within this price."
                            );
                        }

                        break;


                    // ==================================================
                    // 6. FILTER BY DATA ALLOWANCE
                    // ==================================================
                    case 6:

                        /*
                         * VALID INPUT:
                         * 50
                         *
                         * WRONG INPUT:
                         * -10
                         *
                         * Also try:
                         * abc
                         *
                         * Expected:
                         * Plans with data allowance >= given value.
                         */

                        BigDecimal minimumData =
                                readBigDecimal(
                                        "Enter minimum data allowance (GB): "
                                );


                        List<TelecomPlan> dataFiltered =
                                planService
                                        .filterByDataAllowance(
                                                minimumData
                                        );


                        if (dataFiltered != null &&
                                !dataFiltered.isEmpty()) {

                            System.out.println(
                                    "\nPlans matching data requirement:"
                            );


                            for (TelecomPlan p :
                                    dataFiltered) {

                                printPlan(p);

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No plans found."
                            );
                        }

                        break;


                    // ==================================================
                    // 7. SORT BY PRICE
                    // ==================================================
                    case 7:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Just select:
                         * 7
                         *
                         * Expected:
                         * Plans displayed from lowest to highest
                         * monthly rental.
                         */

                        List<TelecomPlan> sortedPlans =
                                planService.sortByPrice();


                        System.out.println(
                                "\nPlans sorted by monthly rental:"
                        );


                        for (TelecomPlan p :
                                sortedPlans) {

                            printPlan(p);

                            System.out.println(
                                    "------------------------------------"
                            );
                        }

                        break;


                    // ==================================================
                    // 8. COMPARE PLANS
                    // ==================================================
                    case 8:

                        /*
                         * VALID INPUT:
                         *
                         * Number of plans:
                         * 2
                         *
                         * Plan ID 1:
                         * 1
                         *
                         * Plan ID 2:
                         * 2
                         *
                         *
                         * WRONG INPUT:
                         *
                         * Number of plans:
                         * 0
                         *
                         * Expected:
                         * Plan IDs cannot be empty.
                         *
                         *
                         * Another wrong input:
                         * 999999
                         *
                         * Expected:
                         * Only existing plans will be returned.
                         */

                        int numberOfPlans =
                                readInt(
                                        "How many plans do you want to compare? "
                                );


                        if (numberOfPlans <= 0) {

                            throw new IllegalArgumentException(
                                    "Number of plans must be greater than zero."
                            );
                        }


                        List<Long> planIds =
                                new ArrayList<>();


                        for (int i = 1;
                             i <= numberOfPlans;
                             i++) {

                            long comparePlanId =
                                    readLong(
                                            "Enter plan ID " +
                                                    i +
                                                    ": "
                                    );


                            planIds.add(
                                    comparePlanId
                            );
                        }


                        List<TelecomPlan> comparedPlans =
                                planService.comparePlans(
                                        planIds
                                );


                        if (comparedPlans != null &&
                                !comparedPlans.isEmpty()) {

                            System.out.println(
                                    "\nCompared Plans:"
                            );


                            for (TelecomPlan p :
                                    comparedPlans) {

                                printPlan(p);

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No matching plans found."
                            );
                        }

                        break;


                    // ==================================================
                    // 9. CREATE PLAN
                    // ==================================================
                    case 9:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * PLAN-TEST-01
                         * Premium Test Plan
                         * POSTPAID
                         * 499
                         * 50
                         * 1000
                         * 100
                         * 30
                         * true
                         * ACTIVE
                         *
                         *
                         * WRONG INPUT EXAMPLES:
                         *
                         * Monthly rental:
                         * -500
                         *
                         * Data:
                         * -10
                         *
                         * Voice minutes:
                         * abc
                         *
                         * Plan type:
                         * INVALID
                         *
                         * Expected:
                         * Service/DAO validation or database
                         * error is shown.
                         */

                        String newPlanCode =
                                generatePlanCode();

                        System.out.println(
                                "Generated Plan Code: " +
                                        newPlanCode
                        );


                        System.out.print(
                                "Enter plan name: "
                        );

                        String newPlanName =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter plan type (PREPAID/POSTPAID): "
                        );

                        PlanType newPlanType =
                                readPlanType();


                        BigDecimal monthlyRental =
                                readBigDecimal(
                                        "Enter monthly rental: "
                                );


                        BigDecimal dataAllowance =
                                readBigDecimal(
                                        "Enter data allowance (GB): "
                                );


                        int voiceMinutes =
                                readInt(
                                        "Enter voice minutes: "
                                );


                        int smsAllowance =
                                readInt(
                                        "Enter SMS allowance: "
                                );


                        int validityDays =
                                readInt(
                                        "Enter validity days: "
                                );


                        boolean roaming =
                                readBoolean(
                                        "International roaming (true/false): "
                                );


                        AccountStatus status =
                                readAccountStatus();


                        TelecomPlan newPlan =
                                new TelecomPlan(
                                        0,
                                        newPlanCode,
                                        newPlanName,
                                        newPlanType,
                                        monthlyRental,
                                        dataAllowance,
                                        voiceMinutes,
                                        smsAllowance,
                                        validityDays,
                                        roaming,
                                        status,
                                        null,
                                        null
                                );


                        planService.save(
                                newPlan
                        );


                        System.out.println(
                                "\nPlan saved successfully."
                        );


                        TelecomPlan savedPlan =
                                planService.findByCode(
                                        newPlanCode
                                );


                        if (savedPlan != null) {

                            printPlan(
                                    savedPlan
                            );
                        }

                        break;


                    // ==================================================
                    // 10. UPDATE PLAN
                    // ==================================================
                    case 10:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Plan ID:
                         * 1
                         *
                         * Plan Code:
                         * PLAN-105
                         *
                         * Plan Name:
                         * Updated Premium Plan
                         *
                         * Plan Type:
                         * POSTPAID
                         *
                         * Monthly Rental:
                         * 599
                         *
                         * Data:
                         * 75
                         *
                         * Voice:
                         * 1500
                         *
                         * SMS:
                         * 150
                         *
                         * Validity:
                         * 30
                         *
                         * Roaming:
                         * true
                         *
                         * Status:
                         * ACTIVE
                         *
                         *
                         * WRONG INPUT:
                         *
                         * Plan ID:
                         * 999999
                         *
                         * Expected:
                         * Database/service validation or
                         * no matching record.
                         */

                        long updatePlanId =
                                readLong(
                                        "Enter plan ID: "
                                );


                        TelecomPlan planToUpdate =
                                planService.findById(
                                        updatePlanId
                                );


                        if (planToUpdate == null) {

                            System.out.println(
                                    "Plan not found."
                            );

                            break;
                        }


                        System.out.println(
                                "\nCurrent plan:"
                        );


                        printPlan(
                                planToUpdate
                        );


//                        System.out.print(
//                                "Enter new plan code: "
//                        );
//
//                        String updatedPlanCode =
//                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter new plan name: "
                        );

                        String updatedPlanName =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter new plan type (PREPAID/POSTPAID): "
                        );

                        PlanType updatedPlanType =
                                readPlanType();


                        BigDecimal updatedRental =
                                readBigDecimal(
                                        "Enter new monthly rental: "
                                );


                        BigDecimal updatedData =
                                readBigDecimal(
                                        "Enter new data allowance (GB): "
                                );


                        int updatedVoice =
                                readInt(
                                        "Enter new voice minutes: "
                                );


                        int updatedSms =
                                readInt(
                                        "Enter new SMS allowance: "
                                );


                        int updatedValidity =
                                readInt(
                                        "Enter new validity days: "
                                );


                        boolean updatedRoaming =
                                readBoolean(
                                        "International roaming (true/false): "
                                );


                        AccountStatus updatedStatus =
                                readAccountStatus();


//                        planToUpdate.setPlanCode(
//                                updatedPlanCode
//                        );

                        planToUpdate.setPlanName(
                                updatedPlanName
                        );

                        planToUpdate.setPlanType(
                                updatedPlanType
                        );

                        planToUpdate.setMonthlyRental(
                                updatedRental
                        );

                        planToUpdate.setDataAllowanceGB(
                                updatedData
                        );

                        planToUpdate.setVoiceMinutes(
                                updatedVoice
                        );

                        planToUpdate.setSmsAllowance(
                                updatedSms
                        );

                        planToUpdate.setValidityDays(
                                updatedValidity
                        );

                        planToUpdate.setInternationalRoaming(
                                updatedRoaming
                        );

                        planToUpdate.setStatus(
                                updatedStatus
                        );


                        planService.update(
                                planToUpdate
                        );


                        System.out.println(
                                "\nPlan updated successfully."
                        );


                        printPlan(
                                planToUpdate
                        );

                        break;


                    // ==================================================
                    // 11. DELETE PLAN
                    // ==================================================
                    case 11:

                        /*
                         * VALID INPUT:
                         * Use a TEST plan ID created specifically
                         * for demonstration.
                         *
                         * Example:
                         * 99
                         *
                         *
                         * WRONG INPUT:
                         * 999999
                         *
                         * Expected:
                         * No matching row / DAO behaviour.
                         *
                         * IMPORTANT:
                         * Do NOT delete an important existing plan
                         * during an interview.
                         */

                        long deletePlanId =
                                readLong(
                                        "Enter plan ID to delete: "
                                );


                        TelecomPlan planToDelete =
                                planService.findById(
                                        deletePlanId
                                );


                        if (planToDelete == null) {

                            System.out.println(
                                    "Plan not found."
                            );

                            break;
                        }


                        printPlan(
                                planToDelete
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


                        planService.delete(
                                deletePlanId
                        );


                        TelecomPlan deleted =
                                planService.findById(
                                        deletePlanId
                                );


                        if (deleted == null) {

                            System.out.println(
                                    "Plan deleted successfully."
                            );

                        } else {

                            System.out.println(
                                    "Plan could not be deleted."
                            );
                        }

                        break;


                    // ==================================================
                    // 0. EXIT
                    // ==================================================
                    case 0:

                        System.out.println(
                                "Exiting Plan Console..."
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
                        "PLAN OPERATION FAILED"
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
                "               PLAN CONSOLE"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Find Plan By ID"
        );

        System.out.println(
                "2. Find Plan By Code"
        );

        System.out.println(
                "3. Find All Plans"
        );

        System.out.println(
                "4. Find Active Plans"
        );

        System.out.println(
                "5. Filter Plans By Price"
        );

        System.out.println(
                "6. Filter Plans By Data Allowance"
        );

        System.out.println(
                "7. Sort Plans By Price"
        );

        System.out.println(
                "8. Compare Plans"
        );

        System.out.println(
                "9. Create Plan"
        );

        System.out.println(
                "10. Update Plan"
        );

        System.out.println(
                "11. Delete Plan"
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
                 * Expected:
                 * Invalid number.
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
    // SAFE DECIMAL INPUT
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
    // PLAN TYPE
    // ==========================================================

    private static PlanType readPlanType() {

        while (true) {

            String input =
                    scanner.nextLine().trim();


            try {

                return PlanType.valueOf(
                        input.toUpperCase()
                );

            } catch (IllegalArgumentException e) {

                /*
                 * VALID:
                 * PREPAID
                 * POSTPAID
                 *
                 * WRONG:
                 * INVALID
                 *
                 * Expected:
                 * Invalid plan type.
                 */

                System.out.println(
                        "Invalid plan type."
                );

                System.out.println(
                        "Valid values depend on your PlanType enum."
                );

                System.out.print(
                        "Enter plan type again: "
                );
            }
        }
    }


    // ==========================================================
    // ACCOUNT STATUS
    // ==========================================================

    private static AccountStatus readAccountStatus() {

        while (true) {

            String input =
                    scanner.nextLine().trim();


            try {

                return AccountStatus.valueOf(
                        input.toUpperCase()
                );

            } catch (IllegalArgumentException e) {

                /*
                 * VALID:
                 * ACTIVE
                 * INACTIVE
                 *
                 * WRONG:
                 * BLOCKED
                 *
                 * Expected:
                 * Invalid account status.
                 */

                System.out.println(
                        "Invalid account status."
                );

                System.out.println(
                        "Enter ACTIVE or INACTIVE."
                );

                System.out.print(
                        "Enter status again: "
                );
            }
        }
    }


    // ==========================================================
    // BOOLEAN INPUT
    // ==========================================================

    private static boolean readBoolean(
            String message) {

        while (true) {

            System.out.print(
                    message
            );

            String input =
                    scanner.nextLine().trim();


            if ("true".equalsIgnoreCase(
                    input
            )) {

                return true;

            }


            if ("false".equalsIgnoreCase(
                    input
            )) {

                return false;

            }


            /*
             * WRONG INPUT:
             * yes
             * maybe
             *
             * Expected:
             * Enter true or false.
             */

            System.out.println(
                    "Invalid input. Enter true or false."
            );
        }
    }


    // ==========================================================
    // PRINT PLAN
    // ==========================================================

    private static void printPlan(
            TelecomPlan plan) {

        System.out.println(
                "\n========== PLAN DETAILS =========="
        );

        System.out.println(
                "Plan ID: " +
                        plan.getPlanId()
        );

        System.out.println(
                "Plan Code: " +
                        plan.getPlanCode()
        );

        System.out.println(
                "Plan Name: " +
                        plan.getPlanName()
        );

        System.out.println(
                "Plan Type: " +
                        plan.getPlanType()
        );

        System.out.println(
                "Monthly Rental: ₹" +
                        plan.getMonthlyRental()
        );

        System.out.println(
                "Data Allowance: " +
                        plan.getDataAllowanceGB() +
                        " GB"
        );

        System.out.println(
                "Voice Minutes: " +
                        plan.getVoiceMinutes()
        );

        System.out.println(
                "SMS Allowance: " +
                        plan.getSmsAllowance()
        );

        System.out.println(
                "Validity: " +
                        plan.getValidityDays() +
                        " days"
        );

        System.out.println(
                "International Roaming: " +
                        plan.isInternationalRoaming()
        );

        System.out.println(
                "Status: " +
                        plan.getStatus()
        );

        System.out.println(
                "Created At: " +
                        plan.getCreatedAt()
        );

        System.out.println(
                "Updated At: " +
                        plan.getUpdatedAt()
        );

        System.out.println(
                "================================="
        );
    }
    private static String generatePlanCode() {

        List<TelecomPlan> plans =
                planService.findAll();

        long nextNumber = 101;

        for (TelecomPlan plan : plans) {

            String code =
                    plan.getPlanCode();

            if (code != null &&
                    code.startsWith("PLAN-")) {

                try {

                    long number =
                            Long.parseLong(
                                    code.substring(5)
                            );

                    if (number >= nextNumber) {
                        nextNumber = number + 1;
                    }

                } catch (NumberFormatException ignored) {
                    // Ignore plan codes that do not follow PLAN-number format
                }
            }
        }

        return "PLAN-" + nextNumber;
    }
}