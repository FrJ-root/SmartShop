package org.SmartShop.dto.auth;

import lombok.*;
import org. SmartShop.entity.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private UserRole role;
    private Long clientId;
}