package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Complaint;

import java.util.List;

public interface AdminComplaintService {


    List<Complaint> findAllComplaints(
            Admin admin
    );


    Complaint findComplaintById(
            Admin admin,
            long complaintId
    );


    List<Complaint> findComplaintsByCustomer(
            Admin admin,
            long customerId
    );


    void resolveComplaint(
            Admin admin,
            long complaintId,
            String resolution
    );


    void closeComplaint(
            Admin admin,
            long complaintId
    );

}