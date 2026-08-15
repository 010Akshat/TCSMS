package com.amdocs.telecom.main;

import com.amdocs.telecom.dao.AdminDAO;
import com.amdocs.telecom.dao.impl.AdminDAOImpl;
import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.security.PasswordUtil;
import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;

public class AdminAuthTest {


    public static void main(String[] args) {


        System.out.println(
                "=== ADMIN AUTHENTICATION TEST ==="
        );


        AdminDAO adminDAO =
                new AdminDAOImpl();


        // Create admin only once
        Admin admin =
                new Admin(
                        0,
                        "admin",
                        PasswordUtil.hashPassword(
                                "admin123"
                        ),
                        "System",
                        "Administrator",
                        "admin@amdocs.com",
                        "ACTIVE",
                        null
                );


        adminDAO.save(admin);


        System.out.println(
                "Admin created. ID: "
                        + admin.getAdminId()
        );



        AdminAuthenticationService service =
                new AdminAuthenticationServiceImpl();



        // SUCCESS LOGIN

        try {

            Admin loggedInAdmin =
                    service.login(
                            "admin",
                            "admin123"
                    );


            System.out.println(
                    "Admin login successful!"
            );


            System.out.println(
                    "Welcome "
                            + loggedInAdmin.getFirstName()
            );


        } catch(Exception e) {

            System.out.println(
                    "Admin login failed."
            );
        }



        // WRONG PASSWORD

        try {

            service.login(
                    "admin",
                    "wrong123"
            );


            System.out.println(
                    "Wrong password test FAILED"
            );


        } catch(Exception e) {


            System.out.println(
                    "Wrong password rejection PASSED"
            );
        }


        System.out.println(
                "=============================="
        );
    }
}