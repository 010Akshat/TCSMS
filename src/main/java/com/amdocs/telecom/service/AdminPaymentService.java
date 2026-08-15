package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Payment;

import java.util.List;

public interface AdminPaymentService {


    Payment findPaymentById(
            Admin admin,
            long paymentId
    );


    Payment findByTransactionReference(
            Admin admin,
            String transactionReference
    );


    List<Payment> findAllPayments(
            Admin admin
    );


    List<Payment> findPaymentsByBill(
            Admin admin,
            long billId
    );


    List<Payment> findPaymentsByCustomer(
            Admin admin,
            long customerId
    );

}