package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Payment;

import com.amdocs.telecom.security.AdminAuthorizationUtil;

import com.amdocs.telecom.service.AdminPaymentService;
import com.amdocs.telecom.service.PaymentService;

import java.util.List;


public class AdminPaymentServiceImpl
        implements AdminPaymentService {


    private final PaymentService paymentService;


    public AdminPaymentServiceImpl() {

        this.paymentService =
                new PaymentServiceImpl();

    }



    @Override
    public Payment findPaymentById(
            Admin admin,
            long paymentId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(paymentId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid payment id."
            );
        }


        Payment payment =
                paymentService.findById(
                        paymentId
                );


        if(payment == null) {

            throw new IllegalArgumentException(
                    "Payment not found."
            );
        }


        return payment;
    }





    @Override
    public Payment findByTransactionReference(
            Admin admin,
            String transactionReference) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(transactionReference == null ||
                transactionReference.trim().isEmpty()) {


            throw new IllegalArgumentException(
                    "Transaction reference is mandatory."
            );
        }


        return paymentService
                .findByTransactionReference(
                        transactionReference.trim()
                );
    }





    @Override
    public List<Payment> findAllPayments(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return paymentService.findAll();
    }





    @Override
    public List<Payment> findPaymentsByBill(
            Admin admin,
            long billId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(billId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid bill id."
            );
        }


        return paymentService.findByBillId(
                billId
        );
    }





    @Override
    public List<Payment> findPaymentsByCustomer(
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


        return paymentService.findByCustomerId(
                customerId
        );
    }

}