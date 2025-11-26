package org. SmartShop.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.SmartShop.entity. enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BigDecimal totalTTC;
    private OrderStatus status;
    private Integer itemCount;
    private String promoCode;
}