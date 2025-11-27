package org.SmartShop.service.impl;

import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.order.OrderItemRequestDTO;
import org.SmartShop.dto.order.OrderRequestDTO;
import org.SmartShop.dto.order.OrderResponseDTO;
import org.SmartShop.entity.*;
import org.SmartShop.entity.enums.OrderStatus;
import org.SmartShop.mapper.OrderMapper;
import org.SmartShop.repository.*;
import org.SmartShop.service.LoyaltyService;
import org.SmartShop.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
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
    private final PromoCodeRepository promoCodeRepository; // Nouveau
    private final OrderMapper orderMapper;
    private final LoyaltyService loyaltyService;

    // TVA Configurable (Best Practice)
    @Value("${app.business.tva-rate:0.20}")
    private BigDecimal tvaRate;

    private static final BigDecimal PROMO_RATE = new BigDecimal("0.05"); // 5%

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // --- VALIDATION MÉTIER 1 : Client obligatoire ---
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Règle Métier : Une commande doit avoir un client valide."));

        // --- VALIDATION MÉTIER 2 : Panier non vide ---
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException("Règle Métier : Une commande ne peut pas être vide (sans articles).");
        }

        Order order = new Order();
        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subTotalHT = BigDecimal.ZERO;
        boolean stockInsufficient = false;

        // --- TRAITEMENT DES ARTICLES ---
        for (OrderItemRequestDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable ID: " + itemDto.getProductId()));

            // --- VALIDATION MÉTIER 3 : Stock ---
            if (product.getStockAvailable() < itemDto.getQuantity()) {
                stockInsufficient = true;
                // On continue pour calculer le montant, mais le statut sera REJECTED
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getUnitPriceHT());

            // Calcul Ligne
            BigDecimal lineTotal = product.getUnitPriceHT().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            orderItem.setLineTotal(lineTotal);

            orderItems.add(orderItem);
            subTotalHT = subTotalHT.add(lineTotal);
        }

        order.setOrderItems(orderItems);
        order.setSubTotalHT(subTotalHT);

        // Gestion statut Stock
        if (stockInsufficient) {
            order.setStatus(OrderStatus.REJECTED); // Règle: Refusée si stock insuffisant
        }

        // --- GESTION CODE PROMO (Best Practice) ---
        BigDecimal promoDiscount = BigDecimal.ZERO;
        if (dto.getPromoCode() != null && !dto.getPromoCode().isBlank()) {
            // 1. Vérification Format (Regex)
            if (!dto.getPromoCode().matches("PROMO-[A-Z0-9]{4}")) {
                throw new RuntimeException("Format Code Promo invalide. Attendu: PROMO-XXXX");
            }

            // 2. Vérification Existence et Validité en Base
            PromoCode promo = promoCodeRepository.findByCode(dto.getPromoCode())
                    .orElseThrow(() -> new RuntimeException("Code Promo inexistant ou invalide"));

            if (!promo.isActive()) {
                throw new RuntimeException("Ce Code Promo n'est plus actif.");
            }
            if (promo.isSingleUse() && promo.isUsed()) {
                throw new RuntimeException("Ce Code Promo a déjà été utilisé.");
            }

            // Application
            promoDiscount = subTotalHT.multiply(PROMO_RATE).setScale(2, RoundingMode.HALF_UP);
            order.setPromoCode(promo.getCode());

            // Marquer comme utilisé si usage unique (On le "consomme" à la création)
            if (promo.isSingleUse()) {
                promo.setUsed(true);
                promoCodeRepository.save(promo);
            }
        }

        // --- CALCULS FINANCIERS (Arrondis 2 décimales) ---
        BigDecimal loyaltyDiscount = loyaltyService.calculateLoyaltyDiscount(client, subTotalHT);
        BigDecimal totalDiscount = loyaltyDiscount.add(promoDiscount);
        order.setDiscountAmount(totalDiscount);

        BigDecimal htAfterDiscount = subTotalHT.subtract(totalDiscount);
        if (htAfterDiscount.compareTo(BigDecimal.ZERO) < 0) htAfterDiscount = BigDecimal.ZERO;
        order.setHtAfterDiscount(htAfterDiscount);

        // TVA Configurable
        BigDecimal taxAmount = htAfterDiscount.multiply(tvaRate).setScale(2, RoundingMode.HALF_UP);
        order.setTaxAmount(taxAmount);

        BigDecimal totalTTC = htAfterDiscount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
        order.setTotalTTC(totalTTC);
        order.setAmountRemaining(totalTTC); // Initialement, tout reste à payer

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderResponseDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (newStatus == OrderStatus.CONFIRMED) {
            // --- VALIDATION MÉTIER 4 : Paiement Complet ---
            if (order.getAmountRemaining().compareTo(BigDecimal.ZERO) > 0) {
                throw new RuntimeException("Validation Impossible : La commande n'est pas totalement payée. Reste : " + order.getAmountRemaining() + " DH");
            }

            if (order.getStatus() != OrderStatus.PENDING) {
                throw new RuntimeException("Seules les commandes PENDING peuvent être confirmées.");
            }

            // Décrémentation Stock
            for (OrderItem item : order.getOrderItems()) {
                Product p = item.getProduct();
                if (p.getStockAvailable() < item.getQuantity()) {
                    throw new RuntimeException("Stock insuffisant critique pour le produit : " + p.getName());
                }
                p.setStockAvailable(p.getStockAvailable() - item.getQuantity());
                productRepository.save(p);
            }

            // Mise à jour Stats Client
            updateClientStats(order);
        }
        else if (newStatus == OrderStatus.CANCELED) {
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new RuntimeException("Seules les commandes PENDING peuvent être annulées.");
            }
        }

        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private void updateClientStats(Order order) {
        Client client = order.getClient();
        client.setTotalOrders(client.getTotalOrders() + 1);
        BigDecimal currentSpent = client.getTotalSpent() != null ? client.getTotalSpent() : BigDecimal.ZERO;
        client.setTotalSpent(currentSpent.add(order.getTotalTTC()));

        if (client.getFirstOrderDate() == null) client.setFirstOrderDate(LocalDate.now());
        client.setLastOrderDate(LocalDate.now());

        loyaltyService.updateClientTier(client);
        clientRepository.save(client);
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