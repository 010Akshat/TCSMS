package com.amdocs.telecom.main;

import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.service.PlanService;
import com.amdocs.telecom.service.impl.PlanServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PlanTest {

    public static void main(String[] args) {

        PlanService planService =
                new PlanServiceImpl();

        System.out.println(
                "=== PLAN MODULE TEST ==="
        );


        // ==========================================
        // 1. FIND PLAN BY ID
        // ==========================================

        TelecomPlan plan =
                planService.findById(1);

        if (plan != null) {

            System.out.println(
                    "Find by ID: PASSED"
            );

            System.out.println(
                    "Plan: " +
                            plan.getPlanCode() +
                            " - " +
                            plan.getPlanName()
            );

        } else {

            System.out.println(
                    "Find by ID: FAILED"
            );
        }


        // ==========================================
        // 2. FIND PLAN BY CODE
        // ==========================================

        TelecomPlan planByCode =
                planService.findByCode(
                        "PLAN-102"
                );

        if (planByCode != null) {

            System.out.println(
                    "Find by code: PASSED"
            );

        } else {

            System.out.println(
                    "Find by code: FAILED"
            );
        }


        // ==========================================
        // 3. FIND ALL PLANS
        // ==========================================

        List<TelecomPlan> allPlans =
                planService.findAll();

        System.out.println(
                "Total plans: " +
                        allPlans.size()
        );

        if (allPlans.size() == 5) {

            System.out.println(
                    "Find all plans: PASSED"
            );

        } else {

            System.out.println(
                    "Find all plans: FAILED"
            );
        }


        // ==========================================
        // 4. ACTIVE PLANS
        // ==========================================

        List<TelecomPlan> activePlans =
                planService.findActivePlans();

        System.out.println(
                "Active plans: " +
                        activePlans.size()
        );

        if (activePlans.size() == 5) {

            System.out.println(
                    "Find active plans: PASSED"
            );

        } else {

            System.out.println(
                    "Find active plans: FAILED"
            );
        }


        // ==========================================
        // 5. FILTER BY PRICE
        // ==========================================

        List<TelecomPlan> priceFilteredPlans =
                planService.filterByPrice(
                        new BigDecimal("700.00")
                );

        System.out.println(
                "\nPlans <= 700:"
        );

        for (TelecomPlan p : priceFilteredPlans) {

            System.out.println(
                    p.getPlanCode() +
                            " - ₹" +
                            p.getMonthlyRental()
            );
        }


        // ==========================================
        // 6. FILTER BY DATA ALLOWANCE
        // ==========================================

        List<TelecomPlan> dataFilteredPlans =
                planService.filterByDataAllowance(
                        new BigDecimal("50.00")
                );

        System.out.println(
                "\nPlans with >= 50 GB:"
        );

        for (TelecomPlan p : dataFilteredPlans) {

            System.out.println(
                    p.getPlanCode() +
                            " - " +
                            p.getDataAllowanceGB() +
                            " GB"
            );
        }


        // ==========================================
        // 7. SORT BY PRICE
        // ==========================================

        List<TelecomPlan> sortedPlans =
                planService.sortByPrice();

        System.out.println(
                "\nPlans sorted by price:"
        );

        for (TelecomPlan p : sortedPlans) {

            System.out.println(
                    p.getPlanCode() +
                            " - ₹" +
                            p.getMonthlyRental()
            );
        }


        // ==========================================
        // 8. COMPARE PLANS
        // ==========================================

        List<TelecomPlan> comparedPlans =
                planService.comparePlans(
                        Arrays.asList(
                                1L,
                                2L,
                                4L
                        )
                );

        System.out.println(
                "\nPlan comparison:"
        );

        for (TelecomPlan p : comparedPlans) {

            System.out.println(
                    p.getPlanCode() +
                            " | " +
                            p.getPlanName() +
                            " | ₹" +
                            p.getMonthlyRental() +
                            " | " +
                            p.getDataAllowanceGB() +
                            " GB | " +
                            p.getVoiceMinutes() +
                            " min"
            );
        }


        // ==========================================
        // COMPLETED
        // ==========================================

        System.out.println(
                "\n=== PLAN MODULE TEST COMPLETED ==="
        );
    }
}