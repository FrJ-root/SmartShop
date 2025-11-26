package org.SmartShop.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.SmartShop.entity.enums.PaymentStatus;
import java.math. BigDecimal;
import java. time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private Long orderId;
    private Integer paymentNumber;
    private BigDecimal montant;
    private String paymentType;
    private PaymentStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate encashmentDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String reference;
    private String bank;
}