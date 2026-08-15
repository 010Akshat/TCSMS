package com.amdocs.telecom.main;


import com.amdocs.telecom.model.Admin;

import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminReportService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminReportServiceImpl;


import java.io.File;
import java.math.BigDecimal;



public class AdminReportTest {


    private static int passed = 0;
    private static int failed = 0;



    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminReportService reportService =
                new AdminReportServiceImpl();



        System.out.println(
                "=== ADMIN REPORT TEST SUITE ==="
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


            } else {


                fail(
                        "Admin login"
                );
            }


        } catch(Exception e) {


            fail(
                    "Admin login: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 2: REVENUE REPORT
        // ==========================================


        System.out.println(
                "\n=== TEST 2: REVENUE REPORT ==="
        );


        String revenuePath = null;


        try {


            revenuePath =
                    reportService
                            .generateRevenueReport(
                                    admin
                            );


            if(revenuePath != null) {


                pass(
                        "Revenue report generation"
                );


                System.out.println(
                        revenuePath
                );


            } else {


                fail(
                        "Revenue report generation"
                );
            }


        } catch(Exception e) {


            fail(
                    "Revenue report: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 3: USAGE REPORT
        // ==========================================


        System.out.println(
                "\n=== TEST 3: USAGE REPORT ==="
        );


        try {


            String path =
                    reportService
                            .generateUsageReport(
                                    admin
                            );


            if(path != null) {


                pass(
                        "Usage report generation"
                );


            } else {


                fail(
                        "Usage report generation"
                );
            }


        } catch(Exception e) {


            fail(
                    "Usage report: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 4: CUSTOMER REPORT
        // ==========================================


        System.out.println(
                "\n=== TEST 4: CUSTOMER REPORT ==="
        );


        try {


            String path =
                    reportService
                            .generateCustomerReport(
                                    admin
                            );


            if(path != null) {


                pass(
                        "Customer report generation"
                );


            } else {


                fail(
                        "Customer report generation"
                );
            }


        } catch(Exception e) {


            fail(
                    "Customer report: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 5: DASHBOARD REPORT
        // ==========================================


        System.out.println(
                "\n=== TEST 5: DASHBOARD REPORT ==="
        );


        try {


            String path =
                    reportService
                            .generateDashboardReport(
                                    admin
                            );


            if(path != null) {


                pass(
                        "Dashboard report generation"
                );


            } else {


                fail(
                        "Dashboard report generation"
                );
            }


        } catch(Exception e) {


            fail(
                    "Dashboard report: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 6: UNPAID CUSTOMER REPORT
        // ==========================================


        System.out.println(
                "\n=== TEST 6: UNPAID CUSTOMER REPORT ==="
        );


        try {


            String path =
                    reportService
                            .generateUnpaidCustomerReport(
                                    admin
                            );


            if(path != null) {


                pass(
                        "Unpaid customer report"
                );


            } else {


                fail(
                        "Unpaid customer report"
                );
            }


        } catch(Exception e) {


            fail(
                    "Unpaid customer report: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 7: PLAN REPORT
        // ==========================================


        System.out.println(
                "\n=== TEST 7: PLAN REPORT ==="
        );


        try {


            String path =
                    reportService
                            .generatePlanReport(
                                    admin,
                                    new BigDecimal("0"),
                                    new BigDecimal("10000")
                            );


            if(path != null) {


                pass(
                        "Plan report"
                );


            } else {


                fail(
                        "Plan report"
                );
            }


        } catch(Exception e) {


            fail(
                    "Plan report: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 8: FILE EXISTENCE CHECK
        // ==========================================


        System.out.println(
                "\n=== TEST 8: PDF FILE CHECK ==="
        );


        try {


            File file =
                    new File(
                            revenuePath
                    );


            if(file.exists()
                    &&
                    file.length() > 0) {


                pass(
                        "PDF file exists"
                );


                System.out.println(
                        "Size: "
                                +
                                file.length()
                                +
                                " bytes"
                );


            } else {


                fail(
                        "PDF file exists"
                );
            }


        } catch(Exception e) {


            fail(
                    "PDF file check: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 9: NULL ADMIN
        // ==========================================


        System.out.println(
                "\n=== TEST 9: NULL ADMIN REJECTION ==="
        );


        try {


            reportService
                    .generateRevenueReport(
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
                    "Null admin rejection: "
                            +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 10: MULTIPLE REPORTS
        // ==========================================


        System.out.println(
                "\n=== TEST 10: MULTIPLE REPORT GENERATION ==="
        );


        try {


            reportService
                    .generateRevenueReport(admin);


            reportService
                    .generateUsageReport(admin);


            reportService
                    .generateDashboardReport(admin);


            pass(
                    "Multiple report generation"
            );


        } catch(Exception e) {


            fail(
                    "Multiple report generation: "
                            +
                            e.getMessage()
            );
        }





        System.out.println(
                "\n=============================="
        );


        System.out.println(
                "TOTAL PASSED: "
                        +
                        passed
        );


        System.out.println(
                "TOTAL FAILED: "
                        +
                        failed
        );


        System.out.println(
                "=============================="
        );


    }



    private static void pass(
            String name) {


        passed++;


        System.out.println(
                name
                        +
                        ": PASSED"
        );
    }



    private static void fail(
            String name) {


        failed++;


        System.out.println(
                name
                        +
                        ": FAILED"
        );
    }

}