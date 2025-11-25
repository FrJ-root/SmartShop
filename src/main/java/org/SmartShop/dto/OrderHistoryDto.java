package org.SmartShop.dto;

import org.SmartShop.entity.enums.OrderStatus;
import java.time.LocalDate;

public class OrderHistoryDto {
    private Long id;
    private LocalDate date;
    private double totalTTC;
    private OrderStatus status;

    public OrderHistoryDto() {}

    public OrderHistoryDto(Long id, LocalDate date, double totalTTC, OrderStatus status) {
        this.id = id;
        this.date = date;
        this.totalTTC = totalTTC;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getTotalTTC() { return totalTTC; }
    public void setTotalTTC(double totalTTC) { this.totalTTC = totalTTC; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private LocalDate date;
        private double totalTTC;
        private OrderStatus status;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder date(LocalDate date) { this.date = date; return this; }
        public Builder totalTTC(double totalTTC) { this.totalTTC = totalTTC; return this; }
        public Builder status(OrderStatus status) { this.status = status; return this; }

        public OrderHistoryDto build() {
            return new OrderHistoryDto(id, date, totalTTC, status);
        }
    }
}