package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Complaint;

import java.util.List;

public interface ComplaintService {

    Complaint createComplaint(
            long customerId,
            long subscriptionId,
            String category,
            String description,
            String priority
    );

    Complaint findById(
            long complaintId
    );

    Complaint findByComplaintNumber(
            String complaintNumber
    );

    List<Complaint> findByCustomerId(
            long customerId
    );

    List<Complaint> findBySubscriptionId(
            long subscriptionId
    );

    List<Complaint> findAll();

    void update(
            Complaint complaint
    );

    void resolveComplaint(
            Admin admin, long complaintId,
            String resolution
    );

    void closeComplaint(
            long complaintId
    );

    void delete(
            long complaintId
    );
}