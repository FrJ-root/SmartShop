package org.SmartShop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.order.OrderRequestDTO;
import org.SmartShop.dto.order.OrderResponseDTO;
import org.SmartShop.entity.enums.OrderStatus;
import org.SmartShop.entity.enums.UserRole;
import org.SmartShop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO dto, HttpSession session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            HttpSession session) {

        checkAdminAccess(session);
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(HttpSession session) {
        checkAdminAccess(session);
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    private void checkAdminAccess(HttpSession session) {
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) {
            throw new RuntimeException("Access Denied: Admin role required");
        }
    }
}