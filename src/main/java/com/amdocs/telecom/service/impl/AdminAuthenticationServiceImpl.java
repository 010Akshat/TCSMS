package com.amdocs.telecom.service.impl;

import com.amdocs.telecom.dao.AdminDAO;
import com.amdocs.telecom.dao.impl.AdminDAOImpl;
import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.security.PasswordUtil;
import com.amdocs.telecom.service.AdminAuthenticationService;


public class AdminAuthenticationServiceImpl
        implements AdminAuthenticationService {


    private final AdminDAO adminDAO;


    public AdminAuthenticationServiceImpl() {

        this.adminDAO =
                new AdminDAOImpl();
    }


    @Override
    public Admin login(
            String username,
            String password) {


        if(username == null ||
                username.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Admin username is required."
            );
        }


        if(password == null ||
                password.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Admin password is required."
            );
        }


        Admin admin =
                adminDAO.findByUsername(
                        username.trim()
                );


        if(admin == null) {

            throw new IllegalArgumentException(
                    "Invalid admin username or password."
            );
        }


        if(!"ACTIVE".equalsIgnoreCase(
                admin.getAdminStatus()
        )) {

            throw new IllegalStateException(
                    "Admin account is inactive."
            );
        }


        boolean validPassword =
                PasswordUtil.verifyPassword(
                        password,
                        admin.getPasswordHash()
                );


        if(!validPassword) {

            throw new IllegalArgumentException(
                    "Invalid admin username or password."
            );
        }


        return admin;
    }



    @Override
    public void logout(long adminId) {

        if(adminId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid admin id."
            );
        }

        // Stateless logout.
        // Session/token handling will be done
        // when frontend integration is added.
    }
}