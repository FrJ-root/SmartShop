package org.SmartShop.dto. client;

import lombok.*;
import org.SmartShop.entity. enums.CustomerTier;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientStatisticsDto {
    private Long clientId;
    private String nom;
    private CustomerTier currentTier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
    private Integer ordersThisMonth;
    private BigDecimal spentThisMonth;
    private String nextTierRequirement;
}