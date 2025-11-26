package org.SmartShop.mapper;

import org.SmartShop.dto.payment.PaymentResponseDTO; // Check this import
import org.SmartShop.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponseDTO toDto(Payment payment);
}