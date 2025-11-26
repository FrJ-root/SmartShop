package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixHT;

    @Column(nullable = false)
    @Builder. Default
    private Integer stock = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false; // Lombok will generate isDeleted() automatically

    // Business method to decrement stock
    public void decrementStock(int quantity) {
        if (this.stock >= quantity) {
            this.stock -= quantity;
        } else {
            throw new IllegalArgumentException("Stock insuffisant");
        }
    }

    // Business method to check if available
    public boolean isAvailable(int requestedQuantity) {
        return !this.deleted && this.stock >= requestedQuantity;
    }
}