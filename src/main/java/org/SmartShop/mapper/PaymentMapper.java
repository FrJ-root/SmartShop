package org.SmartShop.mapper;

import org.mapstruct.*;
import org.SmartShop.dto.payment.*;
import org.SmartShop.entity.Paiement;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentMapper {

    // Entity to DTO mappings
    @Mapping(target = "orderId", source = "commande.id")
    PaymentDto toDto(Paiement paiement);

    // Request to Entity mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commande", ignore = true) // Will be set by service
    @Mapping(target = "paymentNumber", ignore = true) // Will be calculated by service
    @Mapping(target = "status", constant = "EN_ATTENTE")
    @Mapping(target = "paymentDate", expression = "java(request.getPaymentDate() != null ? request.getPaymentDate() : java.time.LocalDate.now())")
    @Mapping(target = "encashmentDate", ignore = true)
    Paiement toEntity(CreatePaymentRequest request);
}