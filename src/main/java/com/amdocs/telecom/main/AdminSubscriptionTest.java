package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.TelecomPlan;

import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;
import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminSubscriptionService;
import com.amdocs.telecom.service.PlanService;
import com.amdocs.telecom.service.SubscriptionService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminSubscriptionServiceImpl;
import com.amdocs.telecom.service.impl.PlanServiceImpl;
import com.amdocs.telecom.service.impl.SubscriptionServiceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.amdocs.telecom.model.SubscriptionHistory;

public class AdminSubscriptionTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminSubscriptionService adminSubscriptionService =
                new AdminSubscriptionServiceImpl();


        SubscriptionService subscriptionService =
                new SubscriptionServiceImpl();


        PlanService planService =
                new PlanServiceImpl();



        System.out.println(
                "=== ADMIN SUBSCRIPTION TEST SUITE ==="
        );



        // ==========================================
        // TEST 1: ADMIN LOGIN
        // ==========================================


        Admin admin = null;


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
                    admin.getAdminId() > 0 &&
                    "ACTIVE".equalsIgnoreCase(
                            admin.getAdminStatus()
                    )) {


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
        // DYNAMIC PLAN SELECTION
        // ==========================================


        List<TelecomPlan> activePlans =
                planService.findActivePlans();



        if(activePlans == null ||
                activePlans.size() < 2) {


            System.out.println(
                    "Not enough active plans."
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



        TelecomPlan lowerPlan =
                sortedPlans.get(0);



        TelecomPlan higherPlan =
                sortedPlans.get(
                        sortedPlans.size()-1
                );



        System.out.println(
                "\nSelected plans:"
        );


        System.out.println(
                "Lower Plan : " +
                        lowerPlan.getPlanCode()
        );


        System.out.println(
                "Higher Plan : " +
                        higherPlan.getPlanCode()
        );




        // ==========================================
        // TEST 2: CREATE TEST SUBSCRIPTION
        // ==========================================


        System.out.println(
                "\n=== TEST 2: CREATE TEST SUBSCRIPTION ==="
        );


        long customerId = 3;


        String runId =
                String.valueOf(
                        System.currentTimeMillis()
                );


        MobileSubscription subscription = null;



        try {


            subscription =
                    subscriptionService.subscribe(
                            customerId,
                            lowerPlan.getPlanId(),
                            "99999" +
                                    runId.substring(
                                            runId.length()-5
                                    ),
                            "ADMINSIM" +
                                    runId,
                            "ESIM",
                            "POSTPAID"
                    );



            if(subscription != null &&
                    subscription.getSubscriptionId() > 0) {


                pass(
                        "Create test subscription"
                );


                System.out.println(
                        "Subscription ID: " +
                                subscription.getSubscriptionId()
                );


            } else {


                fail(
                        "Create test subscription"
                );
            }



        } catch(Exception e) {


            fail(
                    "Create test subscription: " +
                            e.getMessage()
            );
        }



        long subscriptionId =
                subscription.getSubscriptionId();




        // ==========================================
        // TEST 3: ADMIN UPGRADE PLAN
        // ==========================================


        System.out.println(
                "\n=== TEST 3: ADMIN UPGRADE PLAN ==="
        );



        try {


            adminSubscriptionService.upgradePlan(
                    admin,
                    subscriptionId,
                    higherPlan.getPlanId(),
                    "Admin upgrade test"
            );



            MobileSubscription updated =
                    subscriptionService.findById(
                            subscriptionId
                    );



            if(updated != null &&
                    updated.getPlanId()
                            == higherPlan.getPlanId()) {


                pass(
                        "Admin upgrade plan"
                );


            } else {


                fail(
                        "Admin upgrade plan"
                );
            }



        } catch(Exception e) {


            fail(
                    "Admin upgrade plan: " +
                            e.getMessage()
            );
        }


        System.out.println(
                "\nPART 1 COMPLETED"
        );

        // ==========================================
// TEST 4: VERIFY UPGRADE HISTORY
// ==========================================

        System.out.println(
                "\n=== TEST 4: VERIFY UPGRADE HISTORY ==="
        );


        try {

            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            subscriptionId
                    );


            boolean found =
                    history.stream()
                            .anyMatch(item ->
                                    item.getOldPlanId()
                                            == lowerPlan.getPlanId()
                                            &&
                                            item.getNewPlanId()
                                                    == higherPlan.getPlanId()
                            );


            if(found) {

                pass(
                        "Upgrade history verification"
                );


            } else {


                fail(
                        "Upgrade history verification"
                );
            }


        } catch(Exception e) {


            fail(
                    "Upgrade history verification: " +
                            e.getMessage()
            );
        }


        // ==========================================
// TEST 5: ADMIN DOWNGRADE PLAN
// ==========================================

        System.out.println(
                "\n=== TEST 5: ADMIN DOWNGRADE PLAN ==="
        );


        try {


            MobileSubscription current =
                    subscriptionService.findById(
                            subscriptionId
                    );


            TelecomPlan currentPlan =
                    planService.findById(
                            current.getPlanId()
                    );


            TelecomPlan downgradePlan = null;


            List<TelecomPlan> plans =
                    planService.findActivePlans();


            for(TelecomPlan plan : plans) {


                if(plan.getMonthlyRental()
                        .compareTo(
                                currentPlan.getMonthlyRental()
                        ) < 0) {


                    downgradePlan = plan;
                    break;
                }
            }



            if(downgradePlan == null) {


                throw new IllegalStateException(
                        "No cheaper plan available."
                );
            }




            adminSubscriptionService.downgradePlan(
                    admin,
                    subscriptionId,
                    downgradePlan.getPlanId(),
                    "Admin downgrade test"
            );



            MobileSubscription updated =
                    subscriptionService.findById(
                            subscriptionId
                    );



            if(updated.getPlanId()
                    == downgradePlan.getPlanId()) {


                pass(
                        "Admin downgrade plan"
                );


            } else {


                fail(
                        "Admin downgrade plan"
                );
            }



        } catch(Exception e) {


            fail(
                    "Admin downgrade plan: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 6: VERIFY DOWNGRADE HISTORY
// ==========================================

        System.out.println(
                "\n=== TEST 6: VERIFY DOWNGRADE HISTORY ==="
        );


        try {


            List<SubscriptionHistory> history =
                    subscriptionService.findHistory(
                            subscriptionId
                    );


            boolean found =
                    history.stream()
                            .anyMatch(item ->
                                    item.getChangedBy()
                                            .equalsIgnoreCase(
                                                    "ADMIN"
                                            )
                            );


            if(found) {


                pass(
                        "Downgrade history verification"
                );


            } else {


                fail(
                        "Downgrade history verification"
                );
            }


        } catch(Exception e) {


            fail(
                    "Downgrade history verification: " +
                            e.getMessage()
            );
        }


        // ==========================================
// TEST 7: CHANGE SUBSCRIPTION TYPE
// ==========================================

        System.out.println(
                "\n=== TEST 7: CHANGE SUBSCRIPTION TYPE ==="
        );


        try {


            adminSubscriptionService.changeSubscriptionType(
                    admin,
                    subscriptionId,
                    "PREPAID",
                    "Admin type change"
            );



            MobileSubscription updated =
                    subscriptionService.findById(
                            subscriptionId
                    );



            if(updated.getSubscriptionType()
                    == SubscriptionType.PREPAID) {


                pass(
                        "Admin change subscription type"
                );


            } else {


                fail(
                        "Admin change subscription type"
                );
            }



        } catch(Exception e) {


            fail(
                    "Admin change subscription type: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 8: DEACTIVATE SUBSCRIPTION
// ==========================================

        System.out.println(
                "\n=== TEST 8: DEACTIVATE SUBSCRIPTION ==="
        );


        try {


            adminSubscriptionService.deactivateSubscription(
                    admin,
                    subscriptionId
            );


            MobileSubscription updated =
                    subscriptionService.findById(
                            subscriptionId
                    );


            if(updated.getStatus()
                    == SubscriptionStatus.INACTIVE) {


                pass(
                        "Admin deactivate subscription"
                );


            } else {


                fail(
                        "Admin deactivate subscription"
                );
            }


        } catch(Exception e) {


            fail(
                    "Admin deactivate subscription: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 9: ACTIVATE SUBSCRIPTION
// ==========================================

        System.out.println(
                "\n=== TEST 9: ACTIVATE SUBSCRIPTION ==="
        );


        try {


            adminSubscriptionService.activateSubscription(
                    admin,
                    subscriptionId
            );


            MobileSubscription updated =
                    subscriptionService.findById(
                            subscriptionId
                    );


            if(updated.getStatus()
                    == SubscriptionStatus.ACTIVE) {


                pass(
                        "Admin activate subscription"
                );


            } else {


                fail(
                        "Admin activate subscription"
                );
            }


        } catch(Exception e) {


            fail(
                    "Admin activate subscription: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 10: NULL ADMIN SECURITY
// ==========================================

        System.out.println(
                "\n=== TEST 10: NULL ADMIN REJECTION ==="
        );


        try {


            adminSubscriptionService.activateSubscription(
                    null,
                    subscriptionId
            );


            fail(
                    "Null admin rejection"
            );


        } catch(SecurityException e) {


            if(e.getMessage()
                    .contains(
                            "Admin authentication required"
                    )) {


                pass(
                        "Null admin rejection"
                );


            } else {


                fail(
                        "Null admin rejection: " +
                                e.getMessage()
                );
            }
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