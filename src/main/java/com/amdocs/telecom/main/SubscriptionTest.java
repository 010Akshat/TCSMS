package com.amdocs.telecom.main;

import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;
import com.amdocs.telecom.service.PlanService;
import com.amdocs.telecom.service.SubscriptionService;
import com.amdocs.telecom.service.impl.PlanServiceImpl;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;

import java.util.ArrayList;
import java.util.Comparator;
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

        long customerId = 3;

        // ==========================================
        // GET ACTIVE PLANS DYNAMICALLY
        // ==========================================

        List<TelecomPlan> activePlans =
                planService.findActivePlans();

        if (activePlans == null ||
                activePlans.size() < 2) {

            System.out.println(
                    "At least two active plans with different prices " +
                            "are required for this test."
            );

            return;
        }

        List<TelecomPlan> sortedPlans =
                new ArrayList<>(
                        activePlans
                );

        sortedPlans.sort(
                Comparator.comparing(
                        TelecomPlan::getMonthlyRental
                )
        );

        TelecomPlan lowerPlan = null;
        TelecomPlan higherPlan = null;

        for (TelecomPlan plan : sortedPlans) {

            if (plan.getMonthlyRental() == null) {
                continue;
            }

            if (lowerPlan == null) {

                lowerPlan = plan;

                continue;
            }

            if (plan.getMonthlyRental()
                    .compareTo(
                            lowerPlan.getMonthlyRental()
                    ) > 0) {

                higherPlan = plan;
                break;
            }
        }

        if (lowerPlan == null ||
                higherPlan == null) {

            System.out.println(
                    "Could not find two active plans " +
                            "with different monthly rentals."
            );

            return;
        }

        final long lowerPlanId =
                lowerPlan.getPlanId();

        final long higherPlanId =
                higherPlan.getPlanId();

        System.out.println(
                "\nSelected plans for testing:"
        );

        System.out.println(
                "Lower Plan: " +
                        lowerPlan.getPlanCode() +
                        " - ₹" +
                        lowerPlan.getMonthlyRental()
        );

        System.out.println(
                "Higher Plan: " +
                        higherPlan.getPlanCode() +
                        " - ₹" +
                        higherPlan.getMonthlyRental()
        );

        // ==========================================
        // UNIQUE TEST DATA
        // ==========================================

        String runId =
                String.valueOf(
                        System.currentTimeMillis()
                );

        String mobile1 =
                "98765" +
                        runId.substring(
                                runId.length() - 5
                        );

        String mobile2 =
                "98766" +
                        runId.substring(
                                runId.length() - 5
                        );

        String mobile3 =
                "98767" +
                        runId.substring(
                                runId.length() - 5
                        );

        String mobile4 =
                "98768" +
                        runId.substring(
                                runId.length() - 5
                        );

        String sim1 =
                "SIMTEST" +
                        runId +
                        "01";

        String sim2 =
                "SIMTEST" +
                        runId +
                        "02";

        String sim3 =
                "SIMTEST" +
                        runId +
                        "03";

        String sim4 =
                "SIMTEST" +
                        runId +
                        "04";

        long createdSubscriptionId = 0;

        // ==========================================
        // TEST 1: CREATE SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 1: CREATE SUBSCRIPTION ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.subscribe(
                            customerId,
                            lowerPlanId,
                            mobile1,
                            sim1,
                            "ESIM",
                            "POSTPAID"
                    );

            createdSubscriptionId =
                    subscription.getSubscriptionId();

            if (subscription.getSubscriptionId() > 0 &&
                    subscription.getCustomerId()
                            == customerId &&
                    subscription.getPlanId()
                            == lowerPlanId &&
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
                    "Subscription creation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: RETRIEVE BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 2: FIND SUBSCRIPTION BY ID ==="
        );

        try {

            MobileSubscription found =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (found != null &&
                    found.getSubscriptionId()
                            == createdSubscriptionId) {

                pass(
                        "Find subscription by ID"
                );

            } else {

                fail(
                        "Find subscription by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find subscription by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: FIND BY SUBSCRIPTION NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND BY SUBSCRIPTION NUMBER ==="
        );

        try {

            MobileSubscription current =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            MobileSubscription found =
                    subscriptionService
                            .findBySubscriptionNumber(
                                    current.getSubscriptionNumber()
                            );

            if (found != null &&
                    found.getSubscriptionId()
                            == createdSubscriptionId) {

                pass(
                        "Find subscription by number"
                );

            } else {

                fail(
                        "Find subscription by number"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find subscription by number: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: FIND BY MOBILE NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND BY MOBILE NUMBER ==="
        );

        try {

            MobileSubscription found =
                    subscriptionService
                            .findByMobileNumber(
                                    mobile1
                            );

            if (found != null &&
                    found.getSubscriptionId()
                            == createdSubscriptionId) {

                pass(
                        "Find subscription by mobile"
                );

            } else {

                fail(
                        "Find subscription by mobile"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find subscription by mobile: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: SECOND CONNECTION - DIFFERENT PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 5: SECOND CONNECTION - DIFFERENT PLAN ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.subscribe(
                            customerId,
                            higherPlanId,
                            mobile2,
                            sim2,
                            "PHYSICAL_SIM",
                            "POSTPAID"
                    );

            if (subscription.getSubscriptionId() > 0 &&
                    subscription.getCustomerId()
                            == customerId &&
                    subscription.getPlanId()
                            == higherPlanId) {

                pass(
                        "Second connection with different plan"
                );

            } else {

                fail(
                        "Second connection with different plan"
                );
            }

        } catch (Exception e) {

            fail(
                    "Second connection with different plan: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 6: SECOND CONNECTION - SAME PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 6: SECOND CONNECTION - SAME PLAN ==="
        );

        try {

            MobileSubscription subscription =
                    subscriptionService.subscribe(
                            customerId,
                            lowerPlanId,
                            mobile3,
                            sim3,
                            "ESIM",
                            "PREPAID"
                    );

            if (subscription.getSubscriptionId() > 0 &&
                    subscription.getPlanId()
                            == lowerPlanId) {

                pass(
                        "Same plan on another connection"
                );

            } else {

                fail(
                        "Same plan on another connection"
                );
            }

        } catch (Exception e) {

            fail(
                    "Same plan on another connection: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 7: DUPLICATE MOBILE
        // ==========================================

        System.out.println(
                "\n=== TEST 7: DUPLICATE MOBILE NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    lowerPlanId,
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
                            "Mobile number is already subscribed."
                    )) {

                pass(
                        "Duplicate mobile rejection"
                );

            } else {

                fail(
                        "Duplicate mobile rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 8: DUPLICATE SIM
        // ==========================================

        System.out.println(
                "\n=== TEST 8: DUPLICATE SIM NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    lowerPlanId,
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
                            "SIM number is already in use."
                    )) {

                pass(
                        "Duplicate SIM rejection"
                );

            } else {

                fail(
                        "Duplicate SIM rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 9: INVALID CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 9: INVALID CUSTOMER ==="
        );

        try {

            subscriptionService.subscribe(
                    999999,
                    lowerPlanId,
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
                            "Customer not found."
                    )) {

                pass(
                        "Invalid customer rejection"
                );

            } else {

                fail(
                        "Invalid customer rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 10: INVALID PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 10: INVALID PLAN ==="
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
                            "Plan not found."
                    )) {

                pass(
                        "Invalid plan rejection"
                );

            } else {

                fail(
                        "Invalid plan rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 11: INVALID SIM TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 11: INVALID SIM TYPE ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    lowerPlanId,
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
                            "Invalid SIM type."
                    )) {

                pass(
                        "Invalid SIM type rejection"
                );

            } else {

                fail(
                        "Invalid SIM type rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 12: INVALID SUBSCRIPTION TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 12: INVALID SUBSCRIPTION TYPE ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    lowerPlanId,
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
                            "Invalid subscription type."
                    )) {

                pass(
                        "Invalid subscription type rejection"
                );

            } else {

                fail(
                        "Invalid subscription type rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 13: BLANK MOBILE
        // ==========================================

        System.out.println(
                "\n=== TEST 13: BLANK MOBILE NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    lowerPlanId,
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
                            "Mobile number is mandatory."
                    )) {

                pass(
                        "Blank mobile rejection"
                );

            } else {

                fail(
                        "Blank mobile rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 14: BLANK SIM
        // ==========================================

        System.out.println(
                "\n=== TEST 14: BLANK SIM NUMBER ==="
        );

        try {

            subscriptionService.subscribe(
                    customerId,
                    lowerPlanId,
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
                            "SIM number is mandatory."
                    )) {

                pass(
                        "Blank SIM rejection"
                );

            } else {

                fail(
                        "Blank SIM rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 15: INACTIVE PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 15: INACTIVE PLAN ==="
        );

        TelecomPlan planToDeactivate =
                planService.findById(
                        higherPlanId
                );

        if (planToDeactivate == null) {

            fail(
                    "Inactive plan test"
            );

        } else {

            AccountStatus originalStatus =
                    planToDeactivate.getStatus();

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
                            higherPlanId,
                            mobile4,
                            sim4,
                            "ESIM",
                            "PREPAID"
                    );

                    fail(
                            "Inactive plan rejection"
                    );

                } catch (IllegalArgumentException e) {

                    if (e.getMessage() != null &&
                            e.getMessage().contains(
                                    "Inactive plan cannot be selected."
                            )) {

                        pass(
                                "Inactive plan rejection"
                        );

                    } else {

                        fail(
                                "Inactive plan rejection: " +
                                        e.getMessage()
                        );
                    }
                }

            } finally {

                planToDeactivate.setStatus(
                        originalStatus
                );

                planService.update(
                        planToDeactivate
                );
            }
        }

        // ==========================================
        // TEST 16: UPGRADE PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 16: UPGRADE PLAN ==="
        );

        try {

            subscriptionService.upgradePlan(
                    createdSubscriptionId,
                    higherPlanId,
                    "Upgrade test",
                    "CUSTOMER"
            );

            MobileSubscription updated =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updated != null &&
                    updated.getPlanId()
                            == higherPlanId) {

                pass(
                        "Upgrade plan"
                );

                System.out.println(
                        "Old Plan: " +
                                lowerPlan.getPlanCode()
                );

                System.out.println(
                        "New Plan: " +
                                higherPlan.getPlanCode()
                );

            } else {

                fail(
                        "Upgrade plan"
                );
            }

        } catch (Exception e) {

            fail(
                    "Upgrade plan: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 17: UPGRADE TO CHEAPER PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 17: INVALID UPGRADE ==="
        );

        try {

            subscriptionService.upgradePlan(
                    createdSubscriptionId,
                    lowerPlanId,
                    "Invalid upgrade test",
                    "CUSTOMER"
            );

            fail(
                    "Invalid upgrade rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Upgrade plan must have a higher monthly rental."
                    )) {

                pass(
                        "Invalid upgrade rejection"
                );

            } else {

                fail(
                        "Invalid upgrade rejection: " +
                                e.getMessage()
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

            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            createdSubscriptionId
                    );

            boolean found =
                    history.stream()
                            .anyMatch(item ->
                                    item.getOldPlanId()
                                            == lowerPlanId
                                            &&
                                            item.getNewPlanId()
                                                    == higherPlanId
                            );

            if (found) {

                pass(
                        "Subscription history"
                );

                System.out.println(
                        "History records: " +
                                history.size()
                );

            } else {

                fail(
                        "Subscription history"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription history: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 19: DOWNGRADE PLAN
        // ==========================================

        System.out.println(
                "\n=== TEST 19: DOWNGRADE PLAN ==="
        );

        try {

            subscriptionService.downgradePlan(
                    createdSubscriptionId,
                    lowerPlanId,
                    "Downgrade test",
                    "CUSTOMER"
            );

            MobileSubscription updated =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updated != null &&
                    updated.getPlanId()
                            == lowerPlanId) {

                pass(
                        "Downgrade plan"
                );

            } else {

                fail(
                        "Downgrade plan"
                );
            }

        } catch (Exception e) {

            fail(
                    "Downgrade plan: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 20: INVALID DOWNGRADE
        // ==========================================

        System.out.println(
                "\n=== TEST 20: INVALID DOWNGRADE ==="
        );

        try {

            subscriptionService.downgradePlan(
                    createdSubscriptionId,
                    higherPlanId,
                    "Invalid downgrade test",
                    "CUSTOMER"
            );

            fail(
                    "Invalid downgrade rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Downgrade plan must have a lower monthly rental."
                    )) {

                pass(
                        "Invalid downgrade rejection"
                );

            } else {

                fail(
                        "Invalid downgrade rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 21: CHANGE SUBSCRIPTION TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 21: CHANGE SUBSCRIPTION TYPE ==="
        );

        try {

            subscriptionService.changeSubscriptionType(
                    createdSubscriptionId,
                    "PREPAID",
                    "Type change test",
                    "CUSTOMER"
            );

            MobileSubscription updated =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updated != null &&
                    updated.getSubscriptionType()
                            == SubscriptionType.PREPAID) {

                pass(
                        "Subscription type change"
                );

            } else {

                fail(
                        "Subscription type change"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription type change: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 22: SAME SUBSCRIPTION TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 22: SAME SUBSCRIPTION TYPE ==="
        );

        try {

            subscriptionService.changeSubscriptionType(
                    createdSubscriptionId,
                    "PREPAID",
                    "Same type test",
                    "CUSTOMER"
            );

            fail(
                    "Same subscription type rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Connection is already of this type."
                    )) {

                pass(
                        "Same subscription type rejection"
                );

            } else {

                fail(
                        "Same subscription type rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 23: DEACTIVATE
        // ==========================================

        System.out.println(
                "\n=== TEST 23: DEACTIVATE SUBSCRIPTION ==="
        );

        try {

            subscriptionService.deactivateSubscription(
                    createdSubscriptionId
            );

            MobileSubscription updated =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updated != null &&
                    updated.getStatus()
                            == SubscriptionStatus.INACTIVE) {

                pass(
                        "Subscription deactivation"
                );

            } else {

                fail(
                        "Subscription deactivation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription deactivation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 24: REACTIVATE
        // ==========================================

        System.out.println(
                "\n=== TEST 24: REACTIVATE SUBSCRIPTION ==="
        );

        try {

            subscriptionService.activateSubscription(
                    createdSubscriptionId
            );

            MobileSubscription updated =
                    subscriptionService.findById(
                            createdSubscriptionId
                    );

            if (updated != null &&
                    updated.getStatus()
                            == SubscriptionStatus.ACTIVE) {

                pass(
                        "Subscription reactivation"
                );

            } else {

                fail(
                        "Subscription reactivation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Subscription reactivation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 25: CUSTOMER CONNECTIONS
        // ==========================================

        System.out.println(
                "\n=== TEST 25: CUSTOMER MULTIPLE CONNECTIONS ==="
        );

        try {

            List<MobileSubscription> subscriptions =
                    subscriptionService.findByCustomerId(
                            customerId
                    );

            final long subscriptionId =
                    createdSubscriptionId;

            boolean found =
                    subscriptions.stream()
                            .anyMatch(existing ->
                                    existing.getSubscriptionId()
                                            == subscriptionId
                            );

            if (found &&
                    subscriptions.size() >= 1) {

                pass(
                        "Customer multiple connections"
                );

                System.out.println(
                        "Connections found: " +
                                subscriptions.size()
                );

            } else {

                fail(
                        "Customer multiple connections"
                );
            }

        } catch (Exception e) {

            fail(
                    "Customer multiple connections: " +
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
                    "SUBSCRIPTION TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "SUBSCRIPTION TEST SUITE: FAILED"
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