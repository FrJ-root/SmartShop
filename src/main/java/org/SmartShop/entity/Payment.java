package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment { // [cite: 134]
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int paymentNumber; // Sequential number (1, 2...)
    private BigDecimal amount;
    private String paymentType; // ESPECES, CHEQUE, VIREMENT [cite: 92]

    private LocalDate paymentDate;
    private LocalDate encashmentDate; // date_encaissement
    private LocalDate dueDate; // For checks [cite: 92]

    private String reference; // Check number or Transfer Ref
    private String bank;      // Bank name

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}