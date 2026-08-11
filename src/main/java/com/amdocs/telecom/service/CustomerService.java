package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer findById(long customerId);

    Customer findByEmail(String email);

    Customer findByMobileNumber(String mobileNumber);

    Customer findByUsername(String username);

    List<Customer> findAll();

    void save(Customer customer);

    void update(Customer customer);

    void delete(long customerId);
}