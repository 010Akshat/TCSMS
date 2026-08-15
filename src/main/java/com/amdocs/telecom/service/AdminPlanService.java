package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.TelecomPlan;

import java.util.List;

public interface AdminPlanService {


    void createPlan(
            Admin admin,
            TelecomPlan plan
    );


    TelecomPlan findPlanById(
            Admin admin,
            long planId
    );


    TelecomPlan findPlanByCode(
            Admin admin,
            String planCode
    );


    List<TelecomPlan> findAllPlans(
            Admin admin
    );


    List<TelecomPlan> findActivePlans(
            Admin admin
    );


    void updatePlan(
            Admin admin,
            TelecomPlan plan
    );


    void activatePlan(
            Admin admin,
            long planId
    );


    void deactivatePlan(
            Admin admin,
            long planId
    );


    void deletePlan(
            Admin admin,
            long planId
    );

}