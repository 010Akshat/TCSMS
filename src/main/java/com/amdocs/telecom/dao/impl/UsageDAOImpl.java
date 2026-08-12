package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.UsageDAO;
import com.amdocs.telecom.model.UsageRecord;
import com.amdocs.telecom.model.UsageType;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UsageDAOImpl implements UsageDAO {

    @Override
    public void save(UsageRecord usageRecord) {

        String sql =
                "INSERT INTO usage_records " +
                        "(subscription_id, usage_date, usage_type, " +
                        "quantity, unit, charge) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    usageRecord.getSubscriptionId()
            );

            statement.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            usageRecord.getUsageDate()
                    )
            );

            statement.setString(
                    3,
                    usageRecord.getUsageType().name()
            );

            statement.setBigDecimal(
                    4,
                    usageRecord.getQuantity()
            );

            statement.setString(
                    5,
                    usageRecord.getUnit()
            );

            statement.setBigDecimal(
                    6,
                    usageRecord.getCharge()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    usageRecord.setUsageId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to save usage record.",
                    e
            );
        }
    }

    @Override
    public UsageRecord findById(long usageId) {

        String sql =
                "SELECT * FROM usage_records " +
                        "WHERE usage_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, usageId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToUsageRecord(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find usage record.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<UsageRecord> findBySubscriptionId(
            long subscriptionId) {

        String sql =
                "SELECT * FROM usage_records " +
                        "WHERE subscription_id = ? " +
                        "ORDER BY usage_date DESC";

        List<UsageRecord> usageRecords =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, subscriptionId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    usageRecords.add(
                            mapResultSetToUsageRecord(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve usage records.",
                    e
            );
        }

        return usageRecords;
    }

    @Override
    public List<UsageRecord> findAll() {

        String sql =
                "SELECT * FROM usage_records " +
                        "ORDER BY usage_date DESC";

        List<UsageRecord> usageRecords =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                usageRecords.add(
                        mapResultSetToUsageRecord(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve usage records.",
                    e
            );
        }

        return usageRecords;
    }

    @Override
    public void update(UsageRecord usageRecord) {

        String sql =
                "UPDATE usage_records SET " +
                        "subscription_id = ?, " +
                        "usage_date = ?, " +
                        "usage_type = ?, " +
                        "quantity = ?, " +
                        "unit = ?, " +
                        "charge = ? " +
                        "WHERE usage_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    usageRecord.getSubscriptionId()
            );

            statement.setTimestamp(
                    2,
                    Timestamp.valueOf(
                            usageRecord.getUsageDate()
                    )
            );

            statement.setString(
                    3,
                    usageRecord.getUsageType().name()
            );

            statement.setBigDecimal(
                    4,
                    usageRecord.getQuantity()
            );

            statement.setString(
                    5,
                    usageRecord.getUnit()
            );

            statement.setBigDecimal(
                    6,
                    usageRecord.getCharge()
            );

            statement.setLong(
                    7,
                    usageRecord.getUsageId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update usage record.",
                    e
            );
        }
    }

    @Override
    public void delete(long usageId) {

        String sql =
                "DELETE FROM usage_records " +
                        "WHERE usage_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, usageId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete usage record.",
                    e
            );
        }
    }

    private UsageRecord mapResultSetToUsageRecord(
            ResultSet resultSet) throws SQLException {

        return new UsageRecord(
                resultSet.getLong("usage_id"),
                resultSet.getLong("subscription_id"),
                resultSet.getTimestamp("usage_date")
                        .toLocalDateTime(),
                UsageType.valueOf(
                        resultSet.getString("usage_type")
                ),
                resultSet.getBigDecimal("quantity"),
                resultSet.getString("unit"),
                resultSet.getBigDecimal("charge")
        );
    }
}