package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.PasswordOTPDAO;
import com.amdocs.telecom.model.PasswordOTP;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class PasswordOTPDAOImpl implements PasswordOTPDAO {

    @Override
    public void save(PasswordOTP passwordOTP) {

        String sql =
                "INSERT INTO password_otp " +
                        "(customer_id, otp_code, created_at, expires_at, used) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    passwordOTP.getCustomerId()
            );

            statement.setString(
                    2,
                    passwordOTP.getOtpCode()
            );

            statement.setTimestamp(
                    3,
                    Timestamp.valueOf(
                            passwordOTP.getCreatedAt()
                    )
            );

            statement.setTimestamp(
                    4,
                    Timestamp.valueOf(
                            passwordOTP.getExpiresAt()
                    )
            );

            statement.setBoolean(
                    5,
                    passwordOTP.isUsed()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    passwordOTP.setOtpId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save password OTP.",
                    e
            );
        }
    }

    @Override
    public PasswordOTP findLatestByCustomerId(
            long customerId) {

        String sql =
                "SELECT * FROM password_otp " +
                        "WHERE customer_id = ? " +
                        "ORDER BY created_at DESC, otp_id DESC " +
                        "LIMIT 1";

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

                if (resultSet.next()) {

                    return new PasswordOTP(
                            resultSet.getLong(
                                    "otp_id"
                            ),

                            resultSet.getLong(
                                    "customer_id"
                            ),

                            resultSet.getString(
                                    "otp_code"
                            ),

                            resultSet.getTimestamp(
                                    "created_at"
                            ).toLocalDateTime(),

                            resultSet.getTimestamp(
                                    "expires_at"
                            ).toLocalDateTime(),

                            resultSet.getBoolean(
                                    "used"
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find latest password OTP.",
                    e
            );
        }

        return null;
    }

    @Override
    public void markAsUsed(
            long otpId) {

        String sql =
                "UPDATE password_otp " +
                        "SET used = TRUE " +
                        "WHERE otp_id = ?";

        try (Connection connection =
                     DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    otpId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to mark password OTP as used.",
                    e
            );
        }


    }

    @Override
    public void deleteByCustomerId(long customerId) {

        String sql =
                "DELETE FROM password_otp " +
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
                    "Failed to delete customer OTP records.",
                    e
            );
        }
    }
}