package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.SubscriptionDAO;
import com.amdocs.telecom.model.MobileSubscription;
import com.amdocs.telecom.model.enums.SimType;
import com.amdocs.telecom.model.enums.SubscriptionStatus;
import com.amdocs.telecom.model.enums.SubscriptionType;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAOImpl implements SubscriptionDAO {

    @Override
    public void save(MobileSubscription subscription) {

        String sql = "INSERT INTO mobile_subscriptions " +
                "(subscription_number, customer_id, plan_id, " +
                "mobile_number, sim_number, sim_type, activation_date, " +
                "subscription_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    subscription.getSubscriptionNumber()
            );

            statement.setLong(
                    2,
                    subscription.getCustomerId()
            );

            statement.setLong(
                    3,
                    subscription.getPlanId()
            );

            statement.setString(
                    4,
                    subscription.getMobileNumber()
            );

            statement.setString(
                    5,
                    subscription.getSimNumber()
            );

            statement.setString(
                    6,
                    subscription.getSimType().name()
            );

            statement.setDate(
                    7,
                    Date.valueOf(
                            subscription.getActivationDate()
                    )
            );

            statement.setString(
                    8,
                    subscription.getSubscriptionType().name()
            );

            statement.setString(
                    9,
                    subscription.getStatus().name()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    subscription.setSubscriptionId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to save mobile subscription.",
                    e
            );
        }
    }

    @Override
    public MobileSubscription findById(long subscriptionId) {

        String sql =
                "SELECT * FROM mobile_subscriptions " +
                        "WHERE subscription_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, subscriptionId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToSubscription(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find mobile subscription.",
                    e
            );
        }

        return null;
    }

    @Override
    public MobileSubscription findBySubscriptionNumber(
            String subscriptionNumber) {

        String sql =
                "SELECT * FROM mobile_subscriptions " +
                        "WHERE subscription_number = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    subscriptionNumber
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToSubscription(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find mobile subscription.",
                    e
            );
        }

        return null;
    }

    @Override
    public MobileSubscription findByMobileNumber(
            String mobileNumber) {

        String sql =
                "SELECT * FROM mobile_subscriptions " +
                        "WHERE mobile_number = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    mobileNumber
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToSubscription(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find mobile subscription.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<MobileSubscription> findByCustomerId(
            long customerId) {

        String sql =
                "SELECT * FROM mobile_subscriptions " +
                        "WHERE customer_id = ?";

        List<MobileSubscription> subscriptions =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    subscriptions.add(
                            mapResultSetToSubscription(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve customer subscriptions.",
                    e
            );
        }

        return subscriptions;
    }

    @Override
    public List<MobileSubscription> findAll() {

        String sql =
                "SELECT * FROM mobile_subscriptions";

        List<MobileSubscription> subscriptions =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                subscriptions.add(
                        mapResultSetToSubscription(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve mobile subscriptions.",
                    e
            );
        }

        return subscriptions;
    }

    @Override
    public void update(MobileSubscription subscription) {

        String sql =
                "UPDATE mobile_subscriptions SET " +
                        "subscription_number = ?, " +
                        "customer_id = ?, " +
                        "plan_id = ?, " +
                        "mobile_number = ?, " +
                        "sim_number = ?, " +
                        "sim_type = ?, " +
                        "activation_date = ?, " +
                        "subscription_type = ?, " +
                        "status = ? " +
                        "WHERE subscription_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    subscription.getSubscriptionNumber()
            );

            statement.setLong(
                    2,
                    subscription.getCustomerId()
            );

            statement.setLong(
                    3,
                    subscription.getPlanId()
            );

            statement.setString(
                    4,
                    subscription.getMobileNumber()
            );

            statement.setString(
                    5,
                    subscription.getSimNumber()
            );

            statement.setString(
                    6,
                    subscription.getSimType().name()
            );

            statement.setDate(
                    7,
                    Date.valueOf(
                            subscription.getActivationDate()
                    )
            );

            statement.setString(
                    8,
                    subscription.getSubscriptionType().name()
            );

            statement.setString(
                    9,
                    subscription.getStatus().name()
            );

            statement.setLong(
                    10,
                    subscription.getSubscriptionId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update mobile subscription.",
                    e
            );
        }
    }

    @Override
    public void delete(long subscriptionId) {

        String sql =
                "DELETE FROM mobile_subscriptions " +
                        "WHERE subscription_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, subscriptionId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete mobile subscription.",
                    e
            );
        }
    }

    private MobileSubscription mapResultSetToSubscription(
            ResultSet resultSet) throws SQLException {

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        return new MobileSubscription(
                resultSet.getLong("subscription_id"),

                resultSet.getString(
                        "subscription_number"
                ),

                resultSet.getLong("customer_id"),

                resultSet.getLong("plan_id"),

                resultSet.getString(
                        "mobile_number"
                ),

                resultSet.getString(
                        "sim_number"
                ),

                SimType.valueOf(
                        resultSet.getString("sim_type")
                ),

                resultSet.getDate(
                        "activation_date"
                ).toLocalDate(),

                SubscriptionType.valueOf(
                        resultSet.getString(
                                "subscription_type"
                        )
                ),

                SubscriptionStatus.valueOf(
                        resultSet.getString("status")
                ),

                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null,

                updatedAt != null
                        ? updatedAt.toLocalDateTime()
                        : null
        );
    }
}