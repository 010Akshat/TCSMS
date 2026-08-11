package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.PasswordOTPDAO;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PasswordOTPDAOImpl implements PasswordOTPDAO {

    @Override
    public void save(PasswordOTP passwordOTP) {

        String sql = "INSERT INTO password_otp " +
                "(customer_id, otp_code, created_at, expires_at, used) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, passwordOTP.getCustomerId());
            statement.setString(2, passwordOTP.getOtpCode());

            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(passwordOTP.getCreatedAt())
            );

            statement.setTimestamp(
                    4,
                    Timestamp.valueOf(passwordOTP.getExpiresAt())
            );

            statement.setBoolean(5, passwordOTP.isUsed());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database operation failed.",
                    e
            );
        }
    }

    @Override
    public PasswordOTP findLatestByCustomerId(long customerId) {

        String sql = "SELECT * FROM password_otp " +
                "WHERE customer_id = ? " +
                "ORDER BY created_at DESC " +
                "LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new PasswordOTP(
                            resultSet.getLong("otp_id"),
                            resultSet.getLong("customer_id"),
                            resultSet.getString("otp_code"),
                            resultSet.getTimestamp("created_at")
                                    .toLocalDateTime(),
                            resultSet.getTimestamp("expires_at")
                                    .toLocalDateTime(),
                            resultSet.getBoolean("used")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database operation failed.",
                    e
            );
        }

        return null;
    }

    @Override
    public void markAsUsed(long otpId) {

        String sql = "UPDATE password_otp " +
                "SET used = TRUE " +
                "WHERE otp_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, otpId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Database operation failed.",
                    e
            );
        }
    }
}