package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.AuditLog;
import java.sql.Connection;

import java.util.List;

public interface AuditLogDAO {

    void save(AuditLog auditLog);

    void save(
            AuditLog auditLog,
            Connection connection
    );
    AuditLog findById(long auditId);

    List<AuditLog> findByPaymentId(long paymentId);

    List<AuditLog> findByBillId(long billId);

    List<AuditLog> findByCustomerId(long customerId);

    List<AuditLog> findAll();
}