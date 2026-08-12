package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.Payment;

import java.sql.Connection;
import java.util.List;

public interface PaymentDAO {

    void save(Payment payment);

    void save(
            Payment payment,
            Connection connection
    );

    Payment findById(long paymentId);

    Payment findById(
            long paymentId,
            Connection connection
    );

    Payment findByTransactionReference(
            String transactionReference
    );

    List<Payment> findByBillId(
            long billId
    );

    List<Payment> findByBillId(
            long billId,
            Connection connection
    );

    List<Payment> findByCustomerId(
            long customerId
    );

    List<Payment> findAll();

    void update(Payment payment);

    void delete(long paymentId);
}