package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }

    @Override
    public Customer findById(long customerId) {
        return customerDAO.findById(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerDAO.findByEmail(email);
    }

    @Override
    public Customer findByMobileNumber(String mobileNumber) {
        return customerDAO.findByMobileNumber(mobileNumber);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerDAO.findByUsername(username);
    }

    @Override
    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    @Override
    public void save(Customer customer) {
        customerDAO.save(customer);
    }

    @Override
    public void update(Customer customer) {
        customerDAO.update(customer);
    }

    @Override
    public void delete(long customerId) {
        customerDAO.delete(customerId);
    }
}