package com.amdocs.telecom.main;

import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.service.PlanService;
import com.amdocs.telecom.service.impl.PlanServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PlanTest {

    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {

        PlanService planService =
                new PlanServiceImpl();


        System.out.println(
                "=== PLAN MODULE TEST ==="
        );


        TelecomPlan plan =
                planService.findById(1);


        // ==========================================
        // TEST 1: FIND PLAN BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 1: FIND PLAN BY ID ==="
        );

        if (plan != null) {

            pass(
                    "Find plan by ID"
            );

            System.out.println(
                    "Plan: " +
                            plan.getPlanCode() +
                            " - " +
                            plan.getPlanName()
            );

        } else {

            fail(
                    "Find plan by ID"
            );
        }



        // ==========================================
        // TEST 2: FIND PLAN BY CODE
        // ==========================================

        System.out.println(
                "\n=== TEST 2: FIND PLAN BY CODE ==="
        );


        if (plan != null) {

            TelecomPlan planByCode =
                    planService.findByCode(
                            plan.getPlanCode()
                    );


            if (planByCode != null &&
                    planByCode.getPlanId()
                            == plan.getPlanId()) {

                pass(
                        "Find plan by code"
                );

            } else {

                fail(
                        "Find plan by code"
                );
            }

        } else {

            fail(
                    "Find plan by code"
            );
        }



        // ==========================================
        // TEST 3: FIND ALL PLANS
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND ALL PLANS ==="
        );


        List<TelecomPlan> allPlans =
                planService.findAll();


        if (allPlans != null &&
                !allPlans.isEmpty()) {

            pass(
                    "Find all plans"
            );

            System.out.println(
                    "Total plans: " +
                            allPlans.size()
            );

        } else {

            fail(
                    "Find all plans"
            );
        }



        // ==========================================
        // TEST 4: FIND ACTIVE PLANS
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND ACTIVE PLANS ==="
        );


        List<TelecomPlan> activePlans =
                planService.findActivePlans();


        boolean activeValid =
                activePlans != null &&
                        activePlans.stream()
                                .allMatch(p ->
                                        p.getStatus()
                                                .name()
                                                .equals(
                                                        "ACTIVE"
                                                )
                                );


        if (activeValid) {

            pass(
                    "Find active plans"
            );

            System.out.println(
                    "Active plans: " +
                            activePlans.size()
            );

        } else {

            fail(
                    "Find active plans"
            );
        }



        // ==========================================
        // TEST 5: FILTER BY PRICE
        // ==========================================

        System.out.println(
                "\n=== TEST 5: FILTER BY PRICE ==="
        );


        BigDecimal priceLimit =
                new BigDecimal("700.00");


        List<TelecomPlan> priceFiltered =
                planService.filterByPrice(
                        priceLimit
                );


        boolean priceValid =
                priceFiltered.stream()
                        .allMatch(p ->
                                p.getMonthlyRental()
                                        .compareTo(
                                                priceLimit
                                        ) <= 0
                        );


        if (priceValid) {

            pass(
                    "Filter by price"
            );

        } else {

            fail(
                    "Filter by price"
            );
        }



        // ==========================================
        // TEST 6: FILTER BY DATA
        // ==========================================

        System.out.println(
                "\n=== TEST 6: FILTER BY DATA ==="
        );


        BigDecimal dataLimit =
                new BigDecimal("50.00");


        List<TelecomPlan> dataFiltered =
                planService.filterByDataAllowance(
                        dataLimit
                );


        boolean dataValid =
                dataFiltered.stream()
                        .allMatch(p ->
                                p.getDataAllowanceGB()
                                        .compareTo(
                                                dataLimit
                                        ) >= 0
                        );


        if (dataValid) {

            pass(
                    "Filter by data allowance"
            );

        } else {

            fail(
                    "Filter by data allowance"
            );
        }



        // ==========================================
        // TEST 7: SORT BY PRICE
        // ==========================================

        System.out.println(
                "\n=== TEST 7: SORT BY PRICE ==="
        );


        List<TelecomPlan> sortedPlans =
                planService.sortByPrice();


        boolean sortedValid = true;


        for(int i = 0;
            i < sortedPlans.size()-1;
            i++) {


            if(sortedPlans.get(i)
                    .getMonthlyRental()
                    .compareTo(
                            sortedPlans.get(i+1)
                                    .getMonthlyRental()
                    ) > 0) {

                sortedValid = false;
                break;
            }
        }


        if(sortedValid) {

            pass(
                    "Sort by price"
            );

        } else {

            fail(
                    "Sort by price"
            );
        }



        // ==========================================
        // TEST 8: COMPARE PLANS
        // ==========================================

        System.out.println(
                "\n=== TEST 8: COMPARE PLANS ==="
        );


        if(allPlans.size() >= 2) {


            List<Long> ids =
                    Arrays.asList(
                            allPlans.get(0)
                                    .getPlanId(),

                            allPlans.get(1)
                                    .getPlanId()
                    );


            List<TelecomPlan> compared =
                    planService.comparePlans(
                            ids
                    );


            if(compared.size()
                    == ids.size()) {

                pass(
                        "Compare plans"
                );

            } else {

                fail(
                        "Compare plans"
                );
            }

        } else {

            fail(
                    "Compare plans"
            );
        }



        // ==========================================
        // FINAL RESULT
        // ==========================================

        System.out.println(
                "\n================================"
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
                "================================"
        );


        if(failed == 0) {

            System.out.println(
                    "PLAN MODULE TEST: PASSED"
            );

        } else {

            System.out.println(
                    "PLAN MODULE TEST: FAILED"
            );
        }
    }



    private static void pass(
            String name) {

        passed++;

        System.out.println(
                name +
                        ": PASSED"
        );
    }



    private static void fail(
            String name) {

        failed++;

        System.out.println(
                name +
                        ": FAILED"
        );
    }
}