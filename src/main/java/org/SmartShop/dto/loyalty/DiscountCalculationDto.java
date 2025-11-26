package org.SmartShop.dto.loyalty;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCalculationDto {
    private BigDecimal subTotal;
    private BigDecimal loyaltyDiscount;
    private BigDecimal promoDiscount;
    private BigDecimal totalDiscount;
    private BigDecimal finalAmount;
    private String discountReason;
}