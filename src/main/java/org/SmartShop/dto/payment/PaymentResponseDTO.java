package org.SmartShop.dto.payment;

import lombok.Data;
import org.SmartShop.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentResponseDTO {
    private Long id;
    private int paymentNumber;
    private BigDecimal amount;
    private String paymentType;
    private LocalDate paymentDate;
    private PaymentStatus status;
}