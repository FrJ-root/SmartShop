package org.SmartShop.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.SmartShop.entity.enums.CustomerTier;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long id;
    private String nom;
    private String email;
    private CustomerTier tier;
    private Integer totalOrders;
    private BigDecimal totalSpent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate firstOrderDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastOrderDate;
}