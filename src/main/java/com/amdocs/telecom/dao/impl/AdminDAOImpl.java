package com.amdocs.telecom.dao.impl;

import com.amdocs.telecom.dao.AdminDAO;
import com.amdocs.telecom.model.Admin;
import com.amdocs.telecom.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {


    @Override
    public void save(Admin admin) {

        String sql =
                "INSERT INTO admins " +
                        "(admin_username, password_hash, first_name, " +
                        "last_name, email, admin_status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {


            statement.setString(1,
                    admin.getAdminUsername());

            statement.setString(2,
                    admin.getPasswordHash());

            statement.setString(3,
                    admin.getFirstName());

            statement.setString(4,
                    admin.getLastName());

            statement.setString(5,
                    admin.getEmail());

            statement.setString(6,
                    admin.getAdminStatus());


            statement.executeUpdate();


            try(ResultSet resultSet =
                        statement.getGeneratedKeys()) {

                if(resultSet.next()) {

                    admin.setAdminId(
                            resultSet.getLong(1)
                    );
                }
            }


        } catch(SQLException e) {

            throw new RuntimeException(
                    "Failed to save admin.",
                    e
            );
        }
    }



    @Override
    public Admin findById(long adminId) {

        String sql =
                "SELECT * FROM admins " +
                        "WHERE admin_id = ?";


        try(Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)) {


            statement.setLong(1, adminId);


            try(ResultSet resultSet =
                        statement.executeQuery()) {


                if(resultSet.next()) {

                    return mapResultSet(resultSet);
                }
            }


        } catch(SQLException e) {

            throw new RuntimeException(
                    "Failed to find admin.",
                    e
            );
        }


        return null;
    }



    @Override
    public Admin findByUsername(String username) {

        String sql =
                "SELECT * FROM admins " +
                        "WHERE admin_username = ?";


        try(Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)) {


            statement.setString(1, username);


            try(ResultSet resultSet =
                        statement.executeQuery()) {


                if(resultSet.next()) {

                    return mapResultSet(resultSet);
                }
            }


        } catch(SQLException e) {

            throw new RuntimeException(
                    "Failed to find admin by username.",
                    e
            );
        }


        return null;
    }



    @Override
    public List<Admin> findAll() {


        String sql =
                "SELECT * FROM admins";


        List<Admin> admins =
                new ArrayList<>();


        try(Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery()) {


            while(resultSet.next()) {

                admins.add(
                        mapResultSet(resultSet)
                );
            }


        } catch(SQLException e) {

            throw new RuntimeException(
                    "Failed to retrieve admins.",
                    e
            );
        }


        return admins;
    }



    @Override
    public void update(Admin admin) {


        String sql =
                "UPDATE admins SET " +
                        "admin_username=?, " +
                        "password_hash=?, " +
                        "first_name=?, " +
                        "last_name=?, " +
                        "email=?, " +
                        "admin_status=? " +
                        "WHERE admin_id=?";


        try(Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)) {


            statement.setString(1,
                    admin.getAdminUsername());

            statement.setString(2,
                    admin.getPasswordHash());

            statement.setString(3,
                    admin.getFirstName());

            statement.setString(4,
                    admin.getLastName());

            statement.setString(5,
                    admin.getEmail());

            statement.setString(6,
                    admin.getAdminStatus());

            statement.setLong(7,
                    admin.getAdminId());


            statement.executeUpdate();


        } catch(SQLException e) {

            throw new RuntimeException(
                    "Failed to update admin.",
                    e
            );
        }
    }



    @Override
    public void delete(long adminId) {


        String sql =
                "DELETE FROM admins " +
                        "WHERE admin_id=?";


        try(Connection connection =
                    DBConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql)) {


            statement.setLong(1, adminId);

            statement.executeUpdate();


        } catch(SQLException e) {

            throw new RuntimeException(
                    "Failed to delete admin.",
                    e
            );
        }
    }



    private Admin mapResultSet(
            ResultSet resultSet)
            throws SQLException {


        Timestamp createdAt =
                resultSet.getTimestamp(
                        "created_at"
                );


        return new Admin(

                resultSet.getLong(
                        "admin_id"
                ),

                resultSet.getString(
                        "admin_username"
                ),

                resultSet.getString(
                        "password_hash"
                ),

                resultSet.getString(
                        "first_name"
                ),

                resultSet.getString(
                        "last_name"
                ),

                resultSet.getString(
                        "email"
                ),

                resultSet.getString(
                        "admin_status"
                ),

                createdAt != null
                        ? createdAt.toLocalDateTime()
                        : null
        );
    }
}