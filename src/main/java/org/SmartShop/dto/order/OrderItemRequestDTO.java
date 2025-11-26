package org.SmartShop.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDTO {
    @NotNull
    private Long productId;
    @Min(1)
    private int quantity;
}