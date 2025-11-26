package org.SmartShop.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal unitPriceHT;
    private int stockAvailable;
    private boolean deleted;
}