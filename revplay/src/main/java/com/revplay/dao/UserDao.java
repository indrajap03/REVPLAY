package com.revplay.dao;

import com.revplay.model.User;

public interface UserDao {

    void saveUser(User user) throws Exception;

    User findByEmailAndPassword(String email, String password) throws Exception;

    // NEW
    void updatePassword(int userId, String newPassword) throws Exception;

    User findByEmail(String email) throws Exception;
}