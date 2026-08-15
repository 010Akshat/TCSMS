package com.amdocs.telecom.service.impl;


import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.UsageType;

import com.amdocs.telecom.security.AdminAuthorizationUtil;

import com.amdocs.telecom.service.AdminReportService;
import com.amdocs.telecom.service.ReportService;

import com.amdocs.telecom.util.PDFReportGenerator;


import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class AdminReportServiceImpl
        implements AdminReportService {


    private final ReportService reportService;


    public AdminReportServiceImpl() {

        this.reportService =
                new ReportServiceImpl();

    }



    @Override
    public String generateRevenueReport(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        Map<LocalDate, BigDecimal> revenue =
                reportService.getMonthlyRevenue();



        List<String> content =
                new ArrayList<>();


        content.add(
                "Monthly Revenue Report"
        );


        content.add(
                "-------------------------"
        );


        revenue.forEach(
                (month, amount) ->
                        content.add(
                                month +
                                        " : " +
                                        amount
                        )
        );



        return PDFReportGenerator.generatePDF(
                "monthly_revenue_report.pdf",
                "Amdocs Telecom Revenue Report",
                content
        );

    }





    @Override
    public String generateUsageReport(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        Map<UsageType, BigDecimal> usage =
                reportService.getUsageByType();



        List<String> content =
                new ArrayList<>();


        content.add(
                "Usage Report"
        );


        content.add(
                "-------------------------"
        );


        usage.forEach(
                (type, quantity) ->
                        content.add(
                                type +
                                        " : " +
                                        quantity
                        )
        );



        return PDFReportGenerator.generatePDF(
                "usage_report.pdf",
                "Amdocs Telecom Usage Report",
                content
        );

    }





    @Override
    public String generateCustomerReport(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        List<Customer> customers =
                reportService
                        .getHighestConsumingCustomers();



        List<String> content =
                new ArrayList<>();


        content.add(
                "Highest Consuming Customers"
        );


        content.add(
                "-------------------------"
        );


        customers.forEach(
                customer ->
                        content.add(
                                customer.getFirstName()
                                        + " "
                                        + customer.getLastName()
                        )
        );



        return PDFReportGenerator.generatePDF(
                "customer_report.pdf",
                "Customer Consumption Report",
                content
        );

    }





    @Override
    public String generateDashboardReport(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        Map<String,Object> dashboard =
                reportService
                        .generateDashboardReports();



        List<String> content =
                new ArrayList<>();


        content.add(
                "Dashboard Report"
        );


        content.add(
                "-------------------------"
        );


        dashboard.forEach(
                (key,value) ->
                        content.add(
                                key +
                                        " : " +
                                        value.toString()
                        )
        );



        return PDFReportGenerator.generatePDF(
                "dashboard_report.pdf",
                "Amdocs Telecom Dashboard",
                content
        );

    }





    @Override
    public String generateUnpaidCustomerReport(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        List<Customer> customers =
                reportService
                        .getCustomersWithUnpaidBills();



        List<String> content =
                new ArrayList<>();


        content.add(
                "Customers With Unpaid Bills"
        );


        content.add(
                "-------------------------"
        );


        customers.forEach(
                customer ->
                        content.add(
                                customer.getFirstName()
                                        + " "
                                        + customer.getLastName()
                        )
        );



        return PDFReportGenerator.generatePDF(
                "unpaid_customer_report.pdf",
                "Unpaid Customer Report",
                content
        );

    }





    @Override
    public String generatePlanReport(
            Admin admin,
            BigDecimal minPrice,
            BigDecimal maxPrice) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        List<TelecomPlan> plans =
                reportService
                        .getPlansWithinPriceRange(
                                minPrice,
                                maxPrice
                        );



        List<String> content =
                new ArrayList<>();


        content.add(
                "Plan Price Range Report"
        );


        content.add(
                "-------------------------"
        );


        plans.forEach(
                plan ->
                        content.add(
                                plan.getPlanName()
                                        +
                                        " : "
                                        +
                                        plan.getMonthlyRental()
                        )
        );



        return PDFReportGenerator.generatePDF(
                "plan_report.pdf",
                "Plan Analysis Report",
                content
        );

    }

}