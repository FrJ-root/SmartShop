package org.SmartShop.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.SmartShop.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequestDTO {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull @Positive
    private BigDecimal amount;

    @NotNull(message = "Payment type is required (ESPECES, CHEQUE, VIREMENT)")
    private String paymentType;

    private String reference;
    private String bank;
    private LocalDate dueDate;
    private PaymentStatus status;
}