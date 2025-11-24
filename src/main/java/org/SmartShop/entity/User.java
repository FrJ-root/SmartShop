package org.SmartShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.SmartShop.entity.enums.UserRole;

@Entity
@Table(name = "app_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne
    private Client client;
}
