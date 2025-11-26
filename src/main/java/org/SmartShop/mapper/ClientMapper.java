package org.SmartShop.mapper;

import org.SmartShop.dto.client.ClientOrderHistoryDTO;
import org.SmartShop.dto.client.ClientRequestDTO;
import org.SmartShop.dto.client.ClientResponseDTO;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponseDTO toDto(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tier", ignore = true) // Set by logic
    @Mapping(target = "totalOrders", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "lastOrderDate", ignore = true)
    @Mapping(target = "linkedAccount", ignore = true) // Handled manually
    Client toEntity(ClientRequestDTO dto);

    ClientOrderHistoryDTO toHistoryDto(Order order);
}