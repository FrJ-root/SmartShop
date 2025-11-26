package org.SmartShop.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.SmartShop.dto.client.ClientDto;
import org.SmartShop.dto.payment.PaymentDto;  // Add this import
import org.SmartShop.entity.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private ClientDto client;
    private List<OrderItemDto> items;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BigDecimal subTotalHT;
    private BigDecimal discountAmount;
    private BigDecimal htAfterDiscount;
    private BigDecimal taxAmount;
    private BigDecimal totalTTC;
    private BigDecimal amountRemaining;
    private String promoCode;
    private OrderStatus status;
    private List<PaymentDto> payments;
}