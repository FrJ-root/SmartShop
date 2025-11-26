package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.OrderStatus;
import java.math. BigDecimal;
import java. time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "orders")
public class Order { // [cite: 132]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    // Financial calculations [cite: 72]
    private BigDecimal subTotalHT;
    private BigDecimal discountAmount;
    private BigDecimal htAfterDiscount;
    private BigDecimal taxAmount; // TVA 20%
    private BigDecimal totalTTC;

    private String promoCode; // [cite: 71]

    private BigDecimal amountRemaining; // [cite: 96]

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;
}