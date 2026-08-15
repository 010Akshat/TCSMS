package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Customer;

import java.util.List;

public interface AdminCustomerService {


    void createCustomer(
            Admin admin,
            Customer customer,
            String password
    );


    Customer findCustomerById(
            Admin admin,
            long customerId
    );


    Customer findCustomerByEmail(
            Admin admin,
            String email
    );


    Customer findCustomerByMobile(
            Admin admin,
            String mobileNumber
    );


    List<Customer> findAllCustomers(
            Admin admin
    );


    void updateCustomer(
            Admin admin,
            Customer customer
    );


    void deleteCustomer(
            Admin admin,
            long customerId
    );

}