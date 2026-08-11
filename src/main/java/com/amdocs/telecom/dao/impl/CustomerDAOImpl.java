package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.CustomerDAO;
import com.amdocs.telecom.model.Customer;
import com.amdocs.telecom.model.AccountStatus;

import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import com.amdocs.telecom.util.DBConnection;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Statement;


public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public void save(Customer customer) {

        String sql = "INSERT INTO customers " +
                "(customer_number, first_name, last_name, date_of_birth, " +
                "email, mobile_number, address, city, country, username, " +
                "password_hash, registration_date, account_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, customer.getCustomerNumber());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setDate(4, Date.valueOf(customer.getDateOfBirth()));
            statement.setString(5, customer.getEmail());
            statement.setString(6, customer.getMobileNumber());
            statement.setString(7, customer.getAddress());
            statement.setString(8, customer.getCity());
            statement.setString(9, customer.getCountry());
            statement.setString(10, customer.getUsername());
            statement.setString(11, customer.getPasswordHash());
            statement.setTimestamp(12, Timestamp.valueOf(customer.getRegistrationDate()));
            statement.setString(13, customer.getAccountStatus().name());

            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    customer.setCustomerId(resultSet.getLong(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer findById(long customerId) {

        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Customer customer = new Customer(
                            resultSet.getLong("customer_id"),
                            resultSet.getString("customer_number"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("email"),
                            resultSet.getString("mobile_number"),
                            resultSet.getString("address"),
                            resultSet.getString("city"),
                            resultSet.getString("country"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getTimestamp("registration_date").toLocalDateTime(),
                            AccountStatus.valueOf(
                                    resultSet.getString("account_status")
                            )
                    );

                    customer.setFailedLoginAttempts(
                            resultSet.getInt("failed_login_attempts")
                    );

                    Timestamp lockedUntil =
                            resultSet.getTimestamp("locked_until");

                    if (lockedUntil != null) {
                        customer.setLockedUntil(
                                lockedUntil.toLocalDateTime()
                        );
                    }

                    Timestamp lastLogin =
                            resultSet.getTimestamp("last_login");

                    if (lastLogin != null) {
                        customer.setLastLogin(
                                lastLogin.toLocalDateTime()
                        );
                    }

                    return customer;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Customer findByUsername(String username) {

        String sql = "SELECT * FROM customers WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Customer customer = new Customer(
                            resultSet.getLong("customer_id"),
                            resultSet.getString("customer_number"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("email"),
                            resultSet.getString("mobile_number"),
                            resultSet.getString("address"),
                            resultSet.getString("city"),
                            resultSet.getString("country"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getTimestamp("registration_date").toLocalDateTime(),
                            AccountStatus.valueOf(
                                    resultSet.getString("account_status")
                            )
                    );

                    customer.setFailedLoginAttempts(
                            resultSet.getInt("failed_login_attempts")
                    );

                    Timestamp lockedUntil =
                            resultSet.getTimestamp("locked_until");

                    if (lockedUntil != null) {
                        customer.setLockedUntil(
                                lockedUntil.toLocalDateTime()
                        );
                    }

                    Timestamp lastLogin =
                            resultSet.getTimestamp("last_login");

                    if (lastLogin != null) {
                        customer.setLastLogin(
                                lastLogin.toLocalDateTime()
                        );
                    }

                    return customer;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Customer findByEmail(String email) {

        String sql = "SELECT * FROM customers WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Customer customer = new Customer(
                            resultSet.getLong("customer_id"),
                            resultSet.getString("customer_number"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("email"),
                            resultSet.getString("mobile_number"),
                            resultSet.getString("address"),
                            resultSet.getString("city"),
                            resultSet.getString("country"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getTimestamp("registration_date").toLocalDateTime(),
                            AccountStatus.valueOf(
                                    resultSet.getString("account_status")
                            )
                    );

                    customer.setFailedLoginAttempts(
                            resultSet.getInt("failed_login_attempts")
                    );

                    Timestamp lockedUntil =
                            resultSet.getTimestamp("locked_until");

                    if (lockedUntil != null) {
                        customer.setLockedUntil(
                                lockedUntil.toLocalDateTime()
                        );
                    }

                    Timestamp lastLogin =
                            resultSet.getTimestamp("last_login");

                    if (lastLogin != null) {
                        customer.setLastLogin(
                                lastLogin.toLocalDateTime()
                        );
                    }

                    return customer;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Customer findByMobileNumber(String mobileNumber) {

        String sql = "SELECT * FROM customers WHERE mobile_number = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, mobileNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Customer customer = new Customer(
                            resultSet.getLong("customer_id"),
                            resultSet.getString("customer_number"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("email"),
                            resultSet.getString("mobile_number"),
                            resultSet.getString("address"),
                            resultSet.getString("city"),
                            resultSet.getString("country"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getTimestamp("registration_date").toLocalDateTime(),
                            AccountStatus.valueOf(
                                    resultSet.getString("account_status")
                            )
                    );

                    customer.setFailedLoginAttempts(
                            resultSet.getInt("failed_login_attempts")
                    );

                    Timestamp lockedUntil =
                            resultSet.getTimestamp("locked_until");

                    if (lockedUntil != null) {
                        customer.setLockedUntil(
                                lockedUntil.toLocalDateTime()
                        );
                    }

                    Timestamp lastLogin =
                            resultSet.getTimestamp("last_login");

                    if (lastLogin != null) {
                        customer.setLastLogin(
                                lastLogin.toLocalDateTime()
                        );
                    }

                    return customer;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Customer> findAll() {

        String sql = "SELECT * FROM customers";

        List<Customer> customers = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Customer customer = new Customer(
                        resultSet.getLong("customer_id"),
                        resultSet.getString("customer_number"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("email"),
                        resultSet.getString("mobile_number"),
                        resultSet.getString("address"),
                        resultSet.getString("city"),
                        resultSet.getString("country"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getTimestamp("registration_date").toLocalDateTime(),
                        AccountStatus.valueOf(
                                resultSet.getString("account_status")
                        )
                );

                customer.setFailedLoginAttempts(
                        resultSet.getInt("failed_login_attempts")
                );

                Timestamp lockedUntil =
                        resultSet.getTimestamp("locked_until");

                if (lockedUntil != null) {
                    customer.setLockedUntil(
                            lockedUntil.toLocalDateTime()
                    );
                }

                Timestamp lastLogin =
                        resultSet.getTimestamp("last_login");

                if (lastLogin != null) {
                    customer.setLastLogin(
                            lastLogin.toLocalDateTime()
                    );
                }

                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public void update(Customer customer) {

        String sql = "UPDATE customers SET " +
                "customer_number = ?, " +
                "first_name = ?, " +
                "last_name = ?, " +
                "date_of_birth = ?, " +
                "email = ?, " +
                "mobile_number = ?, " +
                "address = ?, " +
                "city = ?, " +
                "country = ?, " +
                "username = ?, " +
                "password_hash = ?, " +
                "registration_date = ?, " +
                "account_status = ? " +
                "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getCustomerNumber());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setDate(4, Date.valueOf(customer.getDateOfBirth()));
            statement.setString(5, customer.getEmail());
            statement.setString(6, customer.getMobileNumber());
            statement.setString(7, customer.getAddress());
            statement.setString(8, customer.getCity());
            statement.setString(9, customer.getCountry());
            statement.setString(10, customer.getUsername());
            statement.setString(11, customer.getPasswordHash());
            statement.setTimestamp(12, Timestamp.valueOf(customer.getRegistrationDate()));
            statement.setString(13, customer.getAccountStatus().name());
            statement.setLong(14, customer.getCustomerId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(long customerId) {

        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFailedLoginAttempts(long customerId, int failedAttempts) {

        String sql = "UPDATE customers " +
                "SET failed_login_attempts = ? " +
                "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, failedAttempts);
            statement.setLong(2, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLockStatus(long customerId, LocalDateTime lockedUntil) {

        String sql = "UPDATE customers " +
                "SET locked_until = ? " +
                "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (lockedUntil != null) {
                statement.setTimestamp(
                        1,
                        Timestamp.valueOf(lockedUntil)
                );
            } else {
                statement.setNull(
                        1,
                        java.sql.Types.TIMESTAMP
                );
            }

            statement.setLong(2, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLastLogin(long customerId, LocalDateTime lastLogin) {

        String sql = "UPDATE customers " +
                "SET last_login = ? " +
                "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (lastLogin != null) {
                statement.setTimestamp(
                        1,
                        Timestamp.valueOf(lastLogin)
                );
            } else {
                statement.setNull(
                        1,
                        java.sql.Types.TIMESTAMP
                );
            }

            statement.setLong(2, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePassword(long customerId, String passwordHash) {

        String sql = "UPDATE customers " +
                "SET password_hash = ? " +
                "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, passwordHash);
            statement.setLong(2, customerId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}