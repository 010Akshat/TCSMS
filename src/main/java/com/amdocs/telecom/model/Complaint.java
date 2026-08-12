package com.amdocs.telecom.model;

import java.time.LocalDateTime;
import com.amdocs.telecom.model.enums.ComplaintCategory;
import com.amdocs.telecom.model.enums.ComplaintStatus;

public class Complaint {

    private long complaintId;
    private String complaintNumber;
    private long customerId;
    private long subscriptionId;
    private ComplaintCategory category;
    private String description;
    private String priority;
    private LocalDateTime createdDate;
    private ComplaintStatus status;
    private String resolution;

    public Complaint(
            long complaintId,
            String complaintNumber,
            long customerId,
            long subscriptionId,
            ComplaintCategory category,
            String description,
            String priority,
            LocalDateTime createdDate,
            ComplaintStatus status,
            String resolution) {

        this.complaintId = complaintId;
        this.complaintNumber = complaintNumber;
        this.customerId = customerId;
        this.subscriptionId = subscriptionId;
        this.category = category;
        this.description = description;
        this.priority = priority;
        this.createdDate = createdDate;
        this.status = status;
        this.resolution = resolution;
    }

    public long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(long complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public ComplaintCategory getCategory() {
        return category;
    }

    public void setCategory(ComplaintCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}