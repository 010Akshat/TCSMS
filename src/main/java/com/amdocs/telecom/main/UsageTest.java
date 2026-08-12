package com.amdocs.telecom.main;

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

public class UsageTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        UsageService usageService =
                new UsageServiceImpl();

        SubscriptionService subscriptionService =
                new SubscriptionServiceImpl();

        System.out.println(
                "=== USAGE TEST SUITE ==="
        );

        // ==========================================
        // FIND AN EXISTING SUBSCRIPTION DYNAMICALLY
        // ==========================================

        List<MobileSubscription> subscriptions =
                subscriptionService.findAll();

        if (subscriptions == null ||
                subscriptions.isEmpty()) {

            System.out.println(
                    "No subscriptions available for usage testing."
            );

            return;
        }

        final long subscriptionId =
                subscriptions.get(0)
                        .getSubscriptionId();

        System.out.println(
                "Using Subscription ID: " +
                        subscriptionId
        );

        // ==========================================
        // CAPTURE DATABASE STATE BEFORE TEST
        // ==========================================

        List<UsageRecord> recordsBefore =
                usageService.findAll();

        int recordCountBefore =
                recordsBefore.size();

        BigDecimal dataBefore =
                usageService.calculateTotalDataUsage();

        BigDecimal voiceBefore =
                usageService.calculateTotalVoiceUsage();

        BigDecimal smsBefore =
                usageService.calculateTotalSmsUsage();

        Map<UsageType, BigDecimal> usageByTypeBefore =
                usageService.calculateUsageByType();

        YearMonth currentMonth =
                YearMonth.from(
                        LocalDateTime.now()
                );

        Map<UsageType, BigDecimal> monthlyBefore =
                usageService.calculateMonthlyUsage(
                        currentMonth
                );

        // ==========================================
        // TEST DATA
        // ==========================================

        UsageRecord dataRecord =
                new UsageRecord(
                        0,
                        subscriptionId,
                        LocalDateTime.now(),
                        UsageType.DATA,
                        new BigDecimal("2.500"),
                        "GB",
                        new BigDecimal("50.00")
                );

        UsageRecord voiceRecord =
                new UsageRecord(
                        0,
                        subscriptionId,
                        LocalDateTime.now().minusHours(1),
                        UsageType.VOICE,
                        new BigDecimal("120.000"),
                        "MINUTES",
                        new BigDecimal("30.00")
                );

        UsageRecord smsRecord =
                new UsageRecord(
                        0,
                        subscriptionId,
                        LocalDateTime.now().minusHours(2),
                        UsageType.SMS,
                        new BigDecimal("45.000"),
                        "SMS",
                        new BigDecimal("10.00")
                );

        UsageRecord roamingRecord =
                new UsageRecord(
                        0,
                        subscriptionId,
                        LocalDateTime.now().minusHours(3),
                        UsageType.ROAMING,
                        new BigDecimal("0.750"),
                        "GB",
                        new BigDecimal("75.00")
                );

        // ==========================================
        // TEST 1: CREATE DATA
        // ==========================================

        System.out.println(
                "\n=== TEST 1: CREATE DATA USAGE ==="
        );

        try {

            usageService.save(dataRecord);

            if (dataRecord.getUsageId() > 0) {

                pass(
                        "Data usage creation"
                );

            } else {

                fail(
                        "Data usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Data usage creation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: CREATE VOICE
        // ==========================================

        System.out.println(
                "\n=== TEST 2: CREATE VOICE USAGE ==="
        );

        try {

            usageService.save(voiceRecord);

            if (voiceRecord.getUsageId() > 0) {

                pass(
                        "Voice usage creation"
                );

            } else {

                fail(
                        "Voice usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Voice usage creation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: CREATE SMS
        // ==========================================

        System.out.println(
                "\n=== TEST 3: CREATE SMS USAGE ==="
        );

        try {

            usageService.save(smsRecord);

            if (smsRecord.getUsageId() > 0) {

                pass(
                        "SMS usage creation"
                );

            } else {

                fail(
                        "SMS usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "SMS usage creation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: CREATE ROAMING
        // ==========================================

        System.out.println(
                "\n=== TEST 4: CREATE ROAMING USAGE ==="
        );

        try {

            usageService.save(roamingRecord);

            if (roamingRecord.getUsageId() > 0) {

                pass(
                        "Roaming usage creation"
                );

            } else {

                fail(
                        "Roaming usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Roaming usage creation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: FIND BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 5: FIND USAGE BY ID ==="
        );

        try {

            UsageRecord found =
                    usageService.findById(
                            dataRecord.getUsageId()
                    );

            if (found != null &&
                    found.getUsageId()
                            == dataRecord.getUsageId()) {

                pass(
                        "Find usage by ID"
                );

            } else {

                fail(
                        "Find usage by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find usage by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 6: FIND BY SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 6: FIND USAGE BY SUBSCRIPTION ==="
        );

        try {

            List<UsageRecord> usage =
                    usageService.findBySubscriptionId(
                            subscriptionId
                    );

            boolean dataFound =
                    usage.stream()
                            .anyMatch(record ->
                                    record.getUsageId()
                                            == dataRecord.getUsageId()
                            );

            boolean voiceFound =
                    usage.stream()
                            .anyMatch(record ->
                                    record.getUsageId()
                                            == voiceRecord.getUsageId()
                            );

            boolean smsFound =
                    usage.stream()
                            .anyMatch(record ->
                                    record.getUsageId()
                                            == smsRecord.getUsageId()
                            );

            boolean roamingFound =
                    usage.stream()
                            .anyMatch(record ->
                                    record.getUsageId()
                                            == roamingRecord.getUsageId()
                            );

            if (dataFound &&
                    voiceFound &&
                    smsFound &&
                    roamingFound) {

                pass(
                        "Find usage by subscription"
                );

            } else {

                fail(
                        "Find usage by subscription"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find usage by subscription: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 7: FIND ALL USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 7: FIND ALL USAGE ==="
        );

        try {

            List<UsageRecord> allUsage =
                    usageService.findAll();

            int expectedCount =
                    recordCountBefore + 4;

            if (allUsage.size()
                    == expectedCount) {

                pass(
                        "Find all usage"
                );

            } else {

                fail(
                        "Find all usage"
                );

                System.out.println(
                        "Expected: " +
                                expectedCount +
                                ", Actual: " +
                                allUsage.size()
                );
            }

        } catch (Exception e) {

            fail(
                    "Find all usage: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 8: TOTAL DATA
        // ==========================================

        System.out.println(
                "\n=== TEST 8: TOTAL DATA USAGE ==="
        );

        try {

            BigDecimal actual =
                    usageService.calculateTotalDataUsage();

            BigDecimal expected =
                    dataBefore.add(
                            new BigDecimal("2.500")
                    );

            if (actual.compareTo(
                    expected
            ) == 0) {

                pass(
                        "Total DATA calculation"
                );

            } else {

                fail(
                        "Total DATA calculation"
                );

                System.out.println(
                        "Expected: " +
                                expected +
                                ", Actual: " +
                                actual
                );
            }

        } catch (Exception e) {

            fail(
                    "Total DATA calculation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 9: TOTAL VOICE
        // ==========================================

        System.out.println(
                "\n=== TEST 9: TOTAL VOICE USAGE ==="
        );

        try {

            BigDecimal actual =
                    usageService.calculateTotalVoiceUsage();

            BigDecimal expected =
                    voiceBefore.add(
                            new BigDecimal("120.000")
                    );

            if (actual.compareTo(
                    expected
            ) == 0) {

                pass(
                        "Total VOICE calculation"
                );

            } else {

                fail(
                        "Total VOICE calculation"
                );

                System.out.println(
                        "Expected: " +
                                expected +
                                ", Actual: " +
                                actual
                );
            }

        } catch (Exception e) {

            fail(
                    "Total VOICE calculation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 10: TOTAL SMS
        // ==========================================

        System.out.println(
                "\n=== TEST 10: TOTAL SMS USAGE ==="
        );

        try {

            BigDecimal actual =
                    usageService.calculateTotalSmsUsage();

            BigDecimal expected =
                    smsBefore.add(
                            new BigDecimal("45.000")
                    );

            if (actual.compareTo(
                    expected
            ) == 0) {

                pass(
                        "Total SMS calculation"
                );

            } else {

                fail(
                        "Total SMS calculation"
                );

                System.out.println(
                        "Expected: " +
                                expected +
                                ", Actual: " +
                                actual
                );
            }

        } catch (Exception e) {

            fail(
                    "Total SMS calculation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 11: MONTHLY USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 11: MONTHLY USAGE BY TYPE ==="
        );

        try {

            Map<UsageType, BigDecimal> monthlyUsage =
                    usageService.calculateMonthlyUsage(
                            currentMonth
                    );

            boolean valid =
                    getUsageValue(
                            monthlyUsage,
                            UsageType.DATA
                    ).compareTo(
                            getUsageValue(
                                    monthlyBefore,
                                    UsageType.DATA
                            ).add(
                                    new BigDecimal("2.500")
                            )
                    ) == 0
                            &&
                            getUsageValue(
                                    monthlyUsage,
                                    UsageType.VOICE
                            ).compareTo(
                                    getUsageValue(
                                            monthlyBefore,
                                            UsageType.VOICE
                                    ).add(
                                            new BigDecimal("120.000")
                                    )
                            ) == 0
                            &&
                            getUsageValue(
                                    monthlyUsage,
                                    UsageType.SMS
                            ).compareTo(
                                    getUsageValue(
                                            monthlyBefore,
                                            UsageType.SMS
                                    ).add(
                                            new BigDecimal("45.000")
                                    )
                            ) == 0
                            &&
                            getUsageValue(
                                    monthlyUsage,
                                    UsageType.ROAMING
                            ).compareTo(
                                    getUsageValue(
                                            monthlyBefore,
                                            UsageType.ROAMING
                                    ).add(
                                            new BigDecimal("0.750")
                                    )
                            ) == 0;

            if (valid) {

                pass(
                        "Monthly usage by type"
                );

            } else {

                fail(
                        "Monthly usage by type"
                );
            }

        } catch (Exception e) {

            fail(
                    "Monthly usage by type: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 12: USAGE BY TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 12: USAGE BY TYPE ==="
        );

        try {

            Map<UsageType, BigDecimal> usageByType =
                    usageService.calculateUsageByType();

            boolean valid =
                    getUsageValue(
                            usageByType,
                            UsageType.DATA
                    ).compareTo(
                            getUsageValue(
                                    usageByTypeBefore,
                                    UsageType.DATA
                            ).add(
                                    new BigDecimal("2.500")
                            )
                    ) == 0
                            &&
                            getUsageValue(
                                    usageByType,
                                    UsageType.VOICE
                            ).compareTo(
                                    getUsageValue(
                                            usageByTypeBefore,
                                            UsageType.VOICE
                                    ).add(
                                            new BigDecimal("120.000")
                                    )
                            ) == 0
                            &&
                            getUsageValue(
                                    usageByType,
                                    UsageType.SMS
                            ).compareTo(
                                    getUsageValue(
                                            usageByTypeBefore,
                                            UsageType.SMS
                                    ).add(
                                            new BigDecimal("45.000")
                                    )
                            ) == 0
                            &&
                            getUsageValue(
                                    usageByType,
                                    UsageType.ROAMING
                            ).compareTo(
                                    getUsageValue(
                                            usageByTypeBefore,
                                            UsageType.ROAMING
                                    ).add(
                                            new BigDecimal("0.750")
                                    )
                            ) == 0;

            if (valid) {

                pass(
                        "Usage by type calculation"
                );

            } else {

                fail(
                        "Usage by type calculation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Usage by type calculation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 13: UPDATE USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 13: UPDATE USAGE ==="
        );

        try {

            dataRecord.setQuantity(
                    new BigDecimal("3.500")
            );

            dataRecord.setCharge(
                    new BigDecimal("70.00")
            );

            usageService.update(
                    dataRecord
            );

            UsageRecord updated =
                    usageService.findById(
                            dataRecord.getUsageId()
                    );

            if (updated != null &&
                    updated.getQuantity()
                            .compareTo(
                                    new BigDecimal("3.500")
                            ) == 0 &&
                    updated.getCharge()
                            .compareTo(
                                    new BigDecimal("70.00")
                            ) == 0) {

                pass(
                        "Usage update"
                );

            } else {

                fail(
                        "Usage update"
                );
            }

        } catch (Exception e) {

            fail(
                    "Usage update: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 14: DELETE ALL TEST RECORDS
        // ==========================================

        System.out.println(
                "\n=== TEST 14: DELETE TEST USAGE ==="
        );

        boolean cleanupSuccessful = true;

        long[] testUsageIds = {
                dataRecord.getUsageId(),
                voiceRecord.getUsageId(),
                smsRecord.getUsageId(),
                roamingRecord.getUsageId()
        };

        try {

            for (long usageId : testUsageIds) {

                if (usageId > 0) {

                    usageService.delete(
                            usageId
                    );
                }
            }

            for (long usageId : testUsageIds) {

                if (usageId > 0 &&
                        usageService.findById(
                                usageId
                        ) != null) {

                    cleanupSuccessful = false;
                    break;
                }
            }

            if (cleanupSuccessful) {

                pass(
                        "Test usage cleanup"
                );

            } else {

                fail(
                        "Test usage cleanup"
                );
            }

        } catch (Exception e) {

            fail(
                    "Test usage cleanup: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // FINAL RESULT
        // ==========================================

        try {

            List<UsageRecord> finalRecords =
                    usageService.findAll();

            System.out.println(
                    "\nFinal usage record count: " +
                            finalRecords.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Final record check failed: " +
                            e.getMessage()
            );
        }

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
                    "USAGE TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "USAGE TEST SUITE: FAILED"
            );
        }
    }

    private static BigDecimal getUsageValue(
            Map<UsageType, BigDecimal> usageMap,
            UsageType usageType) {

        BigDecimal value =
                usageMap.get(
                        usageType
                );

        return value != null
                ? value
                : BigDecimal.ZERO;
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