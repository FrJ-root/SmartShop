package org.SmartShop.dto.loyalty;

import lombok.*;
import org. SmartShop.entity.enums.CustomerTier;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyInfoDto {
    private CustomerTier currentTier;
    private CustomerTier nextTier;
    private BigDecimal currentDiscount;
    private BigDecimal minAmountForDiscount;
    private String nextTierRequirement;
    private Integer ordersToNextTier;
    private BigDecimal amountToNextTier;
}