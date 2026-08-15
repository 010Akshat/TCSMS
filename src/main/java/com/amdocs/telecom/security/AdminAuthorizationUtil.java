package com.amdocs.telecom.security;

import com.amdocs.telecom.model.Admin;

public class AdminAuthorizationUtil {


    public static void checkAdmin(Admin admin) {

        if(admin == null) {

            throw new SecurityException(
                    "Admin authentication required."
            );
        }


        if(!"ACTIVE".equalsIgnoreCase(
                admin.getAdminStatus()
        )) {

            throw new SecurityException(
                    "Inactive admin cannot perform this action."
            );
        }
    }
}