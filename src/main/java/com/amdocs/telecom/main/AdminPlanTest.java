package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.TelecomPlan;

import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminPlanService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminPlanServiceImpl;

import java.util.List;
import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.enums.PlanType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class AdminPlanTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminPlanService adminPlanService =
                new AdminPlanServiceImpl();


        System.out.println(
                "=== ADMIN PLAN TEST SUITE ==="
        );


        Admin admin = null;
        TelecomPlan createdPlan = null;



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
        // TEST 2: VIEW ALL PLANS
        // ==========================================

        System.out.println(
                "\n=== TEST 2: VIEW ALL PLANS ==="
        );


        try {


            List<TelecomPlan> plans =
                    adminPlanService.findAllPlans(
                            admin
                    );


            if(plans != null &&
                    !plans.isEmpty()) {


                pass(
                        "View all plans"
                );


                System.out.println(
                        "Total plans: " +
                                plans.size()
                );


            } else {


                fail(
                        "View all plans"
                );
            }


        } catch(Exception e) {


            fail(
                    "View all plans: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 3: FIND PLAN BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND PLAN BY ID ==="
        );


        try {


            TelecomPlan firstPlan =
                    adminPlanService
                            .findAllPlans(admin)
                            .get(0);


            TelecomPlan found =
                    adminPlanService.findPlanById(
                            admin,
                            firstPlan.getPlanId()
                    );


            if(found != null &&
                    found.getPlanId()
                            == firstPlan.getPlanId()) {


                pass(
                        "Find plan by ID"
                );


            } else {


                fail(
                        "Find plan by ID"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find plan by ID: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 4: FIND PLAN BY CODE
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND PLAN BY CODE ==="
        );


        try {


            TelecomPlan firstPlan =
                    adminPlanService
                            .findAllPlans(admin)
                            .get(0);


            TelecomPlan found =
                    adminPlanService.findPlanByCode(
                            admin,
                            firstPlan.getPlanCode()
                    );


            if(found != null &&
                    found.getPlanId()
                            == firstPlan.getPlanId()) {


                pass(
                        "Find plan by code"
                );


            } else {


                fail(
                        "Find plan by code"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find plan by code: " +
                            e.getMessage()
            );
        }





        // ==========================================
        // TEST 5: VIEW ACTIVE PLANS
        // ==========================================

        System.out.println(
                "\n=== TEST 5: VIEW ACTIVE PLANS ==="
        );


        try {


            List<TelecomPlan> activePlans =
                    adminPlanService
                            .findActivePlans(admin);



            if(activePlans != null &&
                    !activePlans.isEmpty()) {


                pass(
                        "View active plans"
                );


                System.out.println(
                        "Active plans: " +
                                activePlans.size()
                );


            } else {


                fail(
                        "View active plans"
                );
            }


        } catch(Exception e) {


            fail(
                    "View active plans: " +
                            e.getMessage()
            );
        }


        // ==========================================
// TEST 6: CREATE PLAN
// ==========================================

        System.out.println(
                "\n=== TEST 6: CREATE PLAN ==="
        );



        try {


            String runId =
                    String.valueOf(
                            System.currentTimeMillis()
                    );


            TelecomPlan plan =
                    new TelecomPlan(
                            0,
                            "TEST-PLAN-" + runId,
                            "Admin Test Plan",
                            PlanType.POSTPAID,
                            new BigDecimal("499.00"),
                            new BigDecimal("50.00"),
                            1000,
                            100,
                            30,
                            false,
                            AccountStatus.ACTIVE,
                            null,
                            null
                    );


            adminPlanService.createPlan(
                    admin,
                    plan
            );


            createdPlan =
                    adminPlanService.findPlanByCode(
                            admin,
                            plan.getPlanCode()
                    );


            if(createdPlan != null &&
                    createdPlan.getPlanId() > 0) {


                pass(
                        "Create plan"
                );


                System.out.println(
                        "Plan ID: " +
                                createdPlan.getPlanId()
                );


            } else {


                fail(
                        "Create plan"
                );
            }


        } catch(Exception e) {


            fail(
                    "Create plan: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 7: UPDATE PLAN
// ==========================================

        System.out.println(
                "\n=== TEST 7: UPDATE PLAN ==="
        );


        try {


            createdPlan.setPlanName(
                    "Updated Admin Test Plan"
            );


            adminPlanService.updatePlan(
                    admin,
                    createdPlan
            );


            TelecomPlan updated =
                    adminPlanService.findPlanById(
                            admin,
                            createdPlan.getPlanId()
                    );


            if(updated != null &&
                    "Updated Admin Test Plan"
                            .equals(
                                    updated.getPlanName()
                            )) {


                pass(
                        "Update plan"
                );


            } else {


                fail(
                        "Update plan"
                );
            }


        } catch(Exception e) {


            fail(
                    "Update plan: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 8: DEACTIVATE PLAN
// ==========================================

        System.out.println(
                "\n=== TEST 8: DEACTIVATE PLAN ==="
        );


        try {


            adminPlanService.deactivatePlan(
                    admin,
                    createdPlan.getPlanId()
            );


            TelecomPlan updated =
                    adminPlanService.findPlanById(
                            admin,
                            createdPlan.getPlanId()
                    );


            if(updated != null &&
                    updated.getStatus()
                            == AccountStatus.INACTIVE) {


                pass(
                        "Deactivate plan"
                );


            } else {


                fail(
                        "Deactivate plan"
                );
            }


        } catch(Exception e) {


            fail(
                    "Deactivate plan: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 9: ACTIVATE PLAN
// ==========================================

        System.out.println(
                "\n=== TEST 9: ACTIVATE PLAN ==="
        );


        try {


            adminPlanService.activatePlan(
                    admin,
                    createdPlan.getPlanId()
            );


            TelecomPlan updated =
                    adminPlanService.findPlanById(
                            admin,
                            createdPlan.getPlanId()
                    );


            if(updated != null &&
                    updated.getStatus()
                            == AccountStatus.ACTIVE) {


                pass(
                        "Activate plan"
                );


            } else {


                fail(
                        "Activate plan"
                );
            }


        } catch(Exception e) {


            fail(
                    "Activate plan: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 10: DELETE PLAN
// ==========================================

        System.out.println(
                "\n=== TEST 10: DELETE PLAN ==="
        );


        try {


            long planId =
                    createdPlan.getPlanId();


            adminPlanService.deletePlan(
                    admin,
                    planId
            );


            TelecomPlan deleted =
                    adminPlanService.findPlanById(
                            admin,
                            planId
                    );


            if(deleted == null) {


                pass(
                        "Delete plan"
                );


            } else {


                fail(
                        "Delete plan"
                );
            }


        } catch(Exception e) {


            fail(
                    "Delete plan: " +
                            e.getMessage()
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