package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.PlanDAO;
import com.amdocs.telecom.model.enums.AccountStatus;
import com.amdocs.telecom.model.enums.PlanType;
import com.amdocs.telecom.model.TelecomPlan;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PlanDAOImpl implements PlanDAO {

    @Override
    public void save(TelecomPlan plan) {

        String sql = "INSERT INTO telecom_plans " +
                "(plan_code, plan_name, plan_type, monthly_rental, " +
                "data_allowance_gb, voice_minutes, sms_allowance, " +
                "validity_days, international_roaming, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, plan.getPlanCode());
            statement.setString(2, plan.getPlanName());
            statement.setString(3, plan.getPlanType().name());
            statement.setBigDecimal(4, plan.getMonthlyRental());
            statement.setBigDecimal(5, plan.getDataAllowanceGB());
            statement.setInt(6, plan.getVoiceMinutes());
            statement.setInt(7, plan.getSmsAllowance());
            statement.setInt(8, plan.getValidityDays());
            statement.setBoolean(9, plan.isInternationalRoaming());
            statement.setString(10, plan.getStatus().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to save telecom plan.",
                    e
            );
        }
    }

    @Override
    public TelecomPlan findById(long planId) {

        String sql =
                "SELECT * FROM telecom_plans " +
                        "WHERE plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, planId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToPlan(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find telecom plan.",
                    e
            );
        }

        return null;
    }

    @Override
    public TelecomPlan findByCode(String planCode) {

        String sql =
                "SELECT * FROM telecom_plans " +
                        "WHERE plan_code = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, planCode);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToPlan(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to find telecom plan.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<TelecomPlan> findAll() {

        String sql =
                "SELECT * FROM telecom_plans";

        List<TelecomPlan> plans = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                plans.add(
                        mapResultSetToPlan(resultSet)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve telecom plans.",
                    e
            );
        }

        return plans;
    }

    @Override
    public List<TelecomPlan> findActivePlans() {

        String sql =
                "SELECT * FROM telecom_plans " +
                        "WHERE status = ?";

        List<TelecomPlan> plans = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    AccountStatus.ACTIVE.name()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    plans.add(
                            mapResultSetToPlan(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve active telecom plans.",
                    e
            );
        }

        return plans;
    }

    @Override
    public void update(TelecomPlan plan) {

        String sql =
                "UPDATE telecom_plans SET " +
                        "plan_code = ?, " +
                        "plan_name = ?, " +
                        "plan_type = ?, " +
                        "monthly_rental = ?, " +
                        "data_allowance_gb = ?, " +
                        "voice_minutes = ?, " +
                        "sms_allowance = ?, " +
                        "validity_days = ?, " +
                        "international_roaming = ?, " +
                        "status = ? " +
                        "WHERE plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, plan.getPlanCode());
            statement.setString(2, plan.getPlanName());
            statement.setString(3, plan.getPlanType().name());
            statement.setBigDecimal(4, plan.getMonthlyRental());
            statement.setBigDecimal(5, plan.getDataAllowanceGB());
            statement.setInt(6, plan.getVoiceMinutes());
            statement.setInt(7, plan.getSmsAllowance());
            statement.setInt(8, plan.getValidityDays());
            statement.setBoolean(
                    9,
                    plan.isInternationalRoaming()
            );
            statement.setString(
                    10,
                    plan.getStatus().name()
            );
            statement.setLong(
                    11,
                    plan.getPlanId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update telecom plan.",
                    e
            );
        }
    }

    @Override
    public void delete(long planId) {

        String sql =
                "DELETE FROM telecom_plans " +
                        "WHERE plan_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, planId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete telecom plan.",
                    e
            );
        }
    }

    private TelecomPlan mapResultSetToPlan(
            ResultSet resultSet) throws SQLException {

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        return new TelecomPlan(
                resultSet.getLong("plan_id"),
                resultSet.getString("plan_code"),
                resultSet.getString("plan_name"),
                PlanType.valueOf(
                        resultSet.getString("plan_type")
                ),
                resultSet.getBigDecimal("monthly_rental"),
                resultSet.getBigDecimal("data_allowance_gb"),
                resultSet.getInt("voice_minutes"),
                resultSet.getInt("sms_allowance"),
                resultSet.getInt("validity_days"),
                resultSet.getBoolean("international_roaming"),
                AccountStatus.valueOf(
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