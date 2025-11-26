package org. SmartShop.dto.order;

import lombok.*;
import org.SmartShop.dto.product.ProductDto;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}