package com.amdocs.telecom.main;


import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.enums.UsageType;

import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminUsageService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminUsageServiceImpl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;


public class AdminUsageTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminUsageService adminUsageService =
                new AdminUsageServiceImpl();



        System.out.println(
                "=== ADMIN USAGE TEST SUITE ==="
        );


        Admin admin = null;



        // ==========================================
        // TEST 1: ADMIN LOGIN
        // ==========================================

        System.out.println(
                "\n=== TEST 1: ADMIN LOGIN ==="
        );


        try {


            admin =
                    authenticationService.login(
                            "admin",
                            "admin123"
                    );


            if(admin != null &&
                    admin.getAdminId() > 0) {


                pass(
                        "Admin login"
                );


                System.out.println(
                        "Welcome " +
                                admin.getFirstName()
                );


            } else {


                fail(
                        "Admin login"
                );
            }


        } catch(Exception e) {


            fail(
                    "Admin login: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 2: VIEW ALL USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 2: VIEW ALL USAGE ==="
        );


        List<UsageRecord> usageRecords = null;


        try {


            usageRecords =
                    adminUsageService
                            .viewAllUsage(admin);



            if(usageRecords != null &&
                    !usageRecords.isEmpty()) {


                pass(
                        "View all usage"
                );


                System.out.println(
                        "Total usage records: " +
                                usageRecords.size()
                );


            } else {


                fail(
                        "View all usage"
                );
            }


        } catch(Exception e) {


            fail(
                    "View all usage: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 3: FIND USAGE BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND USAGE BY ID ==="
        );


        long usageId = 0;


        try {


            UsageRecord first =
                    usageRecords.get(0);


            usageId =
                    first.getUsageId();



            UsageRecord found =
                    adminUsageService.findUsageById(
                            admin,
                            usageId
                    );



            if(found != null &&
                    found.getUsageId()
                            == usageId) {


                pass(
                        "Find usage by ID"
                );


            } else {


                fail(
                        "Find usage by ID"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find usage by ID: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 4: VIEW USAGE BY SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 4: VIEW USAGE BY SUBSCRIPTION ==="
        );


        try {


            UsageRecord first =
                    usageRecords.get(0);



            List<UsageRecord> result =
                    adminUsageService
                            .viewUsageBySubscription(
                                    admin,
                                    first.getSubscriptionId()
                            );



            if(result != null &&
                    !result.isEmpty()) {


                pass(
                        "View usage by subscription"
                );


            } else {


                fail(
                        "View usage by subscription"
                );
            }


        } catch(Exception e) {


            fail(
                    "View usage by subscription: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 5: VIEW USAGE BY TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 5: VIEW USAGE BY TYPE ==="
        );


        try {


            Map<UsageType, BigDecimal> usageByType =
                    adminUsageService
                            .viewUsageByType(
                                    admin
                            );


            if(usageByType != null) {


                pass(
                        "View usage by type"
                );


                System.out.println(
                        usageByType
                );


            } else {


                fail(
                        "View usage by type"
                );
            }


        } catch(Exception e) {


            fail(
                    "View usage by type: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 6: VIEW MONTHLY USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 6: VIEW MONTHLY USAGE ==="
        );


        try {


            Map<UsageType, BigDecimal> monthlyUsage =
                    adminUsageService
                            .viewMonthlyUsage(
                                    admin,
                                    YearMonth.now()
                            );


            if(monthlyUsage != null) {


                pass(
                        "View monthly usage"
                );


            } else {


                fail(
                        "View monthly usage"
                );
            }


        } catch(Exception e) {


            fail(
                    "View monthly usage: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 7: TOTAL DATA USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 7: TOTAL DATA USAGE ==="
        );


        try {


            BigDecimal data =
                    adminUsageService
                            .viewTotalDataUsage(
                                    admin
                            );


            if(data != null) {


                pass(
                        "Total data usage"
                );


                System.out.println(
                        "DATA: " + data
                );


            } else {


                fail(
                        "Total data usage"
                );
            }


        } catch(Exception e) {


            fail(
                    "Total data usage: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 8: TOTAL VOICE USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 8: TOTAL VOICE USAGE ==="
        );


        try {


            BigDecimal voice =
                    adminUsageService
                            .viewTotalVoiceUsage(
                                    admin
                            );


            if(voice != null) {


                pass(
                        "Total voice usage"
                );


            } else {


                fail(
                        "Total voice usage"
                );
            }


        } catch(Exception e) {


            fail(
                    "Total voice usage: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 9: TOTAL SMS USAGE
        // ==========================================

        System.out.println(
                "\n=== TEST 9: TOTAL SMS USAGE ==="
        );


        try {


            BigDecimal sms =
                    adminUsageService
                            .viewTotalSmsUsage(
                                    admin
                            );


            if(sms != null) {


                pass(
                        "Total SMS usage"
                );


            } else {


                fail(
                        "Total SMS usage"
                );
            }


        } catch(Exception e) {


            fail(
                    "Total SMS usage: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 10: NULL ADMIN REJECTION
        // ==========================================

        System.out.println(
                "\n=== TEST 10: NULL ADMIN REJECTION ==="
        );


        try {


            adminUsageService.viewAllUsage(
                    null
            );


            fail(
                    "Null admin rejection"
            );


        } catch(SecurityException e) {


            pass(
                    "Null admin rejection"
            );


        } catch(Exception e) {


            fail(
                    "Null admin rejection: " +
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


        if(failed == 0) {

            System.out.println(
                    "ADMIN USAGE TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "ADMIN USAGE TEST SUITE: FAILED"
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