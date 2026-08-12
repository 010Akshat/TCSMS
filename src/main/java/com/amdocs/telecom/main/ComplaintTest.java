package com.amdocs.telecom.main;

import com.amdocs.telecom.model.Complaint;
import com.amdocs.telecom.model.enums.ComplaintCategory;
import com.amdocs.telecom.model.enums.ComplaintStatus;
import com.amdocs.telecom.service.ComplaintService;
import com.amdocs.telecom.service.impl.ComplaintServiceImpl;

import java.util.List;

public class ComplaintTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        ComplaintService complaintService =
                new ComplaintServiceImpl();

        System.out.println(
                "=== COMPLAINT TEST SUITE ==="
        );

        long customerId = 3;
        long subscriptionId = 1;

        Complaint complaint = null;

        // ==========================================
        // TEST 1: CREATE COMPLAINT
        // ==========================================

        System.out.println(
                "\n=== TEST 1: CREATE COMPLAINT ==="
        );

        try {

            complaint =
                    complaintService.createComplaint(
                            customerId,
                            subscriptionId,
                            "BILLING",
                            "Incorrect billing amount.",
                            "HIGH"
                    );

            if (complaint != null &&
                    complaint.getComplaintId() > 0 &&
                    complaint.getStatus()
                            == ComplaintStatus.OPEN) {

                pass(
                        "Complaint creation"
                );

                printComplaint(
                        complaint
                );

            } else {

                fail(
                        "Complaint creation"
                );
            }

        } catch (Exception e) {

            fail(
                    "Complaint creation: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 2: FIND BY ID
        // ==========================================

        System.out.println(
                "\n=== TEST 2: FIND COMPLAINT BY ID ==="
        );

        try {

            Complaint found =
                    complaintService.findById(
                            complaint.getComplaintId()
                    );

            if (found != null &&
                    found.getComplaintId()
                            == complaint.getComplaintId()) {

                pass(
                        "Find complaint by ID"
                );

            } else {

                fail(
                        "Find complaint by ID"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find complaint by ID: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 3: FIND BY COMPLAINT NUMBER
        // ==========================================

        System.out.println(
                "\n=== TEST 3: FIND BY COMPLAINT NUMBER ==="
        );

        try {

            Complaint found =
                    complaintService
                            .findByComplaintNumber(
                                    complaint
                                            .getComplaintNumber()
                            );

            if (found != null &&
                    found.getComplaintNumber()
                            .equals(
                                    complaint
                                            .getComplaintNumber()
                            )) {

                pass(
                        "Find complaint by number"
                );

            } else {

                fail(
                        "Find complaint by number"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find complaint by number: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 4: FIND BY CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 4: FIND COMPLAINTS BY CUSTOMER ==="
        );

        try {

            List<Complaint> complaints =
                    complaintService
                            .findByCustomerId(
                                    customerId
                            );

            final long complaintId =
                    complaint.getComplaintId();

            boolean found =
                    complaints.stream()
                            .anyMatch(existing ->
                                    existing.getComplaintId()
                                            == complaintId
                            );

            if (found) {

                pass(
                        "Find complaints by customer"
                );

                System.out.println(
                        "Complaints found: " +
                                complaints.size()
                );

            } else {

                fail(
                        "Find complaints by customer"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find complaints by customer: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 5: FIND BY SUBSCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 5: FIND COMPLAINTS BY SUBSCRIPTION ==="
        );

        try {

            List<Complaint> complaints =
                    complaintService
                            .findBySubscriptionId(
                                    subscriptionId
                            );

            final long complaintId =
                    complaint.getComplaintId();

            boolean found =
                    complaints.stream()
                            .anyMatch(existing ->
                                    existing.getComplaintId()
                                            == complaintId
                            );

            if (found) {

                pass(
                        "Find complaints by subscription"
                );

            } else {

                fail(
                        "Find complaints by subscription"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find complaints by subscription: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 6: INVALID CATEGORY
        // ==========================================

        System.out.println(
                "\n=== TEST 6: INVALID CATEGORY ==="
        );

        try {

            complaintService.createComplaint(
                    customerId,
                    subscriptionId,
                    "INVALID",
                    "Invalid category test.",
                    "LOW"
            );

            fail(
                    "Invalid category rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Invalid complaint category"
                    )) {

                pass(
                        "Invalid category rejection"
                );

                System.out.println(
                        "Reason: " +
                                e.getMessage()
                );

            } else {

                fail(
                        "Invalid category rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 7: BLANK DESCRIPTION
        // ==========================================

        System.out.println(
                "\n=== TEST 7: BLANK DESCRIPTION ==="
        );

        try {

            complaintService.createComplaint(
                    customerId,
                    subscriptionId,
                    "NETWORK",
                    "",
                    "MEDIUM"
            );

            fail(
                    "Blank description rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Complaint description is mandatory"
                    )) {

                pass(
                        "Blank description rejection"
                );

            } else {

                fail(
                        "Blank description rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 8: INVALID CUSTOMER
        // ==========================================

        System.out.println(
                "\n=== TEST 8: INVALID CUSTOMER ==="
        );

        try {

            complaintService.createComplaint(
                    99999,
                    subscriptionId,
                    "BILLING",
                    "Invalid customer test.",
                    "LOW"
            );

            fail(
                    "Invalid customer rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "Customer not found"
                    )) {

                pass(
                        "Invalid customer rejection"
                );

            } else {

                fail(
                        "Invalid customer rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 9: CUSTOMER-SUBSCRIPTION MISMATCH
        // ==========================================

        System.out.println(
                "\n=== TEST 9: CUSTOMER-SUBSCRIPTION MISMATCH ==="
        );

        try {

            complaintService.createComplaint(
                    2,
                    subscriptionId,
                    "SIM",
                    "Customer subscription mismatch.",
                    "MEDIUM"
            );

            fail(
                    "Customer-subscription mismatch rejection"
            );

        } catch (IllegalArgumentException e) {

            if (e.getMessage() != null &&
                    e.getMessage().contains(
                            "does not belong to customer"
                    )) {

                pass(
                        "Customer-subscription mismatch rejection"
                );

            } else {

                fail(
                        "Customer-subscription mismatch rejection: " +
                                e.getMessage()
                );
            }
        }

        // ==========================================
        // TEST 10: RESOLVE COMPLAINT
        // ==========================================

        System.out.println(
                "\n=== TEST 10: RESOLVE COMPLAINT ==="
        );

        try {

            complaintService.resolveComplaint(
                    complaint.getComplaintId(),
                    "Billing amount corrected."
            );

            Complaint resolved =
                    complaintService.findById(
                            complaint.getComplaintId()
                    );

            if (resolved != null &&
                    resolved.getStatus()
                            == ComplaintStatus.RESOLVED &&
                    "Billing amount corrected."
                            .equals(
                                    resolved.getResolution()
                            )) {

                pass(
                        "Complaint resolution"
                );

                System.out.println(
                        "Status: " +
                                resolved.getStatus()
                );

                System.out.println(
                        "Resolution: " +
                                resolved.getResolution()
                );

            } else {

                fail(
                        "Complaint resolution"
                );
            }

        } catch (Exception e) {

            fail(
                    "Complaint resolution: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 11: CLOSE COMPLAINT
        // ==========================================

        System.out.println(
                "\n=== TEST 11: CLOSE COMPLAINT ==="
        );

        try {

            complaintService.closeComplaint(
                    complaint.getComplaintId()
            );

            Complaint closed =
                    complaintService.findById(
                            complaint.getComplaintId()
                    );

            if (closed != null &&
                    closed.getStatus()
                            == ComplaintStatus.CLOSED) {

                pass(
                        "Complaint closure"
                );

            } else {

                fail(
                        "Complaint closure"
                );
            }

        } catch (Exception e) {

            fail(
                    "Complaint closure: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 12: FIND ALL COMPLAINTS
        // ==========================================

        System.out.println(
                "\n=== TEST 12: FIND ALL COMPLAINTS ==="
        );

        try {

            List<Complaint> complaints =
                    complaintService.findAll();

            final long complaintId =
                    complaint.getComplaintId();

            boolean found =
                    complaints.stream()
                            .anyMatch(existing ->
                                    existing.getComplaintId()
                                            == complaintId
                            );

            if (found) {

                pass(
                        "Find all complaints"
                );

                System.out.println(
                        "Total complaints: " +
                                complaints.size()
                );

            } else {

                fail(
                        "Find all complaints"
                );
            }

        } catch (Exception e) {

            fail(
                    "Find all complaints: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 13: UPDATE COMPLAINT
        // ==========================================

        System.out.println(
                "\n=== TEST 13: UPDATE COMPLAINT ==="
        );

        try {

            complaint.setPriority(
                    "LOW"
            );

            complaint.setDescription(
                    "Updated billing complaint description."
            );

            complaintService.update(
                    complaint
            );

            Complaint updated =
                    complaintService.findById(
                            complaint.getComplaintId()
                    );

            if (updated != null &&
                    "LOW".equals(
                            updated.getPriority()
                    ) &&
                    "Updated billing complaint description."
                            .equals(
                                    updated.getDescription()
                            )) {

                pass(
                        "Complaint update"
                );

            } else {

                fail(
                        "Complaint update"
                );
            }

        } catch (Exception e) {

            fail(
                    "Complaint update: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // TEST 14: DELETE COMPLAINT
        // ==========================================

        System.out.println(
                "\n=== TEST 14: DELETE COMPLAINT ==="
        );

        try {

            long complaintId =
                    complaint.getComplaintId();

            complaintService.delete(
                    complaintId
            );

            Complaint deleted =
                    complaintService.findById(
                            complaintId
                    );

            if (deleted == null) {

                pass(
                        "Complaint deletion"
                );

            } else {

                fail(
                        "Complaint deletion"
                );
            }

        } catch (Exception e) {

            fail(
                    "Complaint deletion: " +
                            e.getMessage()
            );
        }

        // ==========================================
        // FINAL RESULT
        // ==========================================

        System.out.println(
                "\n=========================================="
        );

        System.out.println(
                "TOTAL PASSED: " +
                        passed
        );

        System.out.println(
                "TOTAL FAILED: " +
                        failed
        );

        System.out.println(
                "=========================================="
        );

        if (failed == 0) {

            System.out.println(
                    "COMPLAINT TEST SUITE: PASSED"
            );

        } else {

            System.out.println(
                    "COMPLAINT TEST SUITE: FAILED"
            );
        }
    }

    private static void printComplaint(
            Complaint complaint) {

        System.out.println(
                "Complaint ID: " +
                        complaint.getComplaintId()
        );

        System.out.println(
                "Complaint Number: " +
                        complaint.getComplaintNumber()
        );

        System.out.println(
                "Customer ID: " +
                        complaint.getCustomerId()
        );

        System.out.println(
                "Subscription ID: " +
                        complaint.getSubscriptionId()
        );

        System.out.println(
                "Category: " +
                        complaint.getCategory()
        );

        System.out.println(
                "Description: " +
                        complaint.getDescription()
        );

        System.out.println(
                "Priority: " +
                        complaint.getPriority()
        );

        System.out.println(
                "Created Date: " +
                        complaint.getCreatedDate()
        );

        System.out.println(
                "Status: " +
                        complaint.getStatus()
        );

        System.out.println(
                "Resolution: " +
                        complaint.getResolution()
        );
    }

    private static void pass(
            String testName) {

        passed++;

        System.out.println(
                testName +
                        ": PASSED"
        );
    }

    private static void fail(
            String testName) {

        failed++;

        System.out.println(
                testName +
                        ": FAILED"
        );
    }
}