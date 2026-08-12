package com.amdocs.telecom.service;

import com.amdocs.telecom.model.TelecomPlan;

import java.math.BigDecimal;
import java.util.List;

public interface PlanService {

    void save(TelecomPlan plan);

    TelecomPlan findById(long planId);

    TelecomPlan findByCode(String planCode);

    List<TelecomPlan> findAll();

    List<TelecomPlan> findActivePlans();

    List<TelecomPlan> filterByPrice(
            BigDecimal maxPrice
    );

    List<TelecomPlan> filterByDataAllowance(
            BigDecimal minimumDataGB
    );

    List<TelecomPlan> sortByPrice();

    List<TelecomPlan> comparePlans(
            List<Long> planIds
    );

    void update(TelecomPlan plan);

    void delete(long planId);
}