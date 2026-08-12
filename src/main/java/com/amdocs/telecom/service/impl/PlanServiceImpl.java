package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.dao.impl.PlanDAOImpl;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.service.PlanService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PlanServiceImpl implements PlanService {

    private final PlanDAO planDAO;

    public PlanServiceImpl() {
        this.planDAO = new PlanDAOImpl();
    }

    @Override
    public void save(TelecomPlan plan) {
        planDAO.save(plan);
    }

    @Override
    public TelecomPlan findById(long planId) {
        return planDAO.findById(planId);
    }

    @Override
    public TelecomPlan findByCode(String planCode) {
        return planDAO.findByCode(planCode);
    }

    @Override
    public List<TelecomPlan> findAll() {
        return planDAO.findAll();
    }

    @Override
    public List<TelecomPlan> findActivePlans() {
        return planDAO.findActivePlans();
    }

    @Override
    public List<TelecomPlan> filterByPrice(
            BigDecimal maxPrice) {

        if (maxPrice == null) {
            throw new IllegalArgumentException(
                    "Maximum price cannot be null."
            );
        }

        return planDAO.findAll()
                .stream()
                .filter(plan ->
                        plan.getMonthlyRental()
                                .compareTo(maxPrice) <= 0
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<TelecomPlan> filterByDataAllowance(
            BigDecimal minimumDataGB) {

        if (minimumDataGB == null) {
            throw new IllegalArgumentException(
                    "Minimum data allowance cannot be null."
            );
        }

        return planDAO.findAll()
                .stream()
                .filter(plan ->
                        plan.getDataAllowanceGB()
                                .compareTo(minimumDataGB) >= 0
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<TelecomPlan> sortByPrice() {

        return planDAO.findAll()
                .stream()
                .sorted(
                        (plan1, plan2) ->
                                plan1.getMonthlyRental()
                                        .compareTo(
                                                plan2.getMonthlyRental()
                                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<TelecomPlan> comparePlans(
            List<Long> planIds) {

        if (planIds == null || planIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Plan IDs cannot be empty."
            );
        }

        return planDAO.findAll()
                .stream()
                .filter(plan ->
                        planIds.contains(
                                plan.getPlanId()
                        )
                )
                .sorted(
                        (plan1, plan2) ->
                                plan1.getMonthlyRental()
                                        .compareTo(
                                                plan2.getMonthlyRental()
                                        )
                )
                .collect(Collectors.toList());
    }

    @Override
    public void update(TelecomPlan plan) {
        planDAO.update(plan);
    }

    @Override
    public void delete(long planId) {
        planDAO.delete(planId);
    }
}
