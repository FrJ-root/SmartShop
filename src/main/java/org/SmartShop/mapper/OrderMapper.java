package org.SmartShop.mapper;

import org. mapstruct.*;
import org. SmartShop.dto.order.*;
import org.SmartShop.entity. Commande;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {ClientMapper.class, ProductMapper.class, PaymentMapper.class, OrderItemMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {

    // Entity to DTO mappings
    @Mapping(target = "payments", source = "paiements")
    OrderDto toDto(Commande commande);

    @Mapping(target = "itemCount", expression = "java(commande.getItems() != null ? commande.getItems(). size() : 0)")
    OrderHistoryDto toHistoryDto(Commande commande);

    List<OrderHistoryDto> toHistoryDtoList(List<Commande> commandes);

    // Request to Entity mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "subTotalHT", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "htAfterDiscount", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "totalTTC", ignore = true)
    @Mapping(target = "amountRemaining", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Commande toEntity(CreateOrderRequest request);
}