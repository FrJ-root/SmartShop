package org.SmartShop.service.impl;

import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.order.OrderItemRequestDTO;
import org.SmartShop.dto.order.OrderRequestDTO;
import org.SmartShop.dto.order.OrderResponseDTO;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.Order;
import org.SmartShop.entity.OrderItem;
import org.SmartShop.entity.Product;
import org.SmartShop.entity.enums.OrderStatus;
import org.SmartShop.mapper.OrderMapper;
import org.SmartShop.repository.ClientRepository;
import org.SmartShop.repository.OrderRepository;
import org.SmartShop.repository.ProductRepository;
import org.SmartShop.service.LoyaltyService;
import org.SmartShop.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    private final LoyaltyService loyaltyService;

    private static final BigDecimal TVA_RATE = new BigDecimal("0.20");
    private static final BigDecimal PROMO_RATE = new BigDecimal("0.05");

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPromoCode(dto.getPromoCode());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subTotalHT = BigDecimal.ZERO;
        boolean stockInsufficient = false;

        for (OrderItemRequestDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));

            if (product.getStockAvailable() < itemDto.getQuantity()) {
                stockInsufficient = true;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getUnitPriceHT());

            BigDecimal lineTotal = product.getUnitPriceHT().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            orderItem.setLineTotal(lineTotal);

            orderItems.add(orderItem);
            subTotalHT = subTotalHT.add(lineTotal);
        }

        order.setOrderItems(orderItems);
        order.setSubTotalHT(subTotalHT);

        if (stockInsufficient) {
            order.setStatus(OrderStatus.REJECTED);
        }


        // 1. Remise Fidélité (Via le service dédié)
        BigDecimal loyaltyDiscount = loyaltyService.calculateLoyaltyDiscount(client, subTotalHT);

        // 2. Remise Code Promo (Calcul local simple)
        BigDecimal promoDiscount = BigDecimal.ZERO;
        if (dto.getPromoCode() != null && dto.getPromoCode().matches("PROMO-[A-Z0-9]{4}")) {
            promoDiscount = subTotalHT.multiply(PROMO_RATE).setScale(2, RoundingMode.HALF_UP);
        }

        // 3. Total Remises (Cumulatives)
        BigDecimal totalDiscount = loyaltyDiscount.add(promoDiscount);
        order.setDiscountAmount(totalDiscount);

        // --- Suite des calculs financiers ---
        BigDecimal htAfterDiscount = subTotalHT.subtract(totalDiscount);
        if (htAfterDiscount.compareTo(BigDecimal.ZERO) < 0) htAfterDiscount = BigDecimal.ZERO;
        order.setHtAfterDiscount(htAfterDiscount);

        BigDecimal taxAmount = htAfterDiscount.multiply(TVA_RATE).setScale(2, RoundingMode.HALF_UP);
        order.setTaxAmount(taxAmount);

        BigDecimal totalTTC = htAfterDiscount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
        order.setTotalTTC(totalTTC);
        order.setAmountRemaining(totalTTC);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponseDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (newStatus == OrderStatus.CONFIRMED) {
            if (order.getAmountRemaining().compareTo(BigDecimal.ZERO) > 0) {
                throw new RuntimeException("Cannot confirm order: Payment not complete. Remaining: " + order.getAmountRemaining());
            }
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new RuntimeException("Only PENDING orders can be confirmed");
            }

            // Gestion Stocks
            for (OrderItem item : order.getOrderItems()) {
                Product p = item.getProduct();
                if (p.getStockAvailable() < item.getQuantity()) {
                    throw new RuntimeException("Stock insufficient for confirmation: " + p.getName());
                }
                p.setStockAvailable(p.getStockAvailable() - item.getQuantity());
                productRepository.save(p);
            }

            // --- MISE À JOUR CLIENT & FIDÉLITÉ (Refactorisé) ---
            Client client = order.getClient();

            // 1. Mise à jour stats
            client.setTotalOrders(client.getTotalOrders() + 1);
            BigDecimal currentSpent = client.getTotalSpent() != null ? client.getTotalSpent() : BigDecimal.ZERO;
            client.setTotalSpent(currentSpent.add(order.getTotalTTC()));

            if (client.getFirstOrderDate() == null) client.setFirstOrderDate(LocalDate.now());
            client.setLastOrderDate(LocalDate.now());

            // 2. Recalcul automatique du niveau via le service
            loyaltyService.updateClientTier(client);

            clientRepository.save(client);

        } else if (newStatus == OrderStatus.CANCELED) {
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new RuntimeException("Only PENDING orders can be canceled");
            }
        }

        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }
}