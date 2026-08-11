package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.Customer;

import java.util.List;
import java.time.LocalDateTime;

public interface CustomerDAO {

    void save(Customer customer);

    Customer findById(long customerId);

    Customer findByUsername(String username);

    Customer findByEmail(String email);

    Customer findByMobileNumber(String mobileNumber);

    List<Customer> findAll();

    void update(Customer customer);

    void delete(long customerId);

    void updateFailedLoginAttempts(long customerId, int failedAttempts);

    void updateLockStatus(long customerId, LocalDateTime lockedUntil);

    void updateLastLogin(long customerId, LocalDateTime lastLogin);

    void updatePassword(long customerId, String passwordHash);
}