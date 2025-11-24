package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.CustomerTier;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier tier;

    private int totalOrders;
    private double totalSpent;

    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToMany(mappedBy = "client")
    private List<Commande> commandes;
}