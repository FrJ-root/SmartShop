package org.SmartShop.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull
    @Positive(message = "Price must be positive")
    private BigDecimal unitPriceHT;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stockAvailable;
}