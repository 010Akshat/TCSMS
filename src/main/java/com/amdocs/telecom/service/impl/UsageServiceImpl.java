package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.UsageDAO;
import com.amdocs.telecom.dao.impl.UsageDAOImpl;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.enums.UsageType;
import com.amdocs.telecom.service.UsageService;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsageServiceImpl implements UsageService {

    private final UsageDAO usageDAO;

    public UsageServiceImpl() {
        this.usageDAO = new UsageDAOImpl();
    }

    @Override
    public void save(UsageRecord usageRecord) {
        usageDAO.save(usageRecord);
    }

    @Override
    public UsageRecord findById(long usageId) {
        return usageDAO.findById(usageId);
    }

    @Override
    public List<UsageRecord> findBySubscriptionId(
            long subscriptionId) {

        return usageDAO.findBySubscriptionId(
                subscriptionId
        );
    }

    @Override
    public List<UsageRecord> findAll() {
        return usageDAO.findAll();
    }

    @Override
    public void update(UsageRecord usageRecord) {
        usageDAO.update(usageRecord);
    }

    @Override
    public void delete(long usageId) {
        usageDAO.delete(usageId);
    }

    @Override
    public BigDecimal calculateTotalDataUsage() {

        return usageDAO.findAll()
                .stream()
                .filter(record ->
                        record.getUsageType()
                                == UsageType.DATA
                )
                .map(UsageRecord::getQuantity)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    @Override
    public BigDecimal calculateTotalVoiceUsage() {

        return usageDAO.findAll()
                .stream()
                .filter(record ->
                        record.getUsageType()
                                == UsageType.VOICE
                )
                .map(UsageRecord::getQuantity)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    @Override
    public BigDecimal calculateTotalSmsUsage() {

        return usageDAO.findAll()
                .stream()
                .filter(record ->
                        record.getUsageType()
                                == UsageType.SMS
                )
                .map(UsageRecord::getQuantity)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    @Override
    public Map<UsageType, BigDecimal> calculateMonthlyUsage(
            YearMonth month) {

        if (month == null) {
            throw new IllegalArgumentException(
                    "Month cannot be null."
            );
        }

        return usageDAO.findAll()
                .stream()
                .filter(record ->
                        YearMonth.from(
                                record.getUsageDate()
                        ).equals(month)
                )
                .collect(
                        Collectors.groupingBy(
                                UsageRecord::getUsageType,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        UsageRecord::getQuantity,
                                        BigDecimal::add
                                )
                        )
                );
    }

    @Override
    public Map<UsageType, BigDecimal> calculateUsageByType() {

        return usageDAO.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                UsageRecord::getUsageType,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        UsageRecord::getQuantity,
                                        BigDecimal::add
                                )
                        )
                );
    }
}