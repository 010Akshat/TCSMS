package com.amdocs.telecom.service;


import com.amdocs.telecom.model.Admin;

import java.math.BigDecimal;


public interface AdminReportService {


    String generateRevenueReport(
            Admin admin
    );


    String generateUsageReport(
            Admin admin
    );


    String generateCustomerReport(
            Admin admin
    );


    String generateDashboardReport(
            Admin admin
    );


    String generateUnpaidCustomerReport(
            Admin admin
    );


    String generatePlanReport(
            Admin admin,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

}