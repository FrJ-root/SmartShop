package org.SmartShop.mapper;

import org.mapstruct.*;
import org.SmartShop.dto.order.OrderItemDto;
import org.SmartShop. dto.order.OrderItemRequest;
import org.SmartShop. entity.OrderItem;

@Mapper(
        componentModel = "spring",
        uses = {ProductMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderItemMapper {

    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "lineTotal", ignore = true)
    @Mapping(target = "commande", ignore = true)
    OrderItem toEntity(OrderItemRequest request);
}