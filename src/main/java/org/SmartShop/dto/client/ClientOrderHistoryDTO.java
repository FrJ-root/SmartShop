package org.SmartShop.dto.client;

import lombok.Data;
import org.SmartShop.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClientOrderHistoryDTO {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal totalTTC;
    private OrderStatus status;
}