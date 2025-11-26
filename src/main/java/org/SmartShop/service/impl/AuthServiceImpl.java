package org.SmartShop.service.impl;

import org.SmartShop.dto.auth.LoginRequest;
import org.SmartShop.dto.auth.LoginResponse;
import org.SmartShop.entity.User;
import org.SmartShop.repository.UserRepository;
import org.SmartShop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    private static final String USER_SESSION_KEY = "authenticated_user";

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpSession session) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return LoginResponse.builder()
                    .message("Nom d'utilisateur ou mot de passe incorrect")
                    .build();
        }

        // Store user in session
        session.setAttribute(USER_SESSION_KEY, user);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        return LoginResponse.builder()
                .message("Connexion réussie")
                .role(user.getRole())
                .username(user.getUsername())
                .clientId(user.getClient() != null ? user.getClient().getId() : null)
                .sessionId(session.getId())
                .build();
    }

    @Override
    public void logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
        session.invalidate();
    }

    @Override
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute(USER_SESSION_KEY);
    }

    @Override
    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(USER_SESSION_KEY) != null;
    }
}
