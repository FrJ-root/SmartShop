package org.SmartShop.dto.auth;

import lombok.Builder;
import lombok.Data;
import org.SmartShop.entity.enums.UserRole;

@Data
@Builder
public class AuthResponseDTO {
    private Long id;
    private String username;
    private UserRole role;
}