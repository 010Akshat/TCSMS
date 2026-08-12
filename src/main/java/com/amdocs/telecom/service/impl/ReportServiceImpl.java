package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.BillingDAO;
import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.dao.UsageDAO;

import com.amdocs.telecom.dao.impl.BillingDAOImpl;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.PlanDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionDAOImpl;
import com.amdocs.telecom.dao.impl.UsageDAOImpl;

import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.model.enums.BillStatus;
import com.amdocs.telecom.model.enums.UsageType;

import com.amdocs.telecom.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import java.util.stream.Collectors;


public class ReportServiceImpl implements ReportService {


    private final CustomerDAO customerDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final PlanDAO planDAO;
    private final UsageDAO usageDAO;
    private final BillingDAO billingDAO;


    public ReportServiceImpl() {

        this.customerDAO =
                new CustomerDAOImpl();

        this.subscriptionDAO =
                new SubscriptionDAOImpl();

        this.planDAO =
                new PlanDAOImpl();

        this.usageDAO =
                new UsageDAOImpl();

        this.billingDAO =
                new BillingDAOImpl();
    }


    @Override
    public List<Customer> getHighestConsumingCustomers() {


        Map<Long, BigDecimal> customerUsage =
                usageDAO.findAll()
                        .stream()
                        .map(usage -> {

                            long customerId =
                                    subscriptionDAO
                                            .findById(
                                                    usage.getSubscriptionId()
                                            )
                                            .getCustomerId();


                            return new CustomerUsage(
                                    customerId,
                                    usage.getQuantity()
                            );

                        })
                        .collect(
                                Collectors.groupingBy(
                                        CustomerUsage::getCustomerId,

                                        Collectors.reducing(
                                                BigDecimal.ZERO,
                                                CustomerUsage::getQuantity,
                                                BigDecimal::add
                                        )
                                )
                        );


        return customerUsage.entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<Long, BigDecimal>
                                        comparingByValue()
                                .reversed()
                )
                .limit(5)
                .map(entry ->
                        customerDAO.findById(
                                entry.getKey()
                        )
                )
                .collect(
                        Collectors.toList()
                );
    }



    @Override
    public Map<String, Long> getCustomersByCity() {


        return customerDAO.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Customer::getCity,
                                Collectors.counting()
                        )
                );
    }



    @Override
    public List<TelecomPlan> getPlansWithinPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice) {


        if(minPrice == null || maxPrice == null) {

            throw new IllegalArgumentException(
                    "Price range cannot be null."
            );
        }


        if(minPrice.compareTo(maxPrice) > 0) {

            throw new IllegalArgumentException(
                    "Minimum price cannot be greater than maximum price."
            );
        }


        return planDAO.findAll()
                .stream()
                .filter(plan ->
                        plan.getMonthlyRental()
                                .compareTo(minPrice) >= 0
                                &&
                                plan.getMonthlyRental()
                                        .compareTo(maxPrice) <= 0
                )
                .collect(
                        Collectors.toList()
                );
    }



    @Override
    public Map<Long, Long> getMostSubscribedPlans() {


        return subscriptionDAO.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                subscription ->
                                        subscription.getPlanId(),

                                Collectors.counting()
                        )
                );
    }



    @Override
    public Map<LocalDate, BigDecimal> getMonthlyRevenue() {


        return billingDAO.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(

                                bill ->
                                        bill.getBillingMonth(),

                                Collectors.reducing(

                                        BigDecimal.ZERO,

                                        bill ->
                                                bill.getTotalAmount(),

                                        BigDecimal::add
                                )
                        )
                );
    }



    @Override
    public Map<UsageType, BigDecimal> getUsageByType() {


        return usageDAO.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(

                                usage ->
                                        usage.getUsageType(),

                                Collectors.reducing(

                                        BigDecimal.ZERO,

                                        usage ->
                                                usage.getQuantity(),

                                        BigDecimal::add
                                )
                        )
                );
    }



    @Override
    public List<Customer> getCustomersWithUnpaidBills() {


        return billingDAO.findAll()
                .stream()

                .filter(bill ->
                        bill.getBillStatus()
                                == BillStatus.UNPAID
                )


                .map(bill ->
                        subscriptionDAO.findById(
                                bill.getSubscriptionId()
                        )
                )


                .map(subscription ->
                        customerDAO.findById(
                                subscription.getCustomerId()
                        )
                )


                .distinct()

                .collect(
                        Collectors.toList()
                );
    }




    @Override
    public BigDecimal getAverageMonthlyRevenuePerCustomer() {


        Map<Long, BigDecimal> customerRevenue =
                billingDAO.findAll()
                        .stream()

                        .map(bill -> {


                            long customerId =
                                    subscriptionDAO
                                            .findById(
                                                    bill.getSubscriptionId()
                                            )
                                            .getCustomerId();


                            return new CustomerRevenue(
                                    customerId,
                                    bill.getTotalAmount()
                            );

                        })


                        .collect(
                                Collectors.groupingBy(

                                        CustomerRevenue::getCustomerId,

                                        Collectors.reducing(

                                                BigDecimal.ZERO,

                                                CustomerRevenue::getAmount,

                                                BigDecimal::add
                                        )
                                )
                        );


        if(customerRevenue.isEmpty()) {

            return BigDecimal.ZERO;
        }


        BigDecimal totalRevenue =
                customerRevenue.values()
                        .stream()
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        return totalRevenue.divide(
                BigDecimal.valueOf(
                        customerRevenue.size()
                ),
                2,
                java.math.RoundingMode.HALF_UP
        );
    }




    @Override
    public Map<String,Object> generateDashboardReports() {


        ExecutorService executor =
                Executors.newFixedThreadPool(4);


        try {


            Future<Map<LocalDate, BigDecimal>> revenueFuture =
                    executor.submit(
                            () -> getMonthlyRevenue()
                    );


            Future<Map<UsageType, BigDecimal>> usageFuture =
                    executor.submit(
                            () -> getUsageByType()
                    );


            Future<Map<String, Long>> customerFuture =
                    executor.submit(
                            () -> getCustomersByCity()
                    );


            Future<Map<Long, Long>> planFuture =
                    executor.submit(
                            () -> getMostSubscribedPlans()
                    );



            Map<String,Object> dashboard =
                    new HashMap<>();


            dashboard.put(
                    "MONTHLY_REVENUE",
                    revenueFuture.get()
            );


            dashboard.put(
                    "USAGE_REPORT",
                    usageFuture.get()
            );


            dashboard.put(
                    "CUSTOMER_CITY_REPORT",
                    customerFuture.get()
            );


            dashboard.put(
                    "PLAN_SUBSCRIPTION_REPORT",
                    planFuture.get()
            );


            return dashboard;


        } catch(Exception e) {


            throw new RuntimeException(
                    "Failed to generate dashboard reports.",
                    e
            );

        }
        finally {

            executor.shutdown();

        }
    }




    private static class CustomerUsage {


        private final long customerId;
        private final BigDecimal quantity;


        public CustomerUsage(
                long customerId,
                BigDecimal quantity) {

            this.customerId = customerId;
            this.quantity = quantity;
        }


        public long getCustomerId() {
            return customerId;
        }


        public BigDecimal getQuantity() {
            return quantity;
        }
    }




    private static class CustomerRevenue {


        private final long customerId;
        private final BigDecimal amount;


        public CustomerRevenue(
                long customerId,
                BigDecimal amount) {

            this.customerId = customerId;
            this.amount = amount;
        }


        public long getCustomerId() {
            return customerId;
        }


        public BigDecimal getAmount() {
            return amount;
        }
    }

}