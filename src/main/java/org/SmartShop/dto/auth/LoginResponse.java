package org.SmartShop.dto.auth;

import lombok.*;
import org.SmartShop. entity.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private UserRole role;
    private String username;
    private Long clientId;
    private String sessionId;
}