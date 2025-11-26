package org.SmartShop.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta. validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time. LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    @NotNull(message = "L'ID de la commande est obligatoire")
    private Long orderId;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0. 01", message = "Le montant doit être supérieur à 0")
    @Digits(integer = 8, fraction = 2, message = "Format de montant invalide")
    private BigDecimal montant;

    @NotBlank(message = "Le type de paiement est obligatoire")
    @Pattern(regexp = "ESPECES|CHEQUE|VIREMENT", message = "Type de paiement invalide")
    private String paymentType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    // For checks and transfers
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String reference;
    private String bank;
}