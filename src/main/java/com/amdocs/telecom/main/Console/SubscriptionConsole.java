package com.amdocs.telecom.main.Console;

import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.service.SubscriptionService;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;

import java.util.List;
import java.util.Scanner;


public class SubscriptionConsole {

    private static final Scanner scanner =
            new Scanner(System.in);

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
                    // 1. SUBSCRIBE
                    // ==================================================
                    case 1:

                        /*
                         * VALID INPUT:
                         *
                         * Customer ID:
                         * 3
                         *
                         * Plan ID:
                         * 1
                         *
                         * Mobile Number:
                         * 9876543210
                         *
                         * SIM Type:
                         * ESIM
                         *
                         * Subscription Type:
                         * POSTPAID
                         *
                         *
                         * WRONG MOBILE:
                         *
                         * 987654321
                         *
                         * Expected:
                         * Mobile number must contain exactly 10 digits.
                         *
                         *
                         * WRONG SIM TYPE:
                         *
                         * INVALID_SIM
                         *
                         * Expected:
                         * Invalid SIM type.
                         */

                        long subscribeCustomerId =
                                readLong(
                                        "Enter customer ID: "
                                );


                        long subscribePlanId =
                                readLong(
                                        "Enter plan ID: "
                                );


                        System.out.print(
                                "Enter mobile number: "
                        );

                        String subscribeMobile =
                                scanner.nextLine().trim();


                        if (!subscribeMobile.matches("\\d{10}")) {

                            throw new IllegalArgumentException(
                                    "Mobile number must contain exactly 10 digits."
                            );
                        }


                        String subscribeSim =
                                generateSimNumber();

                        System.out.println(
                                "Generated SIM Number: " +
                                        subscribeSim
                        );


                        System.out.print(
                                "Enter SIM type (ESIM/PHYSICAL_SIM): "
                        );

