package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.PaymentStatus;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Commande commande;

    private int numeroPaiement;

    private double montant;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDate datePaiement;
    private LocalDate dateEncaissement;

    private String reference;
    private String banque;
}
