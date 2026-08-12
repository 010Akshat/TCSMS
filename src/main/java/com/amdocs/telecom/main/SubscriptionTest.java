package com.amdocs.telecom.main;

import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.service.PlanService;
import com.amdocs.telecom.service.SubscriptionService;
import com.amdocs.telecom.service.impl.PlanServiceImpl;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;

import java.util.List;

public class SubscriptionTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        SubscriptionService subscriptionService =
                new SubscriptionServiceImpl();

        PlanService planService =
                new PlanServiceImpl();

        System.out.println(
                "=== SUBSCRIPTION TEST SUITE ==="
        );

        /*
         * Fresh values for every execution.
         *
         * Example:
         * mobile  -> 98765XXXXX
         * SIM     -> SIMTESTXXXXX
         */
        String runId =
                String.valueOf(
                        System.currentTimeMillis() % 100000
                );

        String mobile1 =
                "98765" + String.format("%05d", Integer.parseInt(runId));

        String mobile2 =
                "98766" + String.format("%05d", Integer.parseInt(runId));

        String mobile3 =
                "98767" + String.format("%05d", Integer.parseInt(runId));

        String mobile4 =
                "98768" + String.format("%05d", Integer.parseInt(runId));

        String mobile5 =
                "98769" + String.format("%05d", Integer.parseInt(runId));

        String sim1 = "SIMTEST" + runId + "01";
        String sim2 = "SIMTEST" + runId + "02";
        String sim3 = "SIMTEST" + runId + "03";
        String sim4 = "SIMTEST" + runId + "04";
        String sim5 = "SIMTEST" + runId + "05";

        long customerId = 3;

        long plan1 = 1;
        long plan2 = 2;
        long plan3 = 3;
        long plan5 = 5;

        long createdSubscriptionId = 0;

        // ==========================================
        // TEST 1: CREATE FIRST SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 1: CREATE SUBSCRIPTION ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.subscribe(
                            customerId,
                            plan1,
                            mobile1,
                            sim1,
                            "ESIM",
                            "POSTPAID"
                    );

            createdSubscriptionId =
                    subscription.getSubscriptionId();

            if (subscription.getSubscriptionId() > 0 &&
                    subscription.getPlanId() == plan1 &&
                    subscription.getStatus()
                            == SubscriptionStatus.ACTIVE) {

                pass(
                        "Subscription creation"
                );

                printSubscription(
                        subscription
                );

            } else {

                fail(
                        "Subscription creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription creation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: RETRIEVE SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 2: SUBSCRIPTION RETRIEVAL ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (subscription != null &&
                    subscription.getSubscriptionId()
                            == createdSubscriptionId) {

                pass(
                        "Subscription retrieval"
                );

                printSubscription(
                        subscription
                );

            } else {

                fail(
                        "Subscription retrieval"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription retrieval: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: SECOND CONNECTION - DIFFERENT PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 3: SECOND CONNECTION - DIFFERENT PLAN ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.subscribe(
                            customerId,
                            plan2,
                            mobile2,
                            sim2,
                            "PHYSICAL_SIM",
                            "POSTPAID"
                    );

            if (subscription.getPlanId() == plan2 &&
                    subscription.getCustomerId()
                            == customerId) {

                pass(
                        "Second connection with different plan"
                );

                printSubscription(
                        subscription
                );

            } else {

                fail(
                        "Second connection with different plan"
                );
            }

        } catch (Exception e) {

            fail(
                    "Second connection with different plan: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: SECOND CONNECTION - SAME PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 4: SECOND CONNECTION - SAME PLAN ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.subscribe(
                            customerId,
                            plan1,
                            mobile3,
                            sim3,
                            "ESIM",
                            "PREPAID"
                    );

            if (subscription.getPlanId() == plan1) {

                pass(
                        "Same plan on another connection"
                );

                printSubscription(
                        subscription
                );

            } else {

                fail(
                        "Same plan on another connection"
                );
            }

        } catch (Exception e) {

            fail(
                    "Same plan on another connection: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: DUPLICATE MOBILE NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 5: DUPLICATE MOBILE NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan2,
                    mobile1,
                    sim4,
                    "ESIM",
                    "POSTPAID"
            );

            fail(
                    "Duplicate mobile rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Mobile number is already subscribed.")) {

                pass(
                        "Duplicate mobile rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Duplicate mobile rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 6: DUPLICATE SIM NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 6: DUPLICATE SIM NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan2,
                    mobile4,
                    sim1,
                    "PHYSICAL_SIM",
                    "POSTPAID"
            );

            fail(
                    "Duplicate SIM rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "SIM number is already in use.")) {

                pass(
                        "Duplicate SIM rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Duplicate SIM rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 7: INVALID CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 7: INVALID CUSTOMER ==="
        );

        try {

            subscriptionService.subscribe(
                    999999,
                    plan1,
                    mobile4,
                    sim4,
                    "ESIM",
                    "PREPAID"
            );

            fail(
                    "Invalid customer rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Customer not found.")) {

                pass(
                        "Invalid customer rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Invalid customer rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 8: INVALID PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 8: INVALID PLAN ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    999999,
                    mobile4,
                    sim4,
                    "ESIM",
                    "PREPAID"
            );

            fail(
                    "Invalid plan rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Plan not found.")) {

                pass(
                        "Invalid plan rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Invalid plan rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 9: INACTIVE PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 9: INACTIVE PLAN ==="
        );

        TelecomPlan planToDeactivate =
                planService.findById(plan5);

        if (planToDeactivate == null) {

            fail(
                    "Inactive plan test - plan 5 not found"
            );

        } else {

            try {

                planToDeactivate.setStatus(
                        AccountStatus.INACTIVE
                );

                planService.update(
                        planToDeactivate
                );

                try {

                    subscriptionService.subscribe(
                            customerId,
                            plan5,
                            mobile5,
                            sim5,
                            "ESIM",
                            "PREPAID"
                    );

                    fail(
                            "Inactive plan rejection"
                    );

                } catch (IllegalArgumentException e) {

                    if (e.getMessage() != null &&
                            e.getMessage().contains(
                                    "Inactive plan cannot be selected.")) {

                        pass(
                                "Inactive plan rejection"
                        );

                        System.out.println(
                                "Reason: " + e.getMessage()
                        );

                    } else {

                        fail(
                                "Inactive plan rejection: "
                                        + e.getMessage()
                        );
                    }
                }

            } finally {

                /*
                 * Restore original plan status.
                 *
                 * Our current database state had PLAN-105 as ACTIVE.
                 */
                planToDeactivate.setStatus(
                        AccountStatus.ACTIVE
                );

                planService.update(
                        planToDeactivate
                );
            }
        }

        // ==========================================
        // TEST 10: INVALID SIM TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 10: INVALID SIM TYPE ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan3,
                    mobile4,
                    sim4,
                    "INVALID_SIM",
                    "PREPAID"
            );

            fail(
                    "Invalid SIM type rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Invalid SIM type.")) {

                pass(
                        "Invalid SIM type rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Invalid SIM type rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 11: INVALID SUBSCRIPTION TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 11: INVALID SUBSCRIPTION TYPE ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan3,
                    mobile4,
                    sim4,
                    "ESIM",
                    "INVALID_TYPE"
            );

            fail(
                    "Invalid subscription type rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Invalid subscription type.")) {

                pass(
                        "Invalid subscription type rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Invalid subscription type rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 12: BLANK MOBILE NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 12: BLANK MOBILE NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan3,
                    "",
                    sim4,
                    "ESIM",
                    "PREPAID"
            );

            fail(
                    "Blank mobile rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Mobile number is mandatory.")) {

                pass(
                        "Blank mobile rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Blank mobile rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 13: BLANK SIM NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 13: BLANK SIM NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan3,
                    mobile4,
                    "",
                    "ESIM",
                    "PREPAID"
            );

            fail(
                    "Blank SIM rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "SIM number is mandatory.")) {

                pass(
                        "Blank SIM rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Blank SIM rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 14: BLANK SIM TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 14: BLANK SIM TYPE ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan3,
                    mobile4,
                    sim4,
                    "",
                    "PREPAID"
            );

            fail(
                    "Blank SIM type rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "SIM type is mandatory.")) {

                pass(
                        "Blank SIM type rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Blank SIM type rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 15: BLANK SUBSCRIPTION TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 15: BLANK SUBSCRIPTION TYPE ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    plan3,
                    mobile4,
                    sim4,
                    "ESIM",
                    ""
            );

            fail(
                    "Blank subscription type rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Subscription type is mandatory.")) {

                pass(
                        "Blank subscription type rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Blank subscription type rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 16: CHANGE PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 16: CHANGE PLAN ==="
        );

        try {

            subscriptionService.upgradePlan(
                    createdSubscriptionId,
                    plan2,
                    "Plan change test",
                    "CUSTOMER"
            );

            MobileSubscription updatedSubscription =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updatedSubscription != null &&
                    updatedSubscription.getPlanId()
                            == plan2) {

                pass(
                        "Plan change"
                );

                System.out.println(
                        "Subscription ID: " +
                                updatedSubscription
                                        .getSubscriptionId()
                );

                System.out.println(
                        "Old Plan ID: " +
                                plan1
                );

                System.out.println(
                        "New Plan ID: " +
                                updatedSubscription.getPlanId()
                );

            } else {

                fail(
                        "Plan change"
                );
            }

        } catch (Exception e) {

            fail(
                    "Plan change: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 17: SAME PLAN ON SAME CONNECTION
        // ==========================================

        System.out.println(
                "\n=== TEST 17: SAME PLAN ON SAME CONNECTION ==="
        );

        try {

            subscriptionService.upgradePlan(
                    createdSubscriptionId,
                    plan2,
                    "Same plan test",
                    "CUSTOMER"
            );

            fail(
                    "Same plan rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Connection is already using this plan.")) {

                pass(
                        "Same plan rejection"
                );

                System.out.println(
                        "Reason: " + e.getMessage()
                );

            } else {

                fail(
                        "Same plan rejection: "
                                + e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 18: SUBSCRIPTION HISTORY
        // ==========================================

        System.out.println(
                "\n=== TEST 18: SUBSCRIPTION HISTORY ==="
        );

        try {

            List<SubscriptionHistory> historyList =
                    subscriptionService.findHistory(
                            createdSubscriptionId
                    );

            if (!historyList.isEmpty()) {

                SubscriptionHistory latestHistory =
                        historyList.get(0);

                if (latestHistory.getOldPlanId()
                        == plan1 &&
                        latestHistory.getNewPlanId()
                                == plan2) {

                    pass(
                            "Subscription history"
                    );

                    System.out.println(
                            "History ID: " +
                                    latestHistory.getHistoryId()
                    );

                    System.out.println(
                            "Subscription ID: " +
                                    latestHistory
                                            .getSubscriptionId()
                    );

                    System.out.println(
                            "Old Plan ID: " +
                                    latestHistory.getOldPlanId()
                    );

                    System.out.println(
                            "New Plan ID: " +
                                    latestHistory.getNewPlanId()
                    );

                    System.out.println(
                            "Change Reason: " +
                                    latestHistory.getChangeReason()
                    );

                    System.out.println(
                            "Changed By: " +
                                    latestHistory.getChangedBy()
                    );

                } else {

                    fail(
                            "Subscription history content"
                    );
                }

            } else {

                fail(
                        "Subscription history"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription history: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 19: CHANGE SUBSCRIPTION TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 19: CHANGE SUBSCRIPTION TYPE ==="
        );

        try {

            subscriptionService.changeSubscriptionType(
                    createdSubscriptionId,
                    "PREPAID",
                    "Subscription type change test",
                    "CUSTOMER"
            );

            MobileSubscription updatedSubscription =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updatedSubscription != null &&
                    updatedSubscription.getSubscriptionType()
                            == SubscriptionType.PREPAID) {

                pass(
                        "Subscription type change"
                );

                System.out.println(
                        "Subscription ID: " +
                                updatedSubscription
                                        .getSubscriptionId()
                );

                System.out.println(
                        "New Subscription Type: " +
                                updatedSubscription
                                        .getSubscriptionType()
                );

            } else {

                fail(
                        "Subscription type change"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription type change: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 20: DEACTIVATE SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 20: DEACTIVATE SUBSCRIPTION ==="
        );

        try {

            subscriptionService.deactivateSubscription(
                    createdSubscriptionId
            );

            MobileSubscription updatedSubscription =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updatedSubscription != null &&
                    updatedSubscription.getStatus()
                            == SubscriptionStatus.INACTIVE) {

                pass(
                        "Subscription deactivation"
                );

                System.out.println(
                        "Subscription ID: " +
                                updatedSubscription
                                        .getSubscriptionId()
                );

                System.out.println(
                        "Status: " +
                                updatedSubscription.getStatus()
                );

            } else {

                fail(
                        "Subscription deactivation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription deactivation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 21: REACTIVATE SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 21: REACTIVATE SUBSCRIPTION ==="
        );

        try {

            subscriptionService.activateSubscription(
                    createdSubscriptionId
            );

            MobileSubscription updatedSubscription =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updatedSubscription != null &&
                    updatedSubscription.getStatus()
                            == SubscriptionStatus.ACTIVE) {

                pass(
                        "Subscription reactivation"
                );

                System.out.println(
                        "Subscription ID: " +
                                updatedSubscription
                                        .getSubscriptionId()
                );

                System.out.println(
                        "Status: " +
                                updatedSubscription.getStatus()
                );

            } else {

                fail(
                        "Subscription reactivation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription reactivation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 22: CUSTOMER MULTIPLE CONNECTIONS
        // ==========================================

        System.out.println(
                "\n=== TEST 22: CUSTOMER MULTIPLE CONNECTIONS ==="
        );

        try {

            List<MobileSubscription> subscriptions =
                    subscriptionService.findByCustomerId(
                            customerId
                    );

            if (subscriptions.size() >= 3) {

                pass(
                        "Multiple connections retrieval"
                );

                System.out.println(
                        "Total connections for customer " +
                                customerId +
                                ": " +
                                subscriptions.size()
                );

                for (MobileSubscription s
                        : subscriptions) {

                    System.out.println(
                            "Subscription ID: " +
                                    s.getSubscriptionId() +
                                    " | Mobile: " +
                                    s.getMobileNumber() +
                                    " | Plan ID: " +
                                    s.getPlanId() +
                                    " | Status: " +
                                    s.getStatus()
                    );
                }

            } else {

                fail(
                        "Multiple connections retrieval"
                );
            }

        } catch (Exception e) {

            fail(
                    "Multiple connections retrieval: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // FINAL RESULT
        // ==========================================

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "TOTAL PASSED: " + passed
        );

        System.out.println(
                "TOTAL FAILED: " + failed
        );

        System.out.println(
                "=========================================="
        );

        if (failed == 0) {

            System.out.println(
                    "SUBSCRIPTION TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "SUBSCRIPTION TEST SUITE: FAILED"
            );
        }
    }

    private static void pass(String testName) {

        passed++;

        System.out.println(
                testName + ": PASSED"
        );
    }

    private static void fail(String testName) {

        failed++;

        System.out.println(
                testName + ": FAILED"
        );
    }

    private static void printSubscription(
            MobileSubscription subscription) {

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
    }
}