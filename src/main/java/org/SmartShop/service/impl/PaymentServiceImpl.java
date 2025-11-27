package org.SmartShop.service.impl;

import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.payment.PaymentRequestDTO;
import org.SmartShop.dto.payment.PaymentResponseDTO;
import org.SmartShop.entity.Order;
import org.SmartShop.entity.Payment;
import org.SmartShop.entity.enums.PaymentStatus;
import org.SmartShop.mapper.PaymentMapper;
import org.SmartShop.repository.OrderRepository;
import org.SmartShop.repository.PaymentRepository;
import org.SmartShop.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDTO recordPayment(PaymentRequestDTO dto) {
        // 1. Retrieve Order
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Validate Amount (Cannot pay more than remaining)
        if (dto.getAmount().compareTo(order.getAmountRemaining()) > 0) {
            throw new RuntimeException("Payment amount exceeds remaining balance. Remaining: " + order.getAmountRemaining());
        }

        // 3. Create Payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setPaymentType(dto.getPaymentType());
        payment.setPaymentDate(LocalDate.now());
        payment.setReference(dto.getReference());
        payment.setBank(dto.getBank());
        payment.setDueDate(dto.getDueDate());

        // Determine Payment Number (Increment based on existing payments)
        int nextPaymentNum = order.getPayments() == null ? 1 : order.getPayments().size() + 1;
        payment.setPaymentNumber(nextPaymentNum);

        // Auto-Status Logic [cite: 92]
        if (dto.getStatus() != null) {
            payment.setStatus(dto.getStatus());
        } else {
            // Default logic if not provided
            if ("ESPECES".equalsIgnoreCase(dto.getPaymentType())) {
                payment.setStatus(PaymentStatus.ENCAISSE); // Cash is immediate
                payment.setEncashmentDate(LocalDate.now());
            } else {
                payment.setStatus(PaymentStatus.EN_ATTENTE); // Check/Transfer waiting
            }
        }

        // If status is ENCAISSE, set date
        if (payment.getStatus() == PaymentStatus.ENCAISSE && payment.getEncashmentDate() == null) {
            payment.setEncashmentDate(LocalDate.now());
        }

        Payment savedPayment = paymentRepository.save(payment);

        // 4. Update Order Remaining Amount [cite: 96]
        // We only reduce the amount if the payment is VALID (ENCAISSE or EN_ATTENTE accepted for calculation?)
        // Usually, 'Remaining' decreases immediately when payment is recorded, even if pending clearing.
        BigDecimal newRemaining = order.getAmountRemaining().subtract(dto.getAmount());
        order.setAmountRemaining(newRemaining);
        orderRepository.save(order);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        return paymentRepository.findByOrderId(orderId).stream()
                .map(paymentMapper::toDto)
                .toList();
    }
}