package org.SmartShop.service;

import org.SmartShop.dto.order.OrderRequestDTO;
import org.SmartShop.dto.order.OrderResponseDTO;
import org.SmartShop.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO dto);
    OrderResponseDTO getOrderById(Long id);
    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO updateStatus(Long id, OrderStatus newStatus);
}