package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.LoginHistoryDAO;
import com.amdocs.telecom.model.LoginHistory;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LoginHistoryDAOImpl implements LoginHistoryDAO {

    @Override
    public void save(LoginHistory loginHistory) {

        String sql = "INSERT INTO login_history " +
                "(customer_id, login_time, login_status) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, loginHistory.getCustomerId());
            statement.setTimestamp(
                    2,
                    Timestamp.valueOf(loginHistory.getLoginTime())
            );
            statement.setString(3, loginHistory.getLoginStatus());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to save login history.",
                    e
            );
        }
    }

    @Override
    public List<LoginHistory> findByCustomerId(long customerId) {

        String sql = "SELECT * FROM login_history " +
                "WHERE customer_id = ? " +
                "ORDER BY login_time DESC";

        List<LoginHistory> historyList = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    LoginHistory loginHistory = new LoginHistory(
                            resultSet.getLong("login_history_id"),
                            resultSet.getLong("customer_id"),
                            resultSet.getTimestamp("login_time")
                                    .toLocalDateTime(),
                            resultSet.getString("login_status")
                    );

                    historyList.add(loginHistory);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve login history.",
                    e
            );
        }

        return historyList;
    }

    @Override
    public void deleteByCustomerId(long customerId) {

        String sql =
                "DELETE FROM login_history " +
                        "WHERE customer_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    customerId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete customer login history.",
                    e
            );
        }
    }
}