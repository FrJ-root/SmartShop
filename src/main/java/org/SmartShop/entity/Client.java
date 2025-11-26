package org. SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop. entity.enums.CustomerTier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerTier tier = CustomerTier.BASIC;

    @Builder.Default
    private Integer totalOrders = 0;

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToOne(cascade = CascadeType. ALL, fetch = FetchType. LAZY)
    @JoinColumn(name = "user_id")
    private User user; // This should exist for the relationship

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Commande> commandes = new ArrayList<>();

    // Business method to recalculate tier
    public void recalcTier() {
        if (totalOrders >= 20 || totalSpent.compareTo(new BigDecimal("15000")) >= 0) {
            this.tier = CustomerTier.PLATINUM;
        } else if (totalOrders >= 10 || totalSpent.compareTo(new BigDecimal("5000")) >= 0) {
            this.tier = CustomerTier.GOLD;
        } else if (totalOrders >= 3 || totalSpent.compareTo(new BigDecimal("1000")) >= 0) {
            this.tier = CustomerTier. SILVER;
        } else {
            this.tier = CustomerTier.BASIC;
        }
    }
}