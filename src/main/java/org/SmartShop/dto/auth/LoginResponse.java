package org.SmartShop.dto.auth;

import org.SmartShop.entity.enums.UserRole;

public class LoginResponse {
    private String message;
    private UserRole role;
    private String username;
    private Long clientId;
    private String sessionId;

    public LoginResponse() {}

    public LoginResponse(String message, UserRole role, String username, Long clientId, String sessionId) {
        this.message = message;
        this.role = role;
        this.username = username;
        this.clientId = clientId;
        this.sessionId = sessionId;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public static class LoginResponseBuilder {
        private String message;
        private UserRole role;
        private String username;
        private Long clientId;
        private String sessionId;

        public LoginResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public LoginResponseBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public LoginResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public LoginResponseBuilder clientId(Long clientId) {
            this.clientId = clientId;
            return this;
        }

        public LoginResponseBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(message, role, username, clientId, sessionId);
        }
    }
}