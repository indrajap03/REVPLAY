package com.revplay.service;

import com.revplay.dao.UserDao;
import com.revplay.dao.impl.UserDaoImpl;
import com.revplay.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthService {

    private static final Logger logger =
            LogManager.getLogger(AuthService.class);

    private final UserDao userDao = new UserDaoImpl();

    // REGISTER
    public void register(User user) throws Exception {

        logger.info("Register attempt for email={}", user.getEmail());

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            logger.error("Registration failed: Email is empty");
            throw new Exception("Email cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.error("Registration failed: Password is empty");
            throw new Exception("Password cannot be empty");
        }

        if (!"USER".equalsIgnoreCase(user.getRole()) &&
                !"ARTIST".equalsIgnoreCase(user.getRole())) {
            logger.error("Registration failed: Invalid role={}", user.getRole());
            throw new Exception("Role must be USER or ARTIST");
        }

        userDao.saveUser(user);
        logger.info("Registration successful for email={}", user.getEmail());
    }

    // LOGIN
    public User login(String email, String password) throws Exception {

        logger.info("Login attempt for email={}", email);

        User user = userDao.findByEmailAndPassword(email, password);

        logger.info("Login success={} for email={}", user != null, email);
        return user;
    }

    // ✅ CHANGE PASSWORD
    public void changePassword(int userId, String newPassword) throws Exception {

        logger.info("Change password requested for userId={}", userId);

        userDao.updatePassword(userId, newPassword);

        logger.info("Password changed successfully for userId={}", userId);
    }

    // ✅ FORGOT PASSWORD
    public User getUserByEmail(String email) throws Exception {

        logger.info("Fetching user by email={}", email);

        User user = userDao.findByEmail(email);

        logger.info("User found={} for email={}", user != null, email);
        return user;
    }
}