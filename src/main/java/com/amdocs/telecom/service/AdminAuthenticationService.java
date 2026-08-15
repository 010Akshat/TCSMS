package com.amdocs.telecom.service;

import com.amdocs.telecom.model.Admin;

public interface AdminAuthenticationService {


    Admin login(
            String username,
            String password
    );


    void logout(
            long adminId
    );

}