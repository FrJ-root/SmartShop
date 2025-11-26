package org. SmartShop.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;

    @NotEmpty(message = "La commande doit contenir au moins un article")
    @Valid
    private List<OrderItemRequest> items;

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Format de code promo invalide (PROMO-XXXX)")
    private String promoCode;
}