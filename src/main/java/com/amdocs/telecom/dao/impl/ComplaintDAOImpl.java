package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.ComplaintDAO;
import com.amdocs.telecom.model.Complaint;
import com.amdocs.telecom.model.enums.ComplaintCategory;
import com.amdocs.telecom.model.enums.ComplaintStatus;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAOImpl implements ComplaintDAO {

    @Override
    public void save(Complaint complaint) {

        String sql =
                "INSERT INTO complaints " +
                        "(complaint_number, customer_id, subscription_id, " +
                        "category, description, priority, created_date, " +
                        "status, resolution) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    complaint.getComplaintNumber()
            );

            statement.setLong(
                    2,
                    complaint.getCustomerId()
            );

            statement.setLong(
                    3,
                    complaint.getSubscriptionId()
            );

            statement.setString(
                    4,
                    complaint.getCategory().name()
            );

            statement.setString(
                    5,
                    complaint.getDescription()
            );

            statement.setString(
                    6,
                    complaint.getPriority()
            );

            if (complaint.getCreatedDate() != null) {

                statement.setTimestamp(
                        7,
                        Timestamp.valueOf(
                                complaint.getCreatedDate()
                        )
                );

            } else {

                statement.setTimestamp(
                        7,
                        null
                );
            }

            statement.setString(
                    8,
                    complaint.getStatus().name()
            );

            statement.setString(
                    9,
                    complaint.getResolution()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    complaint.setComplaintId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save complaint.",
                    e
            );
        }
    }

    @Override
    public Complaint findById(
            long complaintId) {

        String sql =
                "SELECT * FROM complaints " +
                        "WHERE complaint_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    complaintId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToComplaint(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find complaint.",
                    e
            );
        }

        return null;
    }

    @Override
    public Complaint findByComplaintNumber(
            String complaintNumber) {

        String sql =
                "SELECT * FROM complaints " +
                        "WHERE complaint_number = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    complaintNumber
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToComplaint(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find complaint by number.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<Complaint> findByCustomerId(
            long customerId) {

        String sql =
                "SELECT * FROM complaints " +
                        "WHERE customer_id = ? " +
                        "ORDER BY created_date DESC";

        List<Complaint> complaints =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    customerId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    complaints.add(
                            mapResultSetToComplaint(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve customer complaints.",
                    e
            );
        }

        return complaints;
    }

    @Override
    public List<Complaint> findBySubscriptionId(
            long subscriptionId) {

        String sql =
                "SELECT * FROM complaints " +
                        "WHERE subscription_id = ? " +
                        "ORDER BY created_date DESC";

        List<Complaint> complaints =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    subscriptionId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    complaints.add(
                            mapResultSetToComplaint(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve subscription complaints.",
                    e
            );
        }

        return complaints;
    }

    @Override
    public List<Complaint> findAll() {

        String sql =
                "SELECT * FROM complaints " +
                        "ORDER BY created_date DESC";

        List<Complaint> complaints =
                new ArrayList<>();

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                complaints.add(
                        mapResultSetToComplaint(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve complaints.",
                    e
            );
        }

        return complaints;
    }

    @Override
    public void update(
            Complaint complaint) {

        String sql =
                "UPDATE complaints SET " +
                        "complaint_number = ?, " +
                        "customer_id = ?, " +
                        "subscription_id = ?, " +
                        "category = ?, " +
                        "description = ?, " +
                        "priority = ?, " +
                        "created_date = ?, " +
                        "status = ?, " +
                        "resolution = ? " +
                        "WHERE complaint_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    complaint.getComplaintNumber()
            );

            statement.setLong(
                    2,
                    complaint.getCustomerId()
            );

            statement.setLong(
                    3,
                    complaint.getSubscriptionId()
            );

            statement.setString(
                    4,
                    complaint.getCategory().name()
            );

            statement.setString(
                    5,
                    complaint.getDescription()
            );

            statement.setString(
                    6,
                    complaint.getPriority()
            );

            if (complaint.getCreatedDate() != null) {

                statement.setTimestamp(
                        7,
                        Timestamp.valueOf(
                                complaint.getCreatedDate()
                        )
                );

            } else {

                statement.setTimestamp(
                        7,
                        null
                );
            }

            statement.setString(
                    8,
                    complaint.getStatus().name()
            );

            statement.setString(
                    9,
                    complaint.getResolution()
            );

            statement.setLong(
                    10,
                    complaint.getComplaintId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update complaint.",
                    e
            );
        }
    }

    @Override
    public void delete(
            long complaintId) {

        String sql =
                "DELETE FROM complaints " +
                        "WHERE complaint_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    complaintId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete complaint.",
                    e
            );
        }
    }

    private Complaint mapResultSetToComplaint(
            ResultSet resultSet) throws SQLException {

        Timestamp createdDate =
                resultSet.getTimestamp(
                        "created_date"
                );

        return new Complaint(
                resultSet.getLong(
                        "complaint_id"
                ),

                resultSet.getString(
                        "complaint_number"
                ),

                resultSet.getLong(
                        "customer_id"
                ),

                resultSet.getLong(
                        "subscription_id"
                ),

                ComplaintCategory.valueOf(
                        resultSet.getString(
                                "category"
                        )
                ),

                resultSet.getString(
                        "description"
                ),

                resultSet.getString(
                        "priority"
                ),

                createdDate != null
                        ? createdDate.toLocalDateTime()
                        : null,

                ComplaintStatus.valueOf(
                        resultSet.getString(
                                "status"
                        )
                ),

                resultSet.getString(
                        "resolution"
                )
        );
    }
}