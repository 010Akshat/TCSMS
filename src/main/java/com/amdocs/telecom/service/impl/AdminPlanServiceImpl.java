package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.AccountStatus;

import com.amdocs.telecom.security.AdminAuthorizationUtil;

import com.amdocs.telecom.service.AdminPlanService;
import com.amdocs.telecom.service.PlanService;

import java.util.List;


public class AdminPlanServiceImpl
        implements AdminPlanService {


    private final PlanService planService;


    public AdminPlanServiceImpl() {

        this.planService =
                new PlanServiceImpl();

    }



    @Override
    public void createPlan(
            Admin admin,
            TelecomPlan plan) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(plan == null) {

            throw new IllegalArgumentException(
                    "Plan cannot be null."
            );
        }


        planService.save(
                plan
        );
    }





    @Override
    public TelecomPlan findPlanById(
            Admin admin,
            long planId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(planId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid plan id."
            );
        }


        return planService.findById(
                planId
        );
    }





    @Override
    public TelecomPlan findPlanByCode(
            Admin admin,
            String planCode) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(planCode == null ||
                planCode.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Plan code is mandatory."
            );
        }


        return planService.findByCode(
                planCode.trim()
        );
    }





    @Override
    public List<TelecomPlan> findAllPlans(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return planService.findAll();
    }





    @Override
    public List<TelecomPlan> findActivePlans(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return planService.findActivePlans();
    }





    @Override
    public void updatePlan(
            Admin admin,
            TelecomPlan plan) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(plan == null) {

            throw new IllegalArgumentException(
                    "Plan cannot be null."
            );
        }


        if(plan.getPlanId() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid plan id."
            );
        }


        planService.update(
                plan
        );
    }





    @Override
    public void activatePlan(
            Admin admin,
            long planId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        TelecomPlan plan =
                planService.findById(
                        planId
                );


        if(plan == null) {

            throw new IllegalArgumentException(
                    "Plan not found."
            );
        }


        plan.setStatus(
                AccountStatus.ACTIVE
        );


        planService.update(
                plan
        );
    }





    @Override
    public void deactivatePlan(
            Admin admin,
            long planId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        TelecomPlan plan =
                planService.findById(
                        planId
                );


        if(plan == null) {

            throw new IllegalArgumentException(
                    "Plan not found."
            );
        }


        plan.setStatus(
                AccountStatus.INACTIVE
        );


        planService.update(
                plan
        );
    }





    @Override
    public void deletePlan(
            Admin admin,
            long planId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(planId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid plan id."
            );
        }


        planService.delete(
                planId
        );
    }

}