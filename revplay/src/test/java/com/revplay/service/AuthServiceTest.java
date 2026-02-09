package com.revplay.service;

import com.revplay.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private final AuthService authService = new AuthService();

    @Test
    void register_shouldFail_whenEmailIsNull() {
        User user = new User();
        user.setPassword("pass123");
        user.setRole("USER");

        Exception ex = assertThrows(Exception.class,
                () -> authService.register(user));

        assertEquals("Email cannot be empty", ex.getMessage());
    }

    @Test
    void register_shouldFail_whenPasswordIsEmpty() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("");
        user.setRole("USER");

        Exception ex = assertThrows(Exception.class,
                () -> authService.register(user));

        assertEquals("Password cannot be empty", ex.getMessage());
    }

    @Test
    void register_shouldFail_whenRoleIsInvalid() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("pass123");
        user.setRole("ADMIN");

        Exception ex = assertThrows(Exception.class,
                () -> authService.register(user));

        assertEquals("Role must be USER or ARTIST", ex.getMessage());
    }
}