package org.SmartShop.service.impl;

import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.order.OrderItemRequestDTO;
import org.SmartShop.dto.order.OrderRequestDTO;
import org.SmartShop.dto.order.OrderResponseDTO;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.Order;
import org.SmartShop.entity.OrderItem;
import org.SmartShop.entity.Product;
import org.SmartShop.entity.enums.CustomerTier;
import org.SmartShop.entity.enums.OrderStatus;
import org.SmartShop.mapper.OrderMapper;
import org.SmartShop.repository.ClientRepository;
import org.SmartShop.repository.OrderRepository;
import org.SmartShop.repository.ProductRepository;
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

    // Constantes
    private static final BigDecimal TVA_RATE = new BigDecimal("0.20"); // 20% [cite: 75]
    private static final BigDecimal PROMO_RATE = new BigDecimal("0.05"); // 5% [cite: 71]

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // 1. Valider le Client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // 2. Initialiser la commande
        Order order = new Order();
        order.setClient(client);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING); // [cite: 85]
        order.setPromoCode(dto.getPromoCode());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subTotalHT = BigDecimal.ZERO;
        boolean stockInsufficient = false;

        // 3. Traiter les articles (Items)
        for (OrderItemRequestDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));

            // Vérification Stock [cite: 100]
            if (product.getStockAvailable() < itemDto.getQuantity()) {
                stockInsufficient = true;
                // On continue pour calculer le total théorique, ou on break ?
                // Le cahier des charges dit "PENDING -> REJECTED si stock insuffisant".
                // On marquera la commande REJECTED à la fin.
            }

            // Création de la ligne de commande
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getUnitPriceHT()); // Prix gelé au moment de la commande

            // Calcul ligne : Prix * Qte [cite: 73]
            BigDecimal lineTotal = product.getUnitPriceHT().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            orderItem.setLineTotal(lineTotal);

            orderItems.add(orderItem);
            subTotalHT = subTotalHT.add(lineTotal);
        }

        order.setOrderItems(orderItems);
        order.setSubTotalHT(subTotalHT);

        // 4. Si Stock Insuffisant -> REJECTED immédiat [cite: 89, 164]
        if (stockInsufficient) {
            order.setStatus(OrderStatus.REJECTED);
            // On sauvegarde quand même pour l'historique (optionnel mais recommandé)
            // Pour cet exercice, on peut choisir de sauvegarder ou lancer une exception.
            // Le cahier des charges demande de gérer le statut REJECTED, donc on sauvegarde.
        }

        // 5. Calcul des Remises (Loyalty + Promo)
        BigDecimal totalDiscountAmount = calculateTotalDiscount(client, subTotalHT, dto.getPromoCode());
        order.setDiscountAmount(totalDiscountAmount);

        // 6. Calcul HT Net [cite: 75]
        // HT après remise = Sous-total - Remise
        BigDecimal htAfterDiscount = subTotalHT.subtract(totalDiscountAmount);
        if (htAfterDiscount.compareTo(BigDecimal.ZERO) < 0) htAfterDiscount = BigDecimal.ZERO;
        order.setHtAfterDiscount(htAfterDiscount);

        // 7. Calcul TVA [cite: 75]
        // TVA = HT après remise * 20%
        BigDecimal taxAmount = htAfterDiscount.multiply(TVA_RATE).setScale(2, RoundingMode.HALF_UP);
        order.setTaxAmount(taxAmount);

        // 8. Calcul TTC [cite: 75]
        BigDecimal totalTTC = htAfterDiscount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
        order.setTotalTTC(totalTTC);

        // Initialement, le montant restant est le total TTC (rien n'est payé)
        order.setAmountRemaining(totalTTC);

        // 9. Sauvegarde
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    // --- Logique de Calcul des Remises ---
    private BigDecimal calculateTotalDiscount(Client client, BigDecimal subTotal, String promoCode) {
        BigDecimal discountPercent = BigDecimal.ZERO;

        // A. Remise Fidélité [cite: 40-42]
        CustomerTier tier = client.getTier();
        if (tier == CustomerTier.PLATINUM && subTotal.compareTo(BigDecimal.valueOf(1200)) >= 0) {
            discountPercent = discountPercent.add(new BigDecimal("0.15"));
        } else if (tier == CustomerTier.GOLD && subTotal.compareTo(BigDecimal.valueOf(800)) >= 0) {
            discountPercent = discountPercent.add(new BigDecimal("0.10"));
        } else if (tier == CustomerTier.SILVER && subTotal.compareTo(BigDecimal.valueOf(500)) >= 0) {
            discountPercent = discountPercent.add(new BigDecimal("0.05"));
        }
        // BASIC = 0%

        // B. Code Promo [cite: 71]
        if (promoCode != null && promoCode.matches("PROMO-[A-Z0-9]{4}")) { // [cite: 102]
            discountPercent = discountPercent.add(PROMO_RATE); // +5% cumulatif
        }

        // Montant Remise = Sous-total * %Total
        return subTotal.multiply(discountPercent).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public OrderResponseDTO updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Gestion de la Confirmation (PENDING -> CONFIRMED)
        if (newStatus == OrderStatus.CONFIRMED) {
            // [cite: 96, 107] Validation impossible si non payé totalement
            if (order.getAmountRemaining().compareTo(BigDecimal.ZERO) > 0) {
                throw new RuntimeException("Cannot confirm order: Payment not complete. Remaining: " + order.getAmountRemaining());
            }
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new RuntimeException("Only PENDING orders can be confirmed");
            }

            // ACTIONS POST-VALIDATION [cite: 78]

            // A. Décrémenter le stock [cite: 81]
            for (OrderItem item : order.getOrderItems()) {
                Product p = item.getProduct();
                if (p.getStockAvailable() < item.getQuantity()) {
                    throw new RuntimeException("Stock insufficient for confirmation: " + p.getName());
                }
                p.setStockAvailable(p.getStockAvailable() - item.getQuantity());
                productRepository.save(p);
            }

            // B. Actualiser stats Client [cite: 82]
            Client client = order.getClient();
            client.setTotalOrders(client.getTotalOrders() + 1);

            BigDecimal currentSpent = client.getTotalSpent() != null ? client.getTotalSpent() : BigDecimal.ZERO;
            client.setTotalSpent(currentSpent.add(order.getTotalTTC())); // On cumule le TTC

            if (client.getFirstOrderDate() == null) client.setFirstOrderDate(LocalDate.now());
            client.setLastOrderDate(LocalDate.now());

            // C. Recalculer Niveau Fidélité [cite: 83, 34-37]
            recalculateClientTier(client);

            clientRepository.save(client);
        } else if (newStatus == OrderStatus.CANCELED) {
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new RuntimeException("Only PENDING orders can be canceled");
            }
        }

        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    // --- Recalcul Fidélité [cite: 36, 37] ---
    private void recalculateClientTier(Client client) {
        int orders = client.getTotalOrders();
        BigDecimal spent = client.getTotalSpent();

        // Logique stricte du cahier des charges
        if (orders >= 20 || spent.compareTo(BigDecimal.valueOf(15000)) >= 0) {
            client.setTier(CustomerTier.PLATINUM);
        } else if (orders >= 10 || spent.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            client.setTier(CustomerTier.GOLD);
        } else if (orders >= 3 || spent.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            client.setTier(CustomerTier.SILVER);
        } else {
            client.setTier(CustomerTier.BASIC);
        }
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