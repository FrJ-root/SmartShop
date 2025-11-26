package org.SmartShop.mapper;

import org.mapstruct.*;
import org.SmartShop.dto.loyalty.*;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.enums.CustomerTier;
import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy. IGNORE
)
public interface LoyaltyMapper {

    @Mapping(target = "currentTier", source = "tier")
    @Mapping(target = "nextTier", expression = "java(calculateNextTier(client. getTier()))")
    @Mapping(target = "currentDiscount", expression = "java(calculateCurrentDiscountRate(client.getTier()))")
    @Mapping(target = "minAmountForDiscount", expression = "java(getMinAmountForDiscount(client.getTier()))")
    @Mapping(target = "nextTierRequirement", expression = "java(calculateNextTierRequirement(client))")
    @Mapping(target = "ordersToNextTier", expression = "java(calculateOrdersToNextTier(client))")
    @Mapping(target = "amountToNextTier", expression = "java(calculateAmountToNextTier(client))")
    LoyaltyInfoDto toLoyaltyInfoDto(Client client);

    // Helper methods for loyalty calculations
    default CustomerTier calculateNextTier(CustomerTier currentTier) {
        switch (currentTier) {
            case BASIC: return CustomerTier.SILVER;
            case SILVER: return CustomerTier.GOLD;
            case GOLD: return CustomerTier.PLATINUM;
            case PLATINUM: return CustomerTier. PLATINUM; // Already at max
            default: return CustomerTier.BASIC;
        }
    }

    default BigDecimal calculateCurrentDiscountRate(CustomerTier tier) {
        switch (tier) {
            case SILVER: return new BigDecimal("0.05"); // 5%
            case GOLD: return new BigDecimal("0.10"); // 10%
            case PLATINUM: return new BigDecimal("0.15"); // 15%
            default: return BigDecimal.ZERO;
        }
    }

    default BigDecimal getMinAmountForDiscount(CustomerTier tier) {
        switch (tier) {
            case SILVER: return new BigDecimal("500");
            case GOLD: return new BigDecimal("800");
            case PLATINUM: return new BigDecimal("1200");
            default: return BigDecimal.ZERO;
        }
    }

    default String calculateNextTierRequirement(Client client) {
        CustomerTier currentTier = client.getTier();
        switch (currentTier) {
            case BASIC:
                return "3 commandes OU 1,000 DH dépensés";
            case SILVER:
                return "10 commandes OU 5,000 DH dépensés";
            case GOLD:
                return "20 commandes OU 15,000 DH dépensés";
            case PLATINUM:
                return "Niveau maximum atteint";
            default:
                return "Niveau inconnu";
        }
    }

    default Integer calculateOrdersToNextTier(Client client) {
        switch (client.getTier()) {
            case BASIC: return Math.max(0, 3 - client.getTotalOrders());
            case SILVER: return Math.max(0, 10 - client.getTotalOrders());
            case GOLD: return Math.max(0, 20 - client.getTotalOrders());
            case PLATINUM: return 0;
            default: return 0;
        }
    }

    default BigDecimal calculateAmountToNextTier(Client client) {
        switch (client. getTier()) {
            case BASIC:
                BigDecimal needed = new BigDecimal("1000"). subtract(client.getTotalSpent());
                return needed.compareTo(BigDecimal.ZERO) > 0 ? needed : BigDecimal.ZERO;
            case SILVER:
                needed = new BigDecimal("5000").subtract(client.getTotalSpent());
                return needed.compareTo(BigDecimal.ZERO) > 0 ? needed : BigDecimal.ZERO;
            case GOLD:
                needed = new BigDecimal("15000").subtract(client.getTotalSpent());
                return needed.compareTo(BigDecimal.ZERO) > 0 ? needed : BigDecimal.ZERO;
            case PLATINUM:
                return BigDecimal.ZERO;
            default:
                return BigDecimal.ZERO;
        }
    }
}