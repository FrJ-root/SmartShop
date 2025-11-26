package org.SmartShop.dto.order;

import jakarta.validation.constraints. NotNull;
import lombok.*;
import org.SmartShop.entity. enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

    @NotNull(message = "Le statut est obligatoire")
    private OrderStatus status;

    private String reason;
}