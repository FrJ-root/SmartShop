package org.SmartShop.mapper;

import org.mapstruct.*;
import org.SmartShop.dto.auth.*;
import org.SmartShop.entity.User;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy. IGNORE
)
public interface UserMapper {

    // Entity to DTO mappings
    @Mapping(target = "clientId", source = "client.id")
    UserDto toDto(User user);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "sessionId", ignore = true) // Will be set by service
    @Mapping(target = "message", constant = "Connexion réussie")
    LoginResponse toLoginResponse(User user);
}