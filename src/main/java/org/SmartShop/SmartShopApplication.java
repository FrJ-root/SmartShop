package org.SmartShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.SmartShop.repository")
@ComponentScan(basePackages = {"org.SmartShop"})
public class SmartShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartShopApplication.class, args);
    }
}