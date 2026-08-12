package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.Complaint;

import java.util.List;

public interface ComplaintDAO {

    void save(Complaint complaint);

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

    void delete(
            long complaintId
    );
}