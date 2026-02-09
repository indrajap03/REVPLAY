package com.revplay.dao.impl;

import com.revplay.dao.UserDao;
import com.revplay.model.User;
import com.revplay.util.DBUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDaoImpl implements UserDao {

    private static final Logger logger =
            LogManager.getLogger(UserDaoImpl.class);

    @Override
    public void saveUser(User user) throws Exception {

        logger.info("Saving new user with email={}", user.getEmail());

        String sql = "INSERT INTO users (user_id, email, password, role, password_hint) " +
                "VALUES (user_seq.NEXTVAL, ?, ?, ?, ?)";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getRole());
        ps.setString(4, user.getPasswordHint());

        ps.executeUpdate();
        con.close();

        logger.info("User saved successfully with email={}", user.getEmail());
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws Exception {

        logger.info("Finding user by email and password for email={}", email);

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        User user = null;

        if (rs.next()) {
            user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            user.setPasswordHint(rs.getString("password_hint"));
        }

        con.close();

        logger.info("User found={} for email={}", user != null, email);
        return user;
    }

    // ✅ CHANGE PASSWORD
    @Override
    public void updatePassword(int userId, String newPassword) throws Exception {

        logger.info("Updating password for userId={}", userId);

        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, newPassword);
        ps.setInt(2, userId);

        ps.executeUpdate();
        con.close();

        logger.info("Password updated successfully for userId={}", userId);
    }

    // ✅ FORGOT PASSWORD
    @Override
    public User findByEmail(String email) throws Exception {

        logger.info("Finding user by email={}", email);

        String sql = "SELECT * FROM users WHERE email = ?";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, email);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()) {
            user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPasswordHint(rs.getString("password_hint"));
        }

        con.close();

        logger.info("User found={} for email={}", user != null, email);
        return user;
    }
}