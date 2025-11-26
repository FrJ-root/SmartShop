package org.SmartShop.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.auth.AuthResponseDTO;
import org.SmartShop.dto.auth.LoginRequestDTO;
import org.SmartShop.entity.User;
import org.SmartShop.repository.UserRepository;
import org.SmartShop.service.AuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public AuthResponseDTO login(LoginRequestDTO request, HttpSession session) {
        // 1. Find User
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials")); // Will be mapped to 401

        // 2. Check Password (Plain text comparison as per strict 'No Spring Security' constraints)
        // In production, use a standalone hashing library like BCrypt or Argon2
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Store in Session (The core requirement) [cite: 12]
        session.setAttribute("USER_ID", user.getId());
        session.setAttribute("USER_ROLE", user.getRole());

        // 4. Return DTO
        return AuthResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate(); // Destroys the session [cite: 127]
    }
}