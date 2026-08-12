package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.BillingDAO;
import com.amdocs.telecom.model.Bill;
import com.amdocs.telecom.model.BillStatus;
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

public class BillingDAOImpl implements BillingDAO {

    @Override
    public void save(Bill bill) {

        String sql =
                "INSERT INTO bills " +
                        "(bill_number, subscription_id, billing_month, " +
                        "plan_rental, usage_charges, tax_amount, discount, " +
                        "total_amount, due_date, bill_status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    bill.getBillNumber()
            );

            statement.setLong(
                    2,
                    bill.getSubscriptionId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            bill.getBillingMonth()
                    )
            );

            statement.setBigDecimal(
                    4,
                    bill.getPlanRental()
            );

            statement.setBigDecimal(
                    5,
                    bill.getUsageCharges()
            );

            statement.setBigDecimal(
                    6,
                    bill.getTaxAmount()
            );

            statement.setBigDecimal(
                    7,
                    bill.getDiscount()
            );

            statement.setBigDecimal(
                    8,
                    bill.getTotalAmount()
            );

            statement.setDate(
                    9,
                    Date.valueOf(
                            bill.getDueDate()
                    )
            );

            statement.setString(
                    10,
                    bill.getBillStatus().name()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {
                    bill.setBillId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save bill.",
                    e
            );
        }
    }

    @Override
    public Bill findById(long billId) {

        String sql =
                "SELECT * FROM bills " +
                        "WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    billId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToBill(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bill.",
                    e
            );
        }

        return null;
    }

    @Override
    public Bill findById(
            long billId,
            Connection connection) {

        String sql =
                "SELECT * FROM bills " +
                        "WHERE bill_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    billId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToBill(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bill in transaction.",
                    e
            );
        }

        return null;
    }
    @Override
    public Bill findByBillNumber(
            String billNumber) {

        String sql =
                "SELECT * FROM bills " +
                        "WHERE bill_number = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    billNumber
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToBill(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bill by number.",
                    e
            );
        }

        return null;
    }

    @Override
    public Bill findBySubscriptionAndMonth(
            long subscriptionId,
            java.time.LocalDate billingMonth) {

        String sql =
                "SELECT * FROM bills " +
                        "WHERE subscription_id = ? " +
                        "AND billing_month = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    subscriptionId
            );

            statement.setDate(
                    2,
                    Date.valueOf(
                            billingMonth
                    )
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapResultSetToBill(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bill for subscription and month.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<Bill> findBySubscriptionId(
            long subscriptionId) {

        String sql =
                "SELECT * FROM bills " +
                        "WHERE subscription_id = ? " +
                        "ORDER BY billing_month DESC";

        List<Bill> bills =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    subscriptionId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    bills.add(
                            mapResultSetToBill(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve subscription bills.",
                    e
            );
        }

        return bills;
    }

    @Override
    public List<Bill> findAll() {

        String sql =
                "SELECT * FROM bills " +
                        "ORDER BY billing_month DESC";

        List<Bill> bills =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                bills.add(
                        mapResultSetToBill(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve bills.",
                    e
            );
        }

        return bills;
    }

    @Override
    public void update(Bill bill) {

        String sql =
                "UPDATE bills SET " +
                        "bill_number = ?, " +
                        "subscription_id = ?, " +
                        "billing_month = ?, " +
                        "plan_rental = ?, " +
                        "usage_charges = ?, " +
                        "tax_amount = ?, " +
                        "discount = ?, " +
                        "total_amount = ?, " +
                        "due_date = ?, " +
                        "bill_status = ? " +
                        "WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    bill.getBillNumber()
            );

            statement.setLong(
                    2,
                    bill.getSubscriptionId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            bill.getBillingMonth()
                    )
            );

            statement.setBigDecimal(
                    4,
                    bill.getPlanRental()
            );

            statement.setBigDecimal(
                    5,
                    bill.getUsageCharges()
            );

            statement.setBigDecimal(
                    6,
                    bill.getTaxAmount()
            );

            statement.setBigDecimal(
                    7,
                    bill.getDiscount()
            );

            statement.setBigDecimal(
                    8,
                    bill.getTotalAmount()
            );

            statement.setDate(
                    9,
                    Date.valueOf(
                            bill.getDueDate()
                    )
            );

            statement.setString(
                    10,
                    bill.getBillStatus().name()
            );

            statement.setLong(
                    11,
                    bill.getBillId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update bill.",
                    e
            );
        }
    }

    @Override
    public void update(
            Bill bill,
            Connection connection) {

        String sql =
                "UPDATE bills SET " +
                        "bill_number = ?, " +
                        "subscription_id = ?, " +
                        "billing_month = ?, " +
                        "plan_rental = ?, " +
                        "usage_charges = ?, " +
                        "tax_amount = ?, " +
                        "discount = ?, " +
                        "total_amount = ?, " +
                        "due_date = ?, " +
                        "bill_status = ? " +
                        "WHERE bill_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    bill.getBillNumber()
            );

            statement.setLong(
                    2,
                    bill.getSubscriptionId()
            );

            statement.setDate(
                    3,
                    Date.valueOf(
                            bill.getBillingMonth()
                    )
            );

            statement.setBigDecimal(
                    4,
                    bill.getPlanRental()
            );

            statement.setBigDecimal(
                    5,
                    bill.getUsageCharges()
            );

            statement.setBigDecimal(
                    6,
                    bill.getTaxAmount()
            );

            statement.setBigDecimal(
                    7,
                    bill.getDiscount()
            );

            statement.setBigDecimal(
                    8,
                    bill.getTotalAmount()
            );

            statement.setDate(
                    9,
                    Date.valueOf(
                            bill.getDueDate()
                    )
            );

            statement.setString(
                    10,
                    bill.getBillStatus().name()
            );

            statement.setLong(
                    11,
                    bill.getBillId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update bill in transaction.",
                    e
            );
        }
    }

    @Override
    public void delete(long billId) {

        String sql =
                "DELETE FROM bills " +
                        "WHERE bill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    billId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete bill.",
                    e
            );
        }
    }

    private Bill mapResultSetToBill(
            ResultSet resultSet) throws SQLException {

        Timestamp createdAt =
                resultSet.getTimestamp(
                        "created_at"
                );

        Timestamp updatedAt =
                resultSet.getTimestamp(
                        "updated_at"
                );

        return new Bill(
                resultSet.getLong(
                        "bill_id"
                ),

                resultSet.getString(
                        "bill_number"
                ),

                resultSet.getLong(
                        "subscription_id"
                ),

                resultSet.getDate(
                        "billing_month"
                ).toLocalDate(),

                resultSet.getBigDecimal(
                        "plan_rental"
                ),

                resultSet.getBigDecimal(
                        "usage_charges"
                ),

                resultSet.getBigDecimal(
                        "tax_amount"
                ),

                resultSet.getBigDecimal(
                        "discount"
                ),

                resultSet.getBigDecimal(
                        "total_amount"
                ),

                resultSet.getDate(
                        "due_date"
                ).toLocalDate(),

                BillStatus.valueOf(
                        resultSet.getString(
                                "bill_status"
                        )
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