package org.SmartShop.controller;

import org.SmartShop.dto.auth.LoginRequest;
import org.SmartShop.dto.auth.LoginResponse;
import org.SmartShop.dto.response.ApiResponse;
import org.SmartShop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, 
                                             HttpSession session) {
        LoginResponse response = authService.login(loginRequest, session);
        
        if (response.getSessionId() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Déconnexion réussie")
                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getCurrentUser(HttpSession session) {
        if (!authService.isAuthenticated(session)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Non authentifié")
                            .build());
        }

        var user = authService.getCurrentUser(session);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Utilisateur connecté")
                .data(new Object() {
                    public String getUsername() { return user.getUsername(); }
                    public String getRole() { return user.getRole().toString(); }
                    public Long getClientId() { 
                        return user.getClient() != null ? user.getClient().getId() : null; 
                    }
                })
                .build());
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkAuth(HttpSession session) {
        boolean isAuth = authService.isAuthenticated(session);
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .success(true)
                .message(isAuth ? "Authentifié" : "Non authentifié")
                .data(isAuth)
                .build());
    }
}
