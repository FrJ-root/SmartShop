package org.SmartShop.dto.client;

import org.SmartShop.entity.enums.CustomerTier;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String email;
    private int totalOrders;
    private CustomerTier tier;
    private BigDecimal totalSpent;
}