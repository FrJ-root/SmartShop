package org.SmartShop.config;

import org.SmartShop.entity.User;
import org.SmartShop.entity.enums.UserRole;
import org.SmartShop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password("admin123")
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("---- ADMIN USER CREATED: admin / admin123 ----");
            }
        };
    }
}