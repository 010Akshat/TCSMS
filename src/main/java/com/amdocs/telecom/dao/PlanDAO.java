package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.TelecomPlan;

import java.util.List;

public interface PlanDAO {

    void save(TelecomPlan plan);

    TelecomPlan findById(long planId);

    TelecomPlan findByCode(String planCode);

    List<TelecomPlan> findAll();

    List<TelecomPlan> findActivePlans();

    void update(TelecomPlan plan);

    void delete(long planId);
}