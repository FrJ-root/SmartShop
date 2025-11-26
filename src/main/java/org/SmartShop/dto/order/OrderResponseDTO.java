package org.SmartShop.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private String status;
    private BigDecimal subTotalHT;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalTTC;
    private BigDecimal amountRemaining;
    private List<OrderItemResponseDTO> items;
}