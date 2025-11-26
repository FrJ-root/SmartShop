package org.SmartShop.dto.client;

import lombok.Data;
import org.SmartShop.entity.enums.CustomerTier;
import java.math.BigDecimal;

@Data
public class ClientResponseDTO {
    private Long id;
    private String name;
    private String email;
    private CustomerTier tier;
    private int totalOrders;
    private BigDecimal totalSpent;
}