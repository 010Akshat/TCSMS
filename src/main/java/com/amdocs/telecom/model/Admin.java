package com.amdocs.telecom.model;

import java.time.LocalDateTime;

public class Admin {

    private long adminId;
    private String adminUsername;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String email;
    private String adminStatus;
    private LocalDateTime createdAt;


    public Admin(long adminId,
                 String adminUsername,
                 String passwordHash,
                 String firstName,
                 String lastName,
                 String email,
                 String adminStatus,
                 LocalDateTime createdAt) {

        this.adminId = adminId;
        this.adminUsername = adminUsername;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.adminStatus = adminStatus;
        this.createdAt = createdAt;
    }


    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }


    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }


    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}