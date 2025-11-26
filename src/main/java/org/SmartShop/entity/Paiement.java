package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(name = "payment_number", nullable = false)
    private Integer paymentNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "payment_type", nullable = false)
    private String paymentType; // ESPECES, CHEQUE, VIREMENT

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;

    @Column(name = "payment_date")
    @Builder.Default
    private LocalDate paymentDate = LocalDate.now();

    @Column(name = "encashment_date")
    private LocalDate encashmentDate;

    @Column(name = "due_date")
    private LocalDate dueDate; // For checks

    private String reference;
    private String bank;

    public void validatePayment() {
        if ("ESPECES".equals(paymentType) && montant. compareTo(new BigDecimal("20000")) > 0) {
            throw new IllegalArgumentException("Paiement en espèces limité à 20,000 DH");
        }
    }

    public void encash() {
        this.status = PaymentStatus. ENCAISSE;
        this.encashmentDate = LocalDate.now();
    }

    public void reject() {
        this.status = PaymentStatus.REJETE;
    }
}