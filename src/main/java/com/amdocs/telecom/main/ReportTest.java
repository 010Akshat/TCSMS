package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.UsageType;
import com.amdocs.telecom.service.ReportService;
import com.amdocs.telecom.service.impl.ReportServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        ReportService reportService =
                new ReportServiceImpl();


        System.out.println(
                "=== REPORT TEST SUITE ==="
        );


        // ==========================================
        // TEST 1: CUSTOMERS BY CITY
        // ==========================================

        System.out.println(
                "\n=== TEST 1: CUSTOMERS BY CITY ==="
        );

        try {

            Map<String, Long> cityReport =
                    reportService.getCustomersByCity();


            if(cityReport != null &&
                    !cityReport.isEmpty()) {


                pass(
                        "Customers by city"
                );


                cityReport.forEach(
                        (city,count) ->
                                System.out.println(
                                        city +
                                                " -> " +
                                                count
                                )
                );

            } else {

                fail(
                        "Customers by city"
                );
            }


        } catch(Exception e) {

            fail(
                    "Customers by city: "
                            + e.getMessage()
            );
        }



        // ==========================================
        // TEST 2: PLAN PRICE RANGE
        // ==========================================

        System.out.println(
                "\n=== TEST 2: PLANS WITHIN PRICE RANGE ==="
        );


        try {


            List<TelecomPlan> plans =
                    reportService.getPlansWithinPriceRange(
                            new BigDecimal("100"),
                            new BigDecimal("1000")
                    );


            if(plans != null &&
                    !plans.isEmpty()) {


                pass(
                        "Plans within price range"
                );


                plans.forEach(plan ->
                        System.out.println(
                                plan.getPlanName()
                                        +
                                        " -> "
                                        +
                                        plan.getMonthlyRental()
                        )
                );


            } else {

                fail(
                        "Plans within price range"
                );
            }


        } catch(Exception e) {

            fail(
                    "Plans within price range: "
                            + e.getMessage()
            );
        }



        // ==========================================
        // TEST 3: MOST SUBSCRIBED PLANS
        // ==========================================

        System.out.println(
                "\n=== TEST 3: MOST SUBSCRIBED PLANS ==="
        );


        try {


            Map<Long,Long> planReport =
                    reportService.getMostSubscribedPlans();


            if(planReport != null &&
                    !planReport.isEmpty()) {


                pass(
                        "Most subscribed plans"
                );


                planReport.forEach(
                        (plan,count) ->
                                System.out.println(
                                        "Plan "
                                                +
                                                plan
                                                +
                                                " -> "
                                                +
                                                count
                                )
                );


            } else {

                fail(
                        "Most subscribed plans"
                );
            }


        } catch(Exception e) {

            fail(
                    "Most subscribed plans: "
                            + e.getMessage()
            );
        }



        // ==========================================
        // TEST 4: MONTHLY REVENUE
        // ==========================================

        System.out.println(
                "\n=== TEST 4: MONTHLY REVENUE ==="
        );


        try {


            Map<LocalDate,BigDecimal> revenue =
                    reportService.getMonthlyRevenue();


            if(revenue != null &&
                    !revenue.isEmpty()) {


                pass(
                        "Monthly revenue"
                );


                revenue.forEach(
                        (month,amount) ->
                                System.out.println(
                                        month
                                                +
                                                " -> "
                                                +
                                                amount
                                )
                );


            } else {

                fail(
                        "Monthly revenue"
                );
            }


        } catch(Exception e) {

            fail(
                    "Monthly revenue: "
                            + e.getMessage()
            );
        }




        // ==========================================
        // TEST 5: USAGE BY TYPE
        // ==========================================

        System.out.println(
                "\n=== TEST 5: USAGE BY TYPE ==="
        );


        try {


            Map<UsageType,BigDecimal> usage =
                    reportService.getUsageByType();


            if(usage != null &&
                    !usage.isEmpty()) {


                pass(
                        "Usage by type"
                );


                usage.forEach(
                        (type,value) ->
                                System.out.println(
                                        type
                                                +
                                                " -> "
                                                +
                                                value
                                )
                );


            } else {

                fail(
                        "Usage by type"
                );
            }


        } catch(Exception e) {

            fail(
                    "Usage by type: "
                            + e.getMessage()
            );
        }




        // ==========================================
        // TEST 6: UNPAID BILL CUSTOMERS
        // ==========================================

        System.out.println(
                "\n=== TEST 6: CUSTOMERS WITH UNPAID BILLS ==="
        );


        try {


            List<Customer> customers =
                    reportService
                            .getCustomersWithUnpaidBills();


            if(customers != null) {


                pass(
                        "Customers with unpaid bills"
                );


                customers.forEach(customer ->
                        System.out.println(
                                customer.getFirstName()
                                        +
                                        " "
                                        +
                                        customer.getLastName()
                        )
                );


            } else {

                fail(
                        "Customers with unpaid bills"
                );
            }


        } catch(Exception e) {

            fail(
                    "Customers with unpaid bills: "
                            + e.getMessage()
            );
        }




        // ==========================================
        // TEST 7: HIGHEST CONSUMING CUSTOMERS
        // ==========================================

        System.out.println(
                "\n=== TEST 7: HIGHEST CONSUMING CUSTOMERS ==="
        );


        try {


            List<Customer> customers =
                    reportService
                            .getHighestConsumingCustomers();


            if(customers != null) {


                pass(
                        "Highest consuming customers"
                );


                customers.forEach(customer ->
                        System.out.println(
                                customer.getFirstName()
                                        +
                                        " "
                                        +
                                        customer.getLastName()
                        )
                );


            } else {

                fail(
                        "Highest consuming customers"
                );
            }


        } catch(Exception e) {

            fail(
                    "Highest consuming customers: "
                            + e.getMessage()
            );
        }





        // ==========================================
        // TEST 8: AVERAGE REVENUE
        // ==========================================

        System.out.println(
                "\n=== TEST 8: AVERAGE MONTHLY REVENUE ==="
        );


        try {


            BigDecimal average =
                    reportService
                            .getAverageMonthlyRevenuePerCustomer();


            if(average != null) {


                pass(
                        "Average monthly revenue"
                );


                System.out.println(
                        "Average: "
                                +
                                average
                );


            } else {

                fail(
                        "Average monthly revenue"
                );
            }


        } catch(Exception e) {

            fail(
                    "Average monthly revenue: "
                            + e.getMessage()
            );
        }




        // ==========================================
        // TEST 9: MULTITHREADED DASHBOARD
        // ==========================================

        System.out.println(
                "\n=== TEST 9: DASHBOARD REPORT ==="
        );


        try {


            Map<String,Object> dashboard =
                    reportService
                            .generateDashboardReports();


            if(dashboard != null &&
                    dashboard.size() == 4) {


                pass(
                        "Dashboard multithreading"
                );


                dashboard.forEach(
                        (key,value) ->
                                System.out.println(
                                        key
                                )
                );


            } else {

                fail(
                        "Dashboard multithreading"
                );
            }


        } catch(Exception e) {

            fail(
                    "Dashboard multithreading: "
                            + e.getMessage()
            );
        }





        System.out.println(
                "\n=========================================="
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
                "=========================================="
        );


        if(failed == 0) {

            System.out.println(
                    "REPORT TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "REPORT TEST SUITE: FAILED"
            );
        }

    }



    private static void pass(String name) {

        passed++;

        System.out.println(
                name +
                        ": PASSED"
        );
    }



    private static void fail(String name) {

        failed++;

        System.out.println(
                name +
                        ": FAILED"
        );
    }

}