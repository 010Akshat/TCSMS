package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Customer;

import com.amdocs.telecom.security.AdminAuthorizationUtil;

import com.amdocs.telecom.service.AdminCustomerService;
import com.amdocs.telecom.service.CustomerService;

import java.util.List;


public class AdminCustomerServiceImpl
        implements AdminCustomerService {


    private final CustomerService customerService;


    public AdminCustomerServiceImpl() {

        this.customerService =
                new CustomerServiceImpl();

    }



    @Override
    public void createCustomer(
            Admin admin,
            Customer customer,
            String password) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(customer == null) {

            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }


        customerService.register(
                customer,
                password
        );
    }





    @Override
    public Customer findCustomerById(
            Admin admin,
            long customerId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(customerId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid customer id."
            );
        }


        Customer customer =
                customerService.findById(
                        customerId
                );


        if(customer == null) {

            throw new IllegalArgumentException(
                    "Customer not found."
            );
        }


        return customer;
    }





    @Override
    public Customer findCustomerByEmail(
            Admin admin,
            String email) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(email == null ||
                email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email is mandatory."
            );
        }


        return customerService.findByEmail(
                email.trim()
        );
    }





    @Override
    public Customer findCustomerByMobile(
            Admin admin,
            String mobileNumber) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(mobileNumber == null ||
                mobileNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Mobile number is mandatory."
            );
        }


        return customerService.findByMobileNumber(
                mobileNumber.trim()
        );
    }





    @Override
    public List<Customer> findAllCustomers(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return customerService.findAll();
    }





    @Override
    public void updateCustomer(
            Admin admin,
            Customer customer) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(customer == null) {

            throw new IllegalArgumentException(
                    "Customer cannot be null."
            );
        }


        if(customer.getCustomerId() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid customer id."
            );
        }


        customerService.update(
                customer
        );
    }





    @Override
    public void deleteCustomer(
            Admin admin,
            long customerId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(customerId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid customer id."
            );
        }


        customerService.delete(
                customerId
        );
    }

}