package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.AuditLogDAO;
import com.amdocs.telecom.model.AuditLog;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAOImpl implements AuditLogDAO {

    @Override
    public void save(AuditLog auditLog) {

        String sql =
                "INSERT INTO audit_logs " +
                        "(action, payment_id, bill_id, customer_id, " +
                        "action_date, details) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    auditLog.getAction()
            );

            statement.setLong(
                    2,
                    auditLog.getPaymentId()
            );

            statement.setLong(
                    3,
                    auditLog.getBillId()
            );

            statement.setLong(
                    4,
                    auditLog.getCustomerId()
            );

            if (auditLog.getActionDate() != null) {

                statement.setTimestamp(
                        5,
                        Timestamp.valueOf(
                                auditLog.getActionDate()
                        )
                );

            } else {

                statement.setTimestamp(
                        5,
                        null
                );
            }

            statement.setString(
                    6,
                    auditLog.getDetails()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    auditLog.setAuditId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save audit log.",
                    e
            );
        }
    }

    @Override
    public void save(
            AuditLog auditLog,
            Connection connection) {

        String sql =
                "INSERT INTO audit_logs " +
                        "(action, payment_id, bill_id, customer_id, " +
                        "action_date, details) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    auditLog.getAction()
            );

            statement.setLong(
                    2,
                    auditLog.getPaymentId()
            );

            statement.setLong(
                    3,
                    auditLog.getBillId()
            );

            statement.setLong(
                    4,
                    auditLog.getCustomerId()
            );

            if (auditLog.getActionDate() != null) {

                statement.setTimestamp(
                        5,
                        Timestamp.valueOf(
                                auditLog.getActionDate()
                        )
                );

            } else {

                statement.setTimestamp(
                        5,
                        null
                );
            }

            statement.setString(
                    6,
                    auditLog.getDetails()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    auditLog.setAuditId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save audit log in transaction.",
                    e
            );
        }
    }

    @Override
    public AuditLog findById(long auditId) {

        String sql =
                "SELECT * FROM audit_logs " +
                        "WHERE audit_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    auditId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToAuditLog(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find audit log.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<AuditLog> findByPaymentId(
            long paymentId) {

        String sql =
                "SELECT * FROM audit_logs " +
                        "WHERE payment_id = ? " +
                        "ORDER BY action_date DESC";

        List<AuditLog> auditLogs =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    paymentId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    auditLogs.add(
                            mapResultSetToAuditLog(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve payment audit logs.",
                    e
            );
        }

        return auditLogs;
    }

    @Override
    public List<AuditLog> findByBillId(
            long billId) {

        String sql =
                "SELECT * FROM audit_logs " +
                        "WHERE bill_id = ? " +
                        "ORDER BY action_date DESC";

        List<AuditLog> auditLogs =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    billId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    auditLogs.add(
                            mapResultSetToAuditLog(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve bill audit logs.",
                    e
            );
        }

        return auditLogs;
    }

    @Override
    public List<AuditLog> findByCustomerId(
            long customerId) {

        String sql =
                "SELECT * FROM audit_logs " +
                        "WHERE customer_id = ? " +
                        "ORDER BY action_date DESC";

        List<AuditLog> auditLogs =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    customerId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    auditLogs.add(
                            mapResultSetToAuditLog(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve customer audit logs.",
                    e
            );
        }

        return auditLogs;
    }

    @Override
    public List<AuditLog> findAll() {

        String sql =
                "SELECT * FROM audit_logs " +
                        "ORDER BY action_date DESC";

        List<AuditLog> auditLogs =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                auditLogs.add(
                        mapResultSetToAuditLog(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve audit logs.",
                    e
            );
        }

        return auditLogs;
    }

    private AuditLog mapResultSetToAuditLog(
            ResultSet resultSet) throws SQLException {

        Timestamp actionDate =
                resultSet.getTimestamp(
                        "action_date"
                );

        return new AuditLog(
                resultSet.getLong(
                        "audit_id"
                ),

                resultSet.getString(
                        "action"
                ),

                resultSet.getLong(
                        "payment_id"
                ),

                resultSet.getLong(
                        "bill_id"
                ),

                resultSet.getLong(
                        "customer_id"
                ),

                actionDate != null
                        ? actionDate.toLocalDateTime()
                        : null,

                resultSet.getString(
                        "details"
                )
        );
    }
}