package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.OrderStatus;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private LocalDate date;

    private double sousTotal;
    private double remise;
    private double montantHT;
    private double tva;
    private double totalTTC;

    private double montantRestant;

    private String codePromo;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
