package org.SmartShop.dto;

import org.SmartShop.entity.enums.CustomerTier;
import java.time.LocalDate;

public class ClientDto {
    private Long id;
    private String nom;
    private String email;
    private CustomerTier tier;
    private int totalOrders;
    private double totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    public ClientDto() {}

    public ClientDto(Long id, String nom, String email, CustomerTier tier,
                     int totalOrders, double totalSpent,
                     LocalDate firstOrderDate, LocalDate lastOrderDate) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.tier = tier;
        this.totalOrders = totalOrders;
        this. totalSpent = totalSpent;
        this.firstOrderDate = firstOrderDate;
        this. lastOrderDate = lastOrderDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public CustomerTier getTier() { return tier; }
    public void setTier(CustomerTier tier) { this.tier = tier; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }

    public LocalDate getFirstOrderDate() { return firstOrderDate; }
    public void setFirstOrderDate(LocalDate firstOrderDate) { this.firstOrderDate = firstOrderDate; }

    public LocalDate getLastOrderDate() { return lastOrderDate; }
    public void setLastOrderDate(LocalDate lastOrderDate) { this.lastOrderDate = lastOrderDate; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String nom;
        private String email;
        private CustomerTier tier;
        private int totalOrders;
        private double totalSpent;
        private LocalDate firstOrderDate;
        private LocalDate lastOrderDate;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder nom(String nom) { this.nom = nom; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder tier(CustomerTier tier) { this.tier = tier; return this; }
        public Builder totalOrders(int totalOrders) { this.totalOrders = totalOrders; return this; }
        public Builder totalSpent(double totalSpent) { this.totalSpent = totalSpent; return this; }
        public Builder firstOrderDate(LocalDate firstOrderDate) { this.firstOrderDate = firstOrderDate; return this; }
        public Builder lastOrderDate(LocalDate lastOrderDate) { this. lastOrderDate = lastOrderDate; return this; }

        public ClientDto build() {
            return new ClientDto(id, nom, email, tier, totalOrders, totalSpent, firstOrderDate, lastOrderDate);
        }
    }
}