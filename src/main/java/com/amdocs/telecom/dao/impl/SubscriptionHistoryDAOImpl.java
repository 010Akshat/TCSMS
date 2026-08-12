package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.SubscriptionHistoryDAO;
import com.amdocs.telecom.model.SubscriptionHistory;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionHistoryDAOImpl
        implements SubscriptionHistoryDAO {

    @Override
    public void save(SubscriptionHistory history) {

        String sql = "INSERT INTO subscription_history " +
                "(subscription_id, old_plan_id, new_plan_id, " +
                "change_date, change_reason, changed_by) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    history.getSubscriptionId()
            );

            statement.setLong(
                    2,
                    history.getOldPlanId()
            );

            statement.setLong(
                    3,
                    history.getNewPlanId()
            );

            statement.setTimestamp(
                    4,
                    Timestamp.valueOf(
                            history.getChangeDate()
                    )
            );

            statement.setString(
                    5,
                    history.getChangeReason()
            );

            statement.setString(
                    6,
                    history.getChangedBy()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    history.setHistoryId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to save subscription history.",
                    e
            );
        }
    }

    @Override
    public SubscriptionHistory findById(long historyId) {

        String sql =
                "SELECT * FROM subscription_history " +
                        "WHERE history_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, historyId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToHistory(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find subscription history.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<SubscriptionHistory> findBySubscriptionId(
            long subscriptionId) {

        String sql =
                "SELECT * FROM subscription_history " +
                        "WHERE subscription_id = ? " +
                        "ORDER BY change_date DESC";

        List<SubscriptionHistory> historyList =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, subscriptionId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    historyList.add(
                            mapResultSetToHistory(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve subscription history.",
                    e
            );
        }

        return historyList;
    }

    @Override
    public List<SubscriptionHistory> findAll() {

        String sql =
                "SELECT * FROM subscription_history " +
                        "ORDER BY change_date DESC";

        List<SubscriptionHistory> historyList =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                historyList.add(
                        mapResultSetToHistory(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve subscription history.",
                    e
            );
        }

        return historyList;
    }

    private SubscriptionHistory mapResultSetToHistory(
            ResultSet resultSet) throws SQLException {

        Timestamp changeDate =
                resultSet.getTimestamp("change_date");

        return new SubscriptionHistory(
                resultSet.getLong("history_id"),
                resultSet.getLong("subscription_id"),
                resultSet.getLong("old_plan_id"),
                resultSet.getLong("new_plan_id"),
                changeDate != null
                        ? changeDate.toLocalDateTime()
                        : null,
                resultSet.getString("change_reason"),
                resultSet.getString("changed_by")
        );
    }
}