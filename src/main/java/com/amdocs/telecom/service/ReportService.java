package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.UsageType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {

    /*
     * Finds customers consuming highest usage.
     */
    List<Customer> getHighestConsumingCustomers();


    /*
     * Groups customers according to city.
     *
     * City -> Number of customers
     */
    Map<String, Long> getCustomersByCity();


    /*
     * Finds plans between given price range.
     */
    List<TelecomPlan> getPlansWithinPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );


    /*
     * Finds most subscribed plans.
     *
     * Plan ID -> Number of subscriptions
     */
    Map<Long, Long> getMostSubscribedPlans();


    /*
     * Calculates revenue month wise.
     *
     * Billing Month -> Revenue
     */
    Map<LocalDate, BigDecimal> getMonthlyRevenue();


    /*
     * Calculates usage grouped by usage type.
     *
     * DATA -> Total DATA usage
     * VOICE -> Total VOICE usage
     */
    Map<UsageType, BigDecimal> getUsageByType();


    /*
     * Finds customers having unpaid bills.
     */
    List<Customer> getCustomersWithUnpaidBills();


    /*
     * Calculates average revenue per customer.
     */
    BigDecimal getAverageMonthlyRevenuePerCustomer();


    /*
     * Executes independent reports concurrently.
     *
     * Uses ExecutorService + Future.
     */
    Map<String, Object> generateDashboardReports();

}