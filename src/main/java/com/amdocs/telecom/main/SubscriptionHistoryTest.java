package com.amdocs.telecom.main;

import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.service.SubscriptionService;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;

import java.util.List;

public class SubscriptionHistoryTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        SubscriptionService subscriptionService =
                new SubscriptionServiceImpl();

        System.out.println(
                "=== SUBSCRIPTION HISTORY TEST SUITE ==="
        );

        // ==========================================
        // GET AN EXISTING SUBSCRIPTION WITH HISTORY
        // ==========================================

        List<com.amdocs.telecom.model.MobileSubscription> subscriptions =
                subscriptionService.findAll();

        if (subscriptions == null ||
                subscriptions.isEmpty()) {

            System.out.println(
                    "No subscriptions found. Test cannot run."
            );

            return;
        }

        long subscriptionId = -1;

        for (com.amdocs.telecom.model.MobileSubscription subscription
                : subscriptions) {

            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            subscription.getSubscriptionId()
                    );

            if (history != null &&
                    !history.isEmpty()) {

                subscriptionId =
                        subscription.getSubscriptionId();

                break;
            }
        }

        if (subscriptionId == -1) {

            System.out.println(
                    "No subscription with history found."
            );

            return;
        }

        // ==========================================
        // TEST 1: FIND HISTORY BY SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 1: FIND HISTORY BY SUBSCRIPTION ==="
        );

        try {

            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            subscriptionId
                    );

            if (history != null &&
                    !history.isEmpty()) {

                pass(
                        "Find history by subscription"
                );

                System.out.println(
                        "History records: " +
                                history.size()
                );

            } else {

                fail(
                        "Find history by subscription"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find history by subscription: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: VERIFY LATEST HISTORY
        // ==========================================

        System.out.println(
                "\n=== TEST 2: VERIFY LATEST HISTORY ==="
        );

        try {

            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            subscriptionId
                    );

            SubscriptionHistory latest =
                    history.get(0);

            if (latest.getHistoryId() > 0 &&
                    latest.getSubscriptionId()
                            == subscriptionId) {

                pass(
                        "Verify latest history"
                );

                System.out.println(
                        "History ID: " +
                                latest.getHistoryId()
                );

                System.out.println(
                        "Subscription ID: " +
                                latest.getSubscriptionId()
                );

                System.out.println(
                        "Old Plan ID: " +
                                latest.getOldPlanId()
                );

                System.out.println(
                        "New Plan ID: " +
                                latest.getNewPlanId()
                );

                System.out.println(
                        "Change Date: " +
                                latest.getChangeDate()
                );

                System.out.println(
                        "Change Reason: " +
                                latest.getChangeReason()
                );

                System.out.println(
                        "Changed By: " +
                                latest.getChangedBy()
                );

            } else {

                fail(
                        "Verify latest history"
                );
            }

        } catch (Exception e) {

            fail(
                    "Verify latest history: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: FIND HISTORY BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND HISTORY BY ID ==="
        );

        try {

            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            subscriptionId
                    );

            SubscriptionHistory historyRecord =
                    history.get(0);

            /*
             * SubscriptionService does not expose
             * findHistoryById(), so use the DAO
             * directly for this DAO-level test.
             */
            com.amdocs.telecom.dao.SubscriptionHistoryDAO
                    historyDAO =
                    new com.amdocs.telecom.dao.impl
                            .SubscriptionHistoryDAOImpl();

            SubscriptionHistory found =
                    historyDAO.findById(
                            historyRecord.getHistoryId()
                    );

            if (found != null &&
                    found.getHistoryId()
                            == historyRecord.getHistoryId() &&
                    found.getSubscriptionId()
                            == subscriptionId) {

                pass(
                        "Find history by ID"
                );

            } else {

                fail(
                        "Find history by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find history by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: FIND ALL HISTORY
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND ALL HISTORY ==="
        );

        try {

            com.amdocs.telecom.dao.SubscriptionHistoryDAO
                    historyDAO =
                    new com.amdocs.telecom.dao.impl
                            .SubscriptionHistoryDAOImpl();

            List<SubscriptionHistory> allHistory =
                    historyDAO.findAll();

            final long targetSubscriptionId =
                    subscriptionId;

            boolean found =
                    allHistory.stream()
                            .anyMatch(history ->
                                    history.getSubscriptionId()
                                            == targetSubscriptionId
                            );

            if (found) {

                pass(
                        "Find all history"
                );

                System.out.println(
                        "Total history records: " +
                                allHistory.size()
                );

            } else {

                fail(
                        "Find all history"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find all history: " +
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
                    "SUBSCRIPTION HISTORY TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "SUBSCRIPTION HISTORY TEST SUITE: FAILED"
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