                        String subscribeSimType =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter subscription type (PREPAID/POSTPAID): "
                        );

                        String subscribeType =
                                scanner.nextLine().trim();


                        MobileSubscription newSubscription =
                                subscriptionService.subscribe(
                                        subscribeCustomerId,
                                        subscribePlanId,
                                        subscribeMobile,
                                        subscribeSim,
                                        subscribeSimType,
                                        subscribeType
                                );


                        System.out.println(
                                "\nSubscription created successfully."
                        );


                        printSubscription(
                                newSubscription
                        );

                        break;


                    // ==================================================
                    // 2. UPGRADE PLAN
                    // ==================================================
                    case 2:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Subscription ID:
                         * 1
                         *
                         * New Plan ID:
                         * 103
                         *
                         * Reason:
                         * Customer requested higher data plan
                         *
                         * Changed By:
                         * CUSTOMER
                         *
                         *
                         * WRONG INPUT:
                         *
                         * New Plan ID with lower rental.
                         *
                         * Expected:
                         * Upgrade plan must have a higher
                         * monthly rental.
                         */

                        long upgradeSubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        long upgradePlanId =
                                readLong(
                                        "Enter new plan ID: "
                                );


                        System.out.print(
                                "Enter change reason: "
                        );

                        String upgradeReason =
                                scanner.nextLine().trim();

                        String upgradeChangedBy =
                                "Customer";


                        subscriptionService.upgradePlan(
                                upgradeSubscriptionId,
                                upgradePlanId,
                                upgradeReason,
                                upgradeChangedBy
                        );


                        System.out.println(
                                "Subscription upgraded successfully."
                        );


                        printSubscription(
                                subscriptionService.findById(
                                        upgradeSubscriptionId
                                )
                        );

                        break;


                    // ==================================================
                    // 3. DOWNGRADE PLAN
                    // ==================================================
                    case 3:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Subscription ID:
                         * 1
                         *
                         * New Plan ID:
                         * 105
                         *
                         * Reason:
                         * Customer requested cheaper plan
                         *
                         * Changed By:
                         * CUSTOMER
                         *
                         *
                         * WRONG INPUT:
                         *
                         * Select a plan with equal/higher rental.
                         *
                         * Expected:
                         * Downgrade plan must have a lower
                         * monthly rental.
                         */

                        long downgradeSubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        long downgradePlanId =
                                readLong(
                                        "Enter new plan ID: "
                                );


                        System.out.print(
                                "Enter change reason: "
                        );

                        String downgradeReason =
                                scanner.nextLine().trim();


                        String downgradeChangedBy =
                                "CUSTOMER";


                        subscriptionService.downgradePlan(
                                downgradeSubscriptionId,
                                downgradePlanId,
                                downgradeReason,
                                downgradeChangedBy
                        );


                        System.out.println(
                                "Subscription downgraded successfully."
                        );


                        printSubscription(
                                subscriptionService.findById(
                                        downgradeSubscriptionId
                                )
                        );

                        break;


                    // ==================================================
                    // 4. CHANGE SUBSCRIPTION TYPE
                    // ==================================================
                    case 4:

                        /*
                         * VALID INPUT EXAMPLE:
                         *
                         * Subscription ID:
                         * 1
                         *
                         * New Type:
                         * PREPAID
                         *
                         * Reason:
                         * Customer requested prepaid conversion
                         *
                         * Changed By:
                         * CUSTOMER
                         *
                         *
                         * WRONG INPUT:
                         *
                         * INVALID_TYPE
                         *
                         * Expected:
                         * Invalid subscription type.
                         *
                         *
                         * Another wrong input:
                         * Same type as current subscription.
                         *
                         * Expected:
                         * Connection is already of this type.
                         */

                        long typeSubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        System.out.print(
                                "Enter new subscription type (PREPAID/POSTPAID): "
                        );

                        String newSubscriptionType =
                                scanner.nextLine().trim();


                        System.out.print(
                                "Enter change reason: "
                        );

                        String typeChangeReason =
                                scanner.nextLine().trim();


                        String typeChangedBy =
                                "CUSTOMER";


                        subscriptionService.changeSubscriptionType(
                                typeSubscriptionId,
                                newSubscriptionType,
                                typeChangeReason,
                                typeChangedBy
                        );


                        System.out.println(
                                "Subscription type changed successfully."
                        );


                        printSubscription(
                                subscriptionService.findById(
                                        typeSubscriptionId
                                )
                        );

                        break;


                    // ==================================================
                    // 5. ACTIVATE SUBSCRIPTION
                    // ==================================================
                    case 5:

                        /*
                         * VALID INPUT:
                         * 1
                         *
                         * Expected:
                         * Subscription becomes ACTIVE.
                         *
                         *
                         * WRONG INPUT:
                         * Already active subscription ID.
                         *
                         * Expected:
                         * Subscription is already active.
                         */

                        long activateId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        subscriptionService.activateSubscription(
                                activateId
                        );


                        System.out.println(
                                "Subscription activated successfully."
                        );


                        printSubscription(
                                subscriptionService.findById(
                                        activateId
                                )
                        );

                        break;


                    // ==================================================
                    // 6. DEACTIVATE SUBSCRIPTION
                    // ==================================================
                    case 6:

                        /*
                         * VALID INPUT:
                         * 1
                         *
                         * Expected:
                         * Subscription becomes INACTIVE.
                         *
                         *
                         * WRONG INPUT:
                         * Already inactive subscription ID.
                         *
                         * Expected:
                         * Subscription is already inactive.
                         */

                        long deactivateId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        subscriptionService.deactivateSubscription(
                                deactivateId
                        );


                        System.out.println(
                                "Subscription deactivated successfully."
                        );


                        printSubscription(
                                subscriptionService.findById(
                                        deactivateId
                                )
                        );

                        break;


                    // ==================================================
                    // 7. FIND SUBSCRIPTION BY ID
                    // ==================================================
                    case 7:

                        /*
                         * VALID INPUT:
                         * 1
                         *
                         * WRONG INPUT:
                         * 999999
                         *
                         * Expected:
                         * null / Subscription not found.
                         */

                        long findId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        MobileSubscription foundById =
                                subscriptionService.findById(
                                        findId
                                );


                        if (foundById != null) {

                            printSubscription(
                                    foundById
                            );

                        } else {

                            System.out.println(
                                    "Subscription not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 8. FIND BY SUBSCRIPTION NUMBER
                    // ==================================================
                    case 8:

                        /*
                         * VALID INPUT EXAMPLE:
                         * SUB100001
                         *
                         * WRONG INPUT:
                         * SUB999999
                         *
                         * Expected:
                         * Subscription not found.
                         */

                        System.out.print(
                                "Enter subscription number: "
                        );

                        String subscriptionNumber =
                                scanner.nextLine().trim();


                        MobileSubscription foundByNumber =
                                subscriptionService
                                        .findBySubscriptionNumber(
                                                subscriptionNumber
                                        );


                        if (foundByNumber != null) {

                            printSubscription(
                                    foundByNumber
                            );

                        } else {

                            System.out.println(
                                    "Subscription not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 9. FIND BY MOBILE NUMBER
                    // ==================================================
                    case 9:

                        /*
                         * VALID INPUT:
                         * 9876543210
                         *
                         * WRONG INPUT:
                         * 1111111111
                         *
                         * Expected:
                         * Subscription not found.
                         */

                        System.out.print(
                                "Enter mobile number: "
                        );

                        String searchMobile =
                                scanner.nextLine().trim();


                        MobileSubscription foundByMobile =
                                subscriptionService
                                        .findByMobileNumber(
                                                searchMobile
                                        );


                        if (foundByMobile != null) {

                            printSubscription(
                                    foundByMobile
                            );

                        } else {

                            System.out.println(
                                    "Subscription not found."
                            );
                        }

                        break;


                    // ==================================================
                    // 10. FIND BY CUSTOMER ID
                    // ==================================================
                    case 10:

                        /*
                         * VALID INPUT:
                         * 3
                         *
                         * WRONG INPUT:
                         * 999999
                         *
                         * Expected:
                         * Empty list / no subscriptions found.
                         */

                        long searchCustomerId =
                                readLong(
                                        "Enter customer ID: "
                                );


                        List<MobileSubscription> customerSubscriptions =
                                subscriptionService
                                        .findByCustomerId(
                                                searchCustomerId
                                        );


                        if (customerSubscriptions != null &&
                                !customerSubscriptions.isEmpty()) {

                            System.out.println(
                                    "\nSubscriptions found: " +
                                            customerSubscriptions.size()
                            );


                            for (MobileSubscription subscription :
                                    customerSubscriptions) {

                                printSubscription(
                                        subscription
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No subscriptions found for customer."
                            );
                        }

                        break;


                    // ==================================================
                    // 11. FIND ALL SUBSCRIPTIONS
                    // ==================================================
                    case 11:

                        /*
                         * NO INPUT REQUIRED.
                         *
                         * Select:
                         * 11
                         *
                         * Expected:
                         * All subscriptions displayed.
                         */

                        List<MobileSubscription> allSubscriptions =
                                subscriptionService.findAll();


                        if (allSubscriptions != null &&
                                !allSubscriptions.isEmpty()) {

                            System.out.println(
                                    "\nTotal subscriptions: " +
                                            allSubscriptions.size()
                            );


                            for (MobileSubscription subscription :
                                    allSubscriptions) {

                                printSubscription(
                                        subscription
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No subscriptions found."
                            );
                        }

                        break;


                    // ==================================================
                    // 12. FIND SUBSCRIPTION HISTORY
                    // ==================================================
                    case 12:

                        /*
                         * VALID INPUT:
                         * 1
                         *
                         * Expected:
                         * Upgrade/downgrade history for subscription.
                         *
                         *
                         * WRONG INPUT:
                         * 999999
                         *
                         * Expected:
                         * Empty history.
                         */

                        long historySubscriptionId =
                                readLong(
                                        "Enter subscription ID: "
                                );


                        List<SubscriptionHistory> history =
                                subscriptionService.findHistory(
                                        historySubscriptionId
                                );


                        if (history != null &&
                                !history.isEmpty()) {

                            System.out.println(
                                    "\n========== SUBSCRIPTION HISTORY =========="
                            );


                            for (SubscriptionHistory record :
                                    history) {

                                printHistory(
                                        record
                                );

                                System.out.println(
                                        "------------------------------------"
                                );
                            }

                        } else {

                            System.out.println(
                                    "No subscription history found."
                            );
                        }

                        break;


                    // ==================================================
                    // 0. EXIT
                    // ==================================================
                    case 0:

                        System.out.println(
                                "Exiting Subscription Console..."
                        );

                        scanner.close();

                        return;


                    default:

                        System.out.println(
                                "Invalid menu choice."
                        );

                        System.out.println(
                                "Choose a number from 0 to 12."
                        );
                }

            } catch (Exception e) {

                System.out.println(
                        "\n=========================================="
                );

                System.out.println(
                        "SUBSCRIPTION OPERATION FAILED"
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
                "         SUBSCRIPTION CONSOLE"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "1. Subscribe"
        );

        System.out.println(
                "2. Upgrade Plan"
        );

        System.out.println(
                "3. Downgrade Plan"
        );

        System.out.println(
                "4. Change Subscription Type"
        );

        System.out.println(
                "5. Activate Subscription"
        );

        System.out.println(
                "6. Deactivate Subscription"
        );

        System.out.println(
                "7. Find Subscription By ID"
        );

        System.out.println(
                "8. Find By Subscription Number"
        );

        System.out.println(
                "9. Find By Mobile Number"
        );

        System.out.println(
                "10. Find By Customer ID"
        );

        System.out.println(
                "11. Find All Subscriptions"
        );

        System.out.println(
                "12. Find Subscription History"
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
    // PRINT SUBSCRIPTION
    // ==========================================================

    private static void printSubscription(
            MobileSubscription subscription) {

        System.out.println(
                "\n========== SUBSCRIPTION DETAILS =========="
        );

        System.out.println(
                "Subscription ID: " +
                        subscription.getSubscriptionId()
        );

        System.out.println(
                "Subscription Number: " +
                        subscription.getSubscriptionNumber()
        );

        System.out.println(
                "Customer ID: " +
                        subscription.getCustomerId()
        );

        System.out.println(
                "Plan ID: " +
                        subscription.getPlanId()
        );

        System.out.println(
                "Mobile Number: " +
                        subscription.getMobileNumber()
        );

        System.out.println(
                "SIM Number: " +
                        subscription.getSimNumber()
        );

        System.out.println(
                "SIM Type: " +
                        subscription.getSimType()
        );

        System.out.println(
                "Subscription Type: " +
                        subscription.getSubscriptionType()
        );

        System.out.println(
                "Status: " +
                        subscription.getStatus()
        );

        System.out.println(
                "Activation Date: " +
                        subscription.getActivationDate()
        );

        System.out.println(
                "Created At: " +
                        subscription.getCreatedAt()
        );

        System.out.println(
                "Updated At: " +
                        subscription.getUpdatedAt()
        );

        System.out.println(
                "=========================================="
        );
    }


    // ==========================================================
    // PRINT SUBSCRIPTION HISTORY
    // ==========================================================

    private static void printHistory(
            SubscriptionHistory history) {

        System.out.println(
                "\n========== SUBSCRIPTION HISTORY =========="
        );

        System.out.println(
                "History ID: " +
                        history.getHistoryId()
        );

        System.out.println(
                "Subscription ID: " +
                        history.getSubscriptionId()
        );

        System.out.println(
                "Old Plan ID: " +
                        history.getOldPlanId()
        );

        System.out.println(
                "New Plan ID: " +
                        history.getNewPlanId()
        );

        System.out.println(
                "Change Date: " +
                        history.getChangeDate()
        );

        System.out.println(
                "Change Reason: " +
                        history.getChangeReason()
        );

        System.out.println(
                "Changed By: " +
                        history.getChangedBy()
        );

        System.out.println(
                "=========================================="
        );


    }
    private static String generateSimNumber() {

        return "SIM" +
                System.currentTimeMillis();
    }
}