package org.SmartShop.controller;

import org.SmartShop.dto.payment.PaymentResponseDTO;
import org.SmartShop.dto.payment.PaymentRequestDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.SmartShop.service.PaymentService;
import org.SmartShop.entity.enums.UserRole;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> recordPayment(@RequestBody @Valid PaymentRequestDTO dto, HttpSession session) {
        checkAdminAccess(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.recordPayment(dto));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrder(@PathVariable Long orderId, HttpSession session) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId));
    }

    private void checkAdminAccess(HttpSession session) {
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) {
            throw new RuntimeException("Access Denied: Admin role required -_-");
        }
    }

}