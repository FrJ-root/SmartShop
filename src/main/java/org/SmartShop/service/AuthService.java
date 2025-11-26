package org.SmartShop.service;

import org.SmartShop.dto.auth.LoginRequest;
import org.SmartShop.dto.auth.LoginResponse;
import org.SmartShop.entity.User;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest, HttpSession session);
    void logout(HttpSession session);
    User getCurrentUser(HttpSession session);
    boolean isAuthenticated(HttpSession session);
}
