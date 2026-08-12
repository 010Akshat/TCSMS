package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Payment;
import com.amdocs.telecom.model.enums.PaymentMode;
import com.amdocs.telecom.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    Payment processPayment(
            long billId,
            BigDecimal amount,
            PaymentMode paymentMode
    );

    Payment processPayment(
            long billId,
            BigDecimal amount,
            PaymentMode paymentMode,
            PaymentStatus paymentStatus
    );

    Payment findById(
            long paymentId
    );

    Payment findByTransactionReference(
            String transactionReference
    );

    List<Payment> findByBillId(
            long billId
    );

    List<Payment> findByCustomerId(
            long customerId
    );

    List<Payment> findAll();

    void update(
            Payment payment
    );

    void delete(
            long paymentId
    );
}