package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.Admin;

import java.util.List;

public interface AdminDAO {

    void save(Admin admin);

    Admin findById(long adminId);

    Admin findByUsername(String username);

    List<Admin> findAll();

    void update(Admin admin);

    void delete(long adminId);
}