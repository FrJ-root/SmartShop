package org.SmartShop.dto.payment;

import jakarta.validation.constraints. NotNull;
import lombok.*;
import org.SmartShop. entity.enums.PaymentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentStatusRequest {

    @NotNull(message = "Le statut est obligatoire")
    private PaymentStatus status;

    private String reason;
}