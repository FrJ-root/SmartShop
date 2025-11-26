package org.SmartShop.dto.product;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String nom;
    private BigDecimal prixHT;
    private Integer stock;
    private Boolean deleted;
    private Boolean available;
}