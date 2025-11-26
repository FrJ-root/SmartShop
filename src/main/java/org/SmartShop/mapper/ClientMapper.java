package org.SmartShop.mapper;

import org. mapstruct.*;
import org.SmartShop.dto.client.*;
import org.SmartShop.entity.Client;
import org.SmartShop.entity. enums.CustomerTier;
import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClientMapper {

    // Entity to DTO mappings
    ClientDto toDto(Client client);

    @Mapping(target = "clientId", source = "id")
    @Mapping(target = "currentTier", source = "tier")
    @Mapping(target = "nextTierRequirement", expression = "java(calculateNextTierRequirement(client))")
    @Mapping(target = "ordersThisMonth", expression = "java(0)")
    @Mapping(target = "spentThisMonth", expression = "java(java.math.BigDecimal. ZERO)")
    ClientStatisticsDto toStatisticsDto(Client client);

    // Request to Entity mappings - FIXED: Remove user mapping completely
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tier", constant = "BASIC")
    @Mapping(target = "totalOrders", constant = "0")
    @Mapping(target = "totalSpent", expression = "java(java.math. BigDecimal.ZERO)")
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "lastOrderDate", ignore = true)
    @Mapping(target = "commandes", expression = "java(new java.util.ArrayList<>())")
    Client toEntity(CreateClientRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy. IGNORE)
    void updateEntityFromRequest(UpdateClientRequest request, @MappingTarget Client client);

    // Helper method
    default String calculateNextTierRequirement(Client client) {
        CustomerTier currentTier = client.getTier();
        int currentOrders = client.getTotalOrders();
        BigDecimal currentSpent = client.getTotalSpent();

        switch (currentTier) {
            case BASIC:
                int ordersNeeded = Math.max(0, 3 - currentOrders);
                BigDecimal amountNeeded = new BigDecimal("1000").subtract(currentSpent);
                if (amountNeeded.compareTo(BigDecimal.ZERO) < 0) amountNeeded = BigDecimal. ZERO;
                return String.format("SILVER: %d commandes OU %. 2f DH restants", ordersNeeded, amountNeeded);

            case SILVER:
                ordersNeeded = Math.max(0, 10 - currentOrders);
                amountNeeded = new BigDecimal("5000").subtract(currentSpent);
                if (amountNeeded.compareTo(BigDecimal. ZERO) < 0) amountNeeded = BigDecimal. ZERO;
                return String. format("GOLD: %d commandes OU %.2f DH restants", ordersNeeded, amountNeeded);

            case GOLD:
                ordersNeeded = Math.max(0, 20 - currentOrders);
                amountNeeded = new BigDecimal("15000").subtract(currentSpent);
                if (amountNeeded.compareTo(BigDecimal.ZERO) < 0) amountNeeded = BigDecimal.ZERO;
                return String.format("PLATINUM: %d commandes OU %.2f DH restants", ordersNeeded, amountNeeded);

            case PLATINUM:
                return "Niveau maximum atteint";

            default:
                return "Niveau inconnu";
        }
    }
}