package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.ComplaintDAO;
import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.dao.impl.ComplaintDAOImpl;
import com.amdocs.telecom.dao.impl.CustomerDAOImpl;
import com.amdocs.telecom.dao.impl.SubscriptionDAOImpl;
import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Complaint;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.enums.ComplaintCategory;
import com.amdocs.telecom.model.enums.ComplaintStatus;
import com.amdocs.telecom.service.ComplaintService;

import java.time.LocalDateTime;
import java.util.List;
import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
public class ComplaintServiceImpl
        implements ComplaintService {

    private final ComplaintDAO complaintDAO;
    private final CustomerDAO customerDAO;
    private final SubscriptionDAO subscriptionDAO;

    public ComplaintServiceImpl() {

        this.complaintDAO =
                new ComplaintDAOImpl();

        this.customerDAO =
                new CustomerDAOImpl();

        this.subscriptionDAO =
                new SubscriptionDAOImpl();
    }

    @Override
    public Complaint createComplaint(
            long customerId,
            long subscriptionId,
            String category,
            String description,
            String priority) {

        // ==========================================
        // 1. VALIDATE CUSTOMER
        // ==========================================

        Customer customer =
                customerDAO.findById(
                        customerId
                );

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Customer not found."
            );
        }

        // ==========================================
        // 2. VALIDATE SUBSCRIPTION
        // ==========================================

        MobileSubscription subscription =
                subscriptionDAO.findById(
                        subscriptionId
                );

        if (subscription == null) {

            throw new IllegalArgumentException(
                    "Subscription not found."
            );
        }

        // ==========================================
        // 3. VALIDATE CUSTOMER-SUBSCRIPTION
        // ==========================================

        if (subscription.getCustomerId()
                != customerId) {

            throw new IllegalArgumentException(
                    "Subscription does not belong to customer."
            );
        }

        // ==========================================
        // 4. VALIDATE CATEGORY
        // ==========================================

        if (category == null ||
                category.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint category is mandatory."
            );
        }

        ComplaintCategory categoryValue;

        try {

            categoryValue =
                    ComplaintCategory.valueOf(
                            category.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid complaint category."
            );
        }

        // ==========================================
        // 5. VALIDATE DESCRIPTION
        // ==========================================

        if (description == null ||
                description.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint description is mandatory."
            );
        }

        // ==========================================
        // 6. VALIDATE PRIORITY
        // ==========================================

        if (priority == null ||
                priority.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint priority is mandatory."
            );
        }

        // ==========================================
        // 7. GENERATE COMPLAINT NUMBER
        // ==========================================

        String complaintNumber =
                generateComplaintNumber();

        // ==========================================
        // 8. CREATE COMPLAINT
        // ==========================================

        Complaint complaint =
                new Complaint(
                        0,
                        complaintNumber,
                        customerId,
                        subscriptionId,
                        categoryValue,
                        description.trim(),
                        priority.trim().toUpperCase(),
                        LocalDateTime.now(),
                        ComplaintStatus.OPEN,
                        null
                );

        // ==========================================
        // 9. SAVE
        // ==========================================

        complaintDAO.save(
                complaint
        );

        return complaint;
    }

    @Override
    public Complaint findById(
            long complaintId) {

        return complaintDAO.findById(
                complaintId
        );
    }

    @Override
    public Complaint findByComplaintNumber(
            String complaintNumber) {

        return complaintDAO.findByComplaintNumber(
                complaintNumber
        );
    }

    @Override
    public List<Complaint> findByCustomerId(
            long customerId) {

        return complaintDAO.findByCustomerId(
                customerId
        );
    }

    @Override
    public List<Complaint> findBySubscriptionId(
            long subscriptionId) {

        return complaintDAO.findBySubscriptionId(
                subscriptionId
        );
    }

    @Override
    public List<Complaint> findAll() {

        return complaintDAO.findAll();
    }

    @Override
    public void update(
            Complaint complaint) {

        if (complaint == null) {

            throw new IllegalArgumentException(
                    "Complaint cannot be null."
            );
        }

        if (complaint.getComplaintId() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid complaint ID."
            );
        }

        complaintDAO.update(
                complaint
        );
    }

    @Override
    public void resolveComplaint(
            Admin admin, long complaintId,
            String resolution) {

        Complaint complaint =
                complaintDAO.findById(
                        complaintId
                );

        if (complaint == null) {

            throw new IllegalArgumentException(
                    "Complaint not found."
            );
        }

        if (resolution == null ||
                resolution.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Resolution is mandatory."
            );
        }

        complaint.setResolution(
                resolution.trim()
        );

        complaint.setStatus(
                ComplaintStatus.RESOLVED
        );

        complaintDAO.update(
                complaint
        );
    }

    @Override
    public void closeComplaint(
            long complaintId) {

        Complaint complaint =
                complaintDAO.findById(
                        complaintId
                );

        if (complaint == null) {

            throw new IllegalArgumentException(
                    "Complaint not found."
            );
        }

        complaint.setStatus(
                ComplaintStatus.CLOSED
        );

        complaintDAO.update(
                complaint
        );
    }

    @Override
    public void delete(
            long complaintId) {

        Complaint complaint =
                complaintDAO.findById(
                        complaintId
                );

        if (complaint == null) {

            throw new IllegalArgumentException(
                    "Complaint not found."
            );
        }

        complaintDAO.delete(
                complaintId
        );
    }

    private String generateComplaintNumber() {

        List<Complaint> complaints =
                complaintDAO.findAll();

        long nextNumber =
                10001L + complaints.size();

        String complaintNumber;

        do {

            complaintNumber =
                    "CMP-" +
                            nextNumber;

            nextNumber++;

        } while (
                complaintDAO
                        .findByComplaintNumber(
                                complaintNumber
                        ) != null
        );

        return complaintNumber;
    }
}