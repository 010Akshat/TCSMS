package com.amdocs.telecom.dao;

import com.amdocs.telecom.model.LoginHistory;

import java.util.List;

public interface LoginHistoryDAO {

    void save(LoginHistory loginHistory);

    List<LoginHistory> findByCustomerId(long customerId);
}