package com.amdocs.telecom.main;

import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.enums.UsageType;
import com.amdocs.telecom.service.UsageService;
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

        System.out.println(
                "=== USAGE TEST SUITE ==="
        );

        long subscriptionId = 1;

        /*
         * Capture database state BEFORE this test run.
         * This makes the test independent of existing records.
         */
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

        BigDecimal dataBeforeForMonth =
                getUsageValue(
                        monthlyBefore,
                        UsageType.DATA
                );

        BigDecimal voiceBeforeForMonth =
                getUsageValue(
                        monthlyBefore,
                        UsageType.VOICE
                );

        BigDecimal smsBeforeForMonth =
                getUsageValue(
                        monthlyBefore,
                        UsageType.SMS
                );

        BigDecimal roamingBeforeForMonth =
                getUsageValue(
                        monthlyBefore,
                        UsageType.ROAMING
                );

        // ==========================================
        // TEST 1: CREATE DATA USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 1: CREATE DATA USAGE ==="
        );

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

        try {

            usageService.save(dataRecord);

            if (dataRecord.getUsageId() > 0) {

                pass(
                        "Data usage creation"
                );

                printUsageRecord(
                        dataRecord
                );

            } else {

                fail(
                        "Data usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Data usage creation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: CREATE VOICE USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 2: CREATE VOICE USAGE ==="
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

        try {

            usageService.save(voiceRecord);

            if (voiceRecord.getUsageId() > 0) {

                pass(
                        "Voice usage creation"
                );

                printUsageRecord(
                        voiceRecord
                );

            } else {

                fail(
                        "Voice usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Voice usage creation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: CREATE SMS USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 3: CREATE SMS USAGE ==="
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

        try {

            usageService.save(smsRecord);

            if (smsRecord.getUsageId() > 0) {

                pass(
                        "SMS usage creation"
                );

                printUsageRecord(
                        smsRecord
                );

            } else {

                fail(
                        "SMS usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "SMS usage creation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: CREATE ROAMING USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 4: CREATE ROAMING USAGE ==="
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

        try {

            usageService.save(roamingRecord);

            if (roamingRecord.getUsageId() > 0) {

                pass(
                        "Roaming usage creation"
                );

                printUsageRecord(
                        roamingRecord
                );

            } else {

                fail(
                        "Roaming usage creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Roaming usage creation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: FIND BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 5: FIND USAGE BY ID ==="
        );

        try {

            UsageRecord foundRecord =
                    usageService.findById(
                            dataRecord.getUsageId()
                    );

            if (foundRecord != null &&
                    foundRecord.getUsageId()
                            == dataRecord.getUsageId()) {

                pass(
                        "Find usage by ID"
                );

                printUsageRecord(
                        foundRecord
                );

            } else {

                fail(
                        "Find usage by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find usage by ID: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 6: FIND BY SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 6: FIND USAGE BY SUBSCRIPTION ==="
        );

        try {

            List<UsageRecord> subscriptionUsage =
                    usageService.findBySubscriptionId(
                            subscriptionId
                    );

            int expectedIncrease = 4;

            /*
             * We captured the initial count before
             * creating the four records.
             */
            int expectedCount =
                    recordsBefore.stream()
                            .filter(record ->
                                    record.getSubscriptionId()
                                            == subscriptionId
                            )
                            .count() == 0
                            ? 4
                            : (int) recordsBefore.stream()
                            .filter(record ->
                                    record.getSubscriptionId()
                                            == subscriptionId
                            )
                            .count()
                            + expectedIncrease;

            if (subscriptionUsage.size()
                    >= expectedCount) {

                pass(
                        "Find usage by subscription"
                );

                System.out.println(
                        "Records found: " +
                                subscriptionUsage.size()
                );

            } else {

                fail(
                        "Find usage by subscription"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find usage by subscription: "
                            + e.getMessage()
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

                System.out.println(
                        "Total usage records: " +
                                allUsage.size()
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
                    "Find all usage: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 8: TOTAL DATA USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 8: TOTAL DATA USAGE ==="
        );

        try {

            BigDecimal totalData =
                    usageService.calculateTotalDataUsage();

            BigDecimal expected =
                    dataBefore.add(
                            new BigDecimal("2.500")
                    );

            if (totalData.compareTo(expected)
                    == 0) {

                pass(
                        "Total DATA calculation"
                );

                System.out.println(
                        "Total DATA usage: " +
                                totalData
                );

            } else {

                fail(
                        "Total DATA calculation"
                );

                System.out.println(
                        "Expected: " +
                                expected +
                                ", Actual: " +
                                totalData
                );
            }

        } catch (Exception e) {

            fail(
                    "Total DATA calculation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 9: TOTAL VOICE USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 9: TOTAL VOICE USAGE ==="
        );

        try {

            BigDecimal totalVoice =
                    usageService.calculateTotalVoiceUsage();

            BigDecimal expected =
                    voiceBefore.add(
                            new BigDecimal("120.000")
                    );

            if (totalVoice.compareTo(expected)
                    == 0) {

                pass(
                        "Total VOICE calculation"
                );

                System.out.println(
                        "Total VOICE usage: " +
                                totalVoice
                );

            } else {

                fail(
                        "Total VOICE calculation"
                );

                System.out.println(
                        "Expected: " +
                                expected +
                                ", Actual: " +
                                totalVoice
                );
            }

        } catch (Exception e) {

            fail(
                    "Total VOICE calculation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 10: TOTAL SMS USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 10: TOTAL SMS USAGE ==="
        );

        try {

            BigDecimal totalSms =
                    usageService.calculateTotalSmsUsage();

            BigDecimal expected =
                    smsBefore.add(
                            new BigDecimal("45.000")
                    );

            if (totalSms.compareTo(expected)
                    == 0) {

                pass(
                        "Total SMS calculation"
                );

                System.out.println(
                        "Total SMS usage: " +
                                totalSms
                );

            } else {

                fail(
                        "Total SMS calculation"
                );

                System.out.println(
                        "Expected: " +
                                expected +
                                ", Actual: " +
                                totalSms
                );
            }

        } catch (Exception e) {

            fail(
                    "Total SMS calculation: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 11: MONTHLY USAGE BY TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 11: MONTHLY USAGE BY TYPE ==="
        );

        try {

            Map<UsageType, BigDecimal> monthlyUsage =
                    usageService.calculateMonthlyUsage(
                            currentMonth
                    );

            BigDecimal expectedData =
                    dataBeforeForMonth.add(
                            new BigDecimal("2.500")
                    );

            BigDecimal expectedVoice =
                    voiceBeforeForMonth.add(
                            new BigDecimal("120.000")
                    );

            BigDecimal expectedSms =
                    smsBeforeForMonth.add(
                            new BigDecimal("45.000")
                    );

            BigDecimal expectedRoaming =
                    roamingBeforeForMonth.add(
                            new BigDecimal("0.750")
                    );

            BigDecimal actualData =
                    getUsageValue(
                            monthlyUsage,
                            UsageType.DATA
                    );

            BigDecimal actualVoice =
                    getUsageValue(
                            monthlyUsage,
                            UsageType.VOICE
                    );

            BigDecimal actualSms =
                    getUsageValue(
                            monthlyUsage,
                            UsageType.SMS
                    );

            BigDecimal actualRoaming =
                    getUsageValue(
                            monthlyUsage,
                            UsageType.ROAMING
                    );

            boolean valid =
                    actualData.compareTo(
                            expectedData
                    ) == 0
                            &&
                            actualVoice.compareTo(
                                    expectedVoice
                            ) == 0
                            &&
                            actualSms.compareTo(
                                    expectedSms
                            ) == 0
                            &&
                            actualRoaming.compareTo(
                                    expectedRoaming
                            ) == 0;

            if (valid) {

                pass(
                        "Monthly usage by type"
                );

                printUsageMap(
                        monthlyUsage
                );

            } else {

                fail(
                        "Monthly usage by type"
                );

                System.out.println(
                        "Expected:"
                );

                System.out.println(
                        "DATA = " +
                                expectedData
                );

                System.out.println(
                        "VOICE = " +
                                expectedVoice
                );

                System.out.println(
                        "SMS = " +
                                expectedSms
                );

                System.out.println(
                        "ROAMING = " +
                                expectedRoaming
                );

                System.out.println(
                        "Actual:"
                );

                printUsageMap(
                        monthlyUsage
                );
            }

        } catch (Exception e) {

            fail(
                    "Monthly usage by type: "
                            + e.getMessage()
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

            BigDecimal expectedData =
                    getUsageValue(
                            usageByTypeBefore,
                            UsageType.DATA
                    ).add(
                            new BigDecimal("2.500")
                    );

            BigDecimal expectedVoice =
                    getUsageValue(
                            usageByTypeBefore,
                            UsageType.VOICE
                    ).add(
                            new BigDecimal("120.000")
                    );

            BigDecimal expectedSms =
                    getUsageValue(
                            usageByTypeBefore,
                            UsageType.SMS
                    ).add(
                            new BigDecimal("45.000")
                    );

            BigDecimal expectedRoaming =
                    getUsageValue(
                            usageByTypeBefore,
                            UsageType.ROAMING
                    ).add(
                            new BigDecimal("0.750")
                    );

            boolean valid =
                    getUsageValue(
                            usageByType,
                            UsageType.DATA
                    ).compareTo(expectedData) == 0
                            &&
                            getUsageValue(
                                    usageByType,
                                    UsageType.VOICE
                            ).compareTo(expectedVoice) == 0
                            &&
                            getUsageValue(
                                    usageByType,
                                    UsageType.SMS
                            ).compareTo(expectedSms) == 0
                            &&
                            getUsageValue(
                                    usageByType,
                                    UsageType.ROAMING
                            ).compareTo(expectedRoaming) == 0;

            if (valid) {

                pass(
                        "Usage by type calculation"
                );

                printUsageMap(
                        usageByType
                );

            } else {

                fail(
                        "Usage by type calculation"
                );

                printUsageMap(
                        usageByType
                );
            }

        } catch (Exception e) {

            fail(
                    "Usage by type calculation: "
                            + e.getMessage()
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

            UsageRecord updatedRecord =
                    usageService.findById(
                            dataRecord.getUsageId()
                    );

            if (updatedRecord != null &&
                    updatedRecord.getQuantity()
                            .compareTo(
                                    new BigDecimal("3.500")
                            ) == 0) {

                pass(
                        "Usage update"
                );

                System.out.println(
                        "Updated quantity: " +
                                updatedRecord.getQuantity()
                );

                System.out.println(
                        "Updated charge: " +
                                updatedRecord.getCharge()
                );

            } else {

                fail(
                        "Usage update"
                );
            }

        } catch (Exception e) {

            fail(
                    "Usage update: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // TEST 14: DELETE USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 14: DELETE USAGE ==="
        );

        try {

            long usageId =
                    smsRecord.getUsageId();

            usageService.delete(
                    usageId
            );

            UsageRecord deletedRecord =
                    usageService.findById(
                            usageId
                    );

            if (deletedRecord == null) {

                pass(
                        "Usage deletion"
                );

            } else {

                fail(
                        "Usage deletion"
                );
            }

        } catch (Exception e) {

            fail(
                    "Usage deletion: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // FINAL RESULT
        // ==========================================

        try {

            List<UsageRecord> finalRecords =
                    usageService.findAll();

            System.out.println(
                    "\nFinal usage records in database: " +
                            finalRecords.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Final record check failed: "
                            + e.getMessage()
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
                usageMap.get(usageType);

        return value != null
                ? value
                : BigDecimal.ZERO;
    }

    private static void printUsageMap(
            Map<UsageType, BigDecimal> usageMap) {

        for (Map.Entry<UsageType, BigDecimal> entry
                : usageMap.entrySet()) {

            System.out.println(
                    entry.getKey() +
                            " -> " +
                            entry.getValue()
            );
        }
    }

    private static void pass(String testName) {

        passed++;

        System.out.println(
                testName +
                        ": PASSED"
        );
    }

    private static void fail(String testName) {

        failed++;

        System.out.println(
                testName +
                        ": FAILED"
        );
    }

    private static void printUsageRecord(
            UsageRecord usageRecord) {

        System.out.println(
                "Usage ID: " +
                        usageRecord.getUsageId()
        );

        System.out.println(
                "Subscription ID: " +
                        usageRecord.getSubscriptionId()
        );

        System.out.println(
                "Usage Date: " +
                        usageRecord.getUsageDate()
        );

        System.out.println(
                "Usage Type: " +
                        usageRecord.getUsageType()
        );

        System.out.println(
                "Quantity: " +
                        usageRecord.getQuantity()
        );

        System.out.println(
                "Unit: " +
                        usageRecord.getUnit()
        );

        System.out.println(
                "Charge: " +
                        usageRecord.getCharge()
        );
    }
}