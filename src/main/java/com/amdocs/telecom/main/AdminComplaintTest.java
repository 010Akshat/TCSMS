package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.model.Complaint;
import com.amdocs.telecom.model.enums.ComplaintStatus;

import com.amdocs.telecom.model.enums.ComplaintStatus;

import com.amdocs.telecom.service.AdminAuthenticationService;
import com.amdocs.telecom.service.AdminComplaintService;
import com.amdocs.telecom.service.ComplaintService;

import com.amdocs.telecom.service.impl.AdminAuthenticationServiceImpl;
import com.amdocs.telecom.service.impl.AdminComplaintServiceImpl;
import com.amdocs.telecom.service.impl.ComplaintServiceImpl;


import java.util.List;


public class AdminComplaintTest {


    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) {


        AdminAuthenticationService authenticationService =
                new AdminAuthenticationServiceImpl();


        AdminComplaintService adminComplaintService =
                new AdminComplaintServiceImpl();


        ComplaintService complaintService =
                new ComplaintServiceImpl();


        System.out.println(
                "=== ADMIN COMPLAINT TEST SUITE ==="
        );


        Admin admin = null;


        // ==========================================
        // TEST 1: ADMIN LOGIN
        // ==========================================


        System.out.println(
                "\n=== TEST 1: ADMIN LOGIN ==="
        );


        try {


            admin =
                    authenticationService.login(
                            "admin",
                            "admin123"
                    );


            if (admin != null &&
                    admin.getAdminId() > 0) {


                pass(
                        "Admin login"
                );


                System.out.println(
                        "Welcome " +
                                admin.getFirstName()
                );


            } else {


                fail(
                        "Admin login"
                );
            }


        } catch (Exception e) {


            fail(
                    "Admin login: " +
                            e.getMessage()
            );
        }


        // ==========================================
        // CREATE TEST COMPLAINT
        // ==========================================


        Complaint complaint = null;


        long customerId = 3;
        long subscriptionId = 1;


        System.out.println(
                "\n=== CREATE TEST COMPLAINT ==="
        );


        try {


            complaint =
                    complaintService.createComplaint(
                            customerId,
                            subscriptionId,
                            "BILLING",
                            "Admin complaint testing.",
                            "HIGH"
                    );


            if (complaint != null &&
                    complaint.getComplaintId() > 0) {


                pass(
                        "Create complaint for admin test"
                );


                System.out.println(
                        "Complaint ID: " +
                                complaint.getComplaintId()
                );


            } else {


                fail(
                        "Create complaint for admin test"
                );
            }


        } catch (Exception e) {


            fail(
                    "Create complaint: " +
                            e.getMessage()
            );
        }


        long complaintId =
                complaint.getComplaintId();


        // ==========================================
        // TEST 2: VIEW ALL COMPLAINTS
        // ==========================================


        System.out.println(
                "\n=== TEST 2: VIEW ALL COMPLAINTS ==="
        );


        try {


            List<Complaint> complaints =
                    adminComplaintService.findAllComplaints(
                            admin
                    );


            boolean found =
                    complaints.stream()
                            .anyMatch(item ->
                                    item.getComplaintId()
                                            == complaintId
                            );


            if (found) {


                pass(
                        "View all complaints"
                );


            } else {


                fail(
                        "View all complaints"
                );
            }


        } catch (Exception e) {


            fail(
                    "View all complaints: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 3: FIND COMPLAINT BY ID
// ==========================================

        System.out.println(
                "\n=== TEST 3: FIND COMPLAINT BY ID ==="
        );


        try {

            Complaint found =
                    adminComplaintService.findComplaintById(
                            admin,
                            complaintId
                    );


            if(found != null &&
                    found.getComplaintId()
                            == complaintId) {


                pass(
                        "Find complaint by ID"
                );


            } else {


                fail(
                        "Find complaint by ID"
                );
            }


        } catch(Exception e) {


            fail(
                    "Find complaint by ID: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 4: FIND CUSTOMER COMPLAINTS
// ==========================================

        System.out.println(
                "\n=== TEST 4: FIND CUSTOMER COMPLAINTS ==="
        );


        try {


            List<Complaint> complaints =
                    adminComplaintService.findComplaintsByCustomer(
                            admin,
                            customerId
                    );


            boolean found =
                    complaints.stream()
                            .anyMatch(item ->
                                    item.getComplaintId()
                                            == complaintId
                            );


            if(found) {


                pass(
                        "Find customer complaints"
                );


            } else {


                fail(
                        "Find customer complaints"
                );
            }



        } catch(Exception e) {


            fail(
                    "Find customer complaints: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 5: RESOLVE COMPLAINT
// ==========================================

        System.out.println(
                "\n=== TEST 5: RESOLVE COMPLAINT ==="
        );


        try {


            adminComplaintService.resolveComplaint(
                    admin,
                    complaintId,
                    "Billing issue resolved by admin."
            );


            pass(
                    "Resolve complaint"
            );


        } catch(Exception e) {


            fail(
                    "Resolve complaint: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 6: VERIFY RESOLVED STATUS
// ==========================================

        System.out.println(
                "\n=== TEST 6: VERIFY RESOLVED STATUS ==="
        );


        try {


            Complaint resolved =
                    adminComplaintService.findComplaintById(
                            admin,
                            complaintId
                    );


            if(resolved != null &&
                    resolved.getStatus()
                            == ComplaintStatus.RESOLVED &&
                    "Billing issue resolved by admin."
                            .equals(
                                    resolved.getResolution()
                            )) {


                pass(
                        "Verify resolved status"
                );


            } else {


                fail(
                        "Verify resolved status"
                );
            }



        } catch(Exception e) {


            fail(
                    "Verify resolved status: " +
                            e.getMessage()
            );
        }

        // ==========================================
// TEST 7: CLOSE COMPLAINT
// ==========================================

        System.out.println(
                "\n=== TEST 7: CLOSE COMPLAINT ==="
        );


        try {

            adminComplaintService.closeComplaint(
                    admin,
                    complaintId
            );


            pass(
                    "Close complaint"
            );


        } catch(Exception e) {


            fail(
                    "Close complaint: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 8: VERIFY CLOSED STATUS
// ==========================================

        System.out.println(
                "\n=== TEST 8: VERIFY CLOSED STATUS ==="
        );


        try {


            Complaint closed =
                    adminComplaintService.findComplaintById(
                            admin,
                            complaintId
                    );


            if(closed != null &&
                    closed.getStatus()
                            == ComplaintStatus.CLOSED) {


                pass(
                        "Verify closed status"
                );


            } else {


                fail(
                        "Verify closed status"
                );
            }


        } catch(Exception e) {


            fail(
                    "Verify closed status: " +
                            e.getMessage()
            );
        }




// ==========================================
// TEST 9: NULL ADMIN REJECTION
// ==========================================

        System.out.println(
                "\n=== TEST 9: NULL ADMIN REJECTION ==="
        );


        try {


            adminComplaintService.findAllComplaints(
                    null
            );


            fail(
                    "Null admin rejection"
            );


        } catch(SecurityException e) {


            pass(
                    "Null admin rejection"
            );

        }

    }
    private static void pass(String testName) {

        passed++;

        System.out.println(
                testName +
                        ": PASSED"
        );
    }


    private static void fail(String testName) {

        failed++;

        System.out.println(
                testName +
                        ": FAILED"
        );
    }
}