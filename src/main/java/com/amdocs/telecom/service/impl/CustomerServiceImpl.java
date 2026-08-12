package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.service.CustomerService;
import com.amdocs.telecom.validation.CustomerValidator;
import java.util.List;
import com.amdocs.telecom.model.enums.AccountStatus;


import java.time.LocalDateTime;

import com.amdocs.telecom.security.PasswordUtil;
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }

    @Override
    public void register(Customer customer, String password) {

        // 1. Mandatory fields
        CustomerValidator.validateMandatoryFields(customer);

        // 2. Age validation
        CustomerValidator.validateAge(
                customer.getDateOfBirth()
        );

        // 3. Password validation
        CustomerValidator.validatePassword(password);

        // 4. Email uniqueness
        Customer existingEmail =
                customerDAO.findByEmail(
                        customer.getEmail()
                );

        if (existingEmail != null) {
            throw new IllegalArgumentException(
                    "Email is already registered."
            );
        }

        // 5. Mobile uniqueness
        Customer existingMobile =
                customerDAO.findByMobileNumber(
                        customer.getMobileNumber()
                );

        if (existingMobile != null) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered."
            );
        }

        // 6. Username uniqueness
        Customer existingUsername =
                customerDAO.findByUsername(
                        customer.getUsername()
                );

        if (existingUsername != null) {
            throw new IllegalArgumentException(
                    "Username is already registered."
            );
        }

        System.out.println(
                "Email, mobile number and username are available."
        );
        // 7. Hash password
        String passwordHash =
                PasswordUtil.hashPassword(password);

        customer.setPasswordHash(passwordHash);

// 8. Set registration details
        customer.setRegistrationDate(
                LocalDateTime.now()
        );

        customer.setAccountStatus(
                AccountStatus.ACTIVE
        );

// 9. Save customer
        customerDAO.save(customer);

        System.out.println(
                "Customer registered successfully."
        );
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