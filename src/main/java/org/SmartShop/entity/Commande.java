package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.OrderStatus;
import java.math. BigDecimal;
import java. time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "commande", cascade = CascadeType. ALL, fetch = FetchType. LAZY)
    @Builder. Default
    private List<Paiement> paiements = new ArrayList<>();

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "sub_total_ht", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal subTotalHT = BigDecimal. ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder. Default
    private BigDecimal discountAmount = BigDecimal. ZERO;

    @Column(name = "ht_after_discount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal htAfterDiscount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal. ZERO;

    @Column(name = "total_ttc", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalTTC = BigDecimal. ZERO;

    @Column(name = "amount_remaining", precision = 10, scale = 2)
    @Builder. Default
    private BigDecimal amountRemaining = BigDecimal. ZERO;

    @Column(name = "promo_code")
    private String promoCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;


}