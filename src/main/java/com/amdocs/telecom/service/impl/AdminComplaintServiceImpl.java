package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Complaint;
import com.amdocs.telecom.security.AdminAuthorizationUtil;
import com.amdocs.telecom.service.AdminComplaintService;
import com.amdocs.telecom.service.ComplaintService;

import java.util.List;


public class AdminComplaintServiceImpl
        implements AdminComplaintService {


    private final ComplaintService complaintService;


    public AdminComplaintServiceImpl() {

        this.complaintService =
                new ComplaintServiceImpl();

    }



    @Override
    public List<Complaint> findAllComplaints(
            Admin admin) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        return complaintService.findAll();
    }




    @Override
    public Complaint findComplaintById(
            Admin admin,
            long complaintId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        if(complaintId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid complaint id."
            );
        }


        Complaint complaint =
                complaintService.findById(
                        complaintId
                );


        if(complaint == null) {

            throw new IllegalArgumentException(
                    "Complaint not found."
            );
        }


        return complaint;
    }





    @Override
    public List<Complaint> findComplaintsByCustomer(
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


        return complaintService.findByCustomerId(
                customerId
        );
    }





    @Override
    public void resolveComplaint(
            Admin admin,
            long complaintId,
            String resolution) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        complaintService.resolveComplaint(
                admin,
                complaintId,
                resolution
        );
    }





    @Override
    public void closeComplaint(
            Admin admin,
            long complaintId) {


        AdminAuthorizationUtil.checkAdmin(
                admin
        );


        complaintService.closeComplaint(
                complaintId
        );
    }

}