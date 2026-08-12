package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.PaymentDAO;
import com.amdocs.telecom.model.Payment;
import com.amdocs.telecom.model.PaymentMode;
import com.amdocs.telecom.model.PaymentStatus;
import com.amdocs.telecom.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public void save(Payment payment) {

        String sql =
                "INSERT INTO payments " +
                        "(transaction_reference, bill_id, customer_id, " +
                        "amount, payment_mode, payment_date, payment_status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(
                    1,
                    payment.getTransactionReference()
            );

            statement.setLong(
                    2,
                    payment.getBillId()
            );

            statement.setLong(
                    3,
                    payment.getCustomerId()
            );

            statement.setBigDecimal(
                    4,
                    payment.getAmount()
            );

            statement.setString(
                    5,
                    payment.getPaymentMode().name()
            );

            if (payment.getPaymentDate() != null) {

                statement.setTimestamp(
                        6,
                        Timestamp.valueOf(
                                payment.getPaymentDate()
                        )
                );

            } else {

                statement.setTimestamp(
                        6,
                        null
                );
            }

            statement.setString(
                    7,
                    payment.getPaymentStatus().name()
            );

            statement.executeUpdate();

            try (ResultSet resultSet =
                         statement.getGeneratedKeys()) {

                if (resultSet.next()) {

                    payment.setPaymentId(
                            resultSet.getLong(1)
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save payment.",
                    e
            );
        }
    }

    @Override
    public Payment findById(long paymentId) {

        String sql =
                "SELECT * FROM payments " +
                        "WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    paymentId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToPayment(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find payment.",
                    e
            );
        }

        return null;
    }

    @Override
    public Payment findByTransactionReference(
            String transactionReference) {

        String sql =
                "SELECT * FROM payments " +
                        "WHERE transaction_reference = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    transactionReference
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return mapResultSetToPayment(
                            resultSet
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find payment by transaction reference.",
                    e
            );
        }

        return null;
    }

    @Override
    public List<Payment> findByBillId(
            long billId) {

        String sql =
                "SELECT * FROM payments " +
                        "WHERE bill_id = ? " +
                        "ORDER BY payment_date DESC";

        List<Payment> payments =
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

                    payments.add(
                            mapResultSetToPayment(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve bill payments.",
                    e
            );
        }

        return payments;
    }

    @Override
    public List<Payment> findByCustomerId(
            long customerId) {

        String sql =
                "SELECT * FROM payments " +
                        "WHERE customer_id = ? " +
                        "ORDER BY payment_date DESC";

        List<Payment> payments =
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

                    payments.add(
                            mapResultSetToPayment(
                                    resultSet
                            )
                    );
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve customer payments.",
                    e
            );
        }

        return payments;
    }

    @Override
    public List<Payment> findAll() {

        String sql =
                "SELECT * FROM payments " +
                        "ORDER BY payment_date DESC";

        List<Payment> payments =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                payments.add(
                        mapResultSetToPayment(
                                resultSet
                        )
                );
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve payments.",
                    e
            );
        }

        return payments;
    }

    @Override
    public void update(Payment payment) {

        String sql =
                "UPDATE payments SET " +
                        "transaction_reference = ?, " +
                        "bill_id = ?, " +
                        "customer_id = ?, " +
                        "amount = ?, " +
                        "payment_mode = ?, " +
                        "payment_date = ?, " +
                        "payment_status = ? " +
                        "WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    payment.getTransactionReference()
            );

            statement.setLong(
                    2,
                    payment.getBillId()
            );

            statement.setLong(
                    3,
                    payment.getCustomerId()
            );

            statement.setBigDecimal(
                    4,
                    payment.getAmount()
            );

            statement.setString(
                    5,
                    payment.getPaymentMode().name()
            );

            if (payment.getPaymentDate() != null) {

                statement.setTimestamp(
                        6,
                        Timestamp.valueOf(
                                payment.getPaymentDate()
                        )
                );

            } else {

                statement.setTimestamp(
                        6,
                        null
                );
            }

            statement.setString(
                    7,
                    payment.getPaymentStatus().name()
            );

            statement.setLong(
                    8,
                    payment.getPaymentId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update payment.",
                    e
            );
        }
    }

    @Override
    public void delete(long paymentId) {

        String sql =
                "DELETE FROM payments " +
                        "WHERE payment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    paymentId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete payment.",
                    e
            );
        }
    }

    private Payment mapResultSetToPayment(
            ResultSet resultSet) throws SQLException {

        Timestamp paymentDate =
                resultSet.getTimestamp(
                        "payment_date"
                );

        return new Payment(
                resultSet.getLong(
                        "payment_id"
                ),

                resultSet.getString(
                        "transaction_reference"
                ),

                resultSet.getLong(
                        "bill_id"
                ),

                resultSet.getLong(
                        "customer_id"
                ),

                resultSet.getBigDecimal(
                        "amount"
                ),

                PaymentMode.valueOf(
                        resultSet.getString(
                                "payment_mode"
                        )
                ),

                paymentDate != null
                        ? paymentDate.toLocalDateTime()
                        : null,

                PaymentStatus.valueOf(
                        resultSet.getString(
                                "payment_status"
                        )
                )
        );
    }
        @Override
        public void save(
                Payment payment,
                Connection connection) {

            String sql =
                    "INSERT INTO payments " +
                            "(transaction_reference, bill_id, customer_id, " +
                            "amount, payment_mode, payment_date, payment_status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(
                        1,
                        payment.getTransactionReference()
                );

                statement.setLong(
                        2,
                        payment.getBillId()
                );

                statement.setLong(
                        3,
                        payment.getCustomerId()
                );

                statement.setBigDecimal(
                        4,
                        payment.getAmount()
                );

                statement.setString(
                        5,
                        payment.getPaymentMode().name()
                );

                if (payment.getPaymentDate() != null) {
                    statement.setTimestamp(
                            6,
                            Timestamp.valueOf(
                                    payment.getPaymentDate()
                            )
                    );
                } else {
                    statement.setTimestamp(
                            6,
                            null
                    );
                }

                statement.setString(
                        7,
                        payment.getPaymentStatus().name()
                );

                statement.executeUpdate();

                try (ResultSet resultSet =
                             statement.getGeneratedKeys()) {

                    if (resultSet.next()) {
                        payment.setPaymentId(
                                resultSet.getLong(1)
                        );
                    }
                }

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Failed to save payment in transaction.",
                        e
                );
            }
        }

        @Override
        public Payment findById(
        long paymentId,
        Connection connection) {

            String sql =
                    "SELECT * FROM payments " +
                            "WHERE payment_id = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setLong(
                        1,
                        paymentId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {
                        return mapResultSetToPayment(
                                resultSet
                        );
                    }
                }

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Failed to find payment in transaction.",
                        e
                );
            }

            return null;
        }

        @Override
        public List<Payment> findByBillId(
        long billId,
        Connection connection) {

            String sql =
                    "SELECT * FROM payments " +
                            "WHERE bill_id = ? " +
                            "ORDER BY payment_date DESC";

            List<Payment> payments =
                    new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setLong(
                        1,
                        billId
                );

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    while (resultSet.next()) {

                        payments.add(
                                mapResultSetToPayment(
                                        resultSet
                                )
                        );
                    }
                }

            } catch (SQLException e) {

                throw new RuntimeException(
                        "Failed to retrieve bill payments in transaction.",
                        e
                );
            }

            return payments;
    }
}