package org.SmartShop.service.impl;

import org.SmartShop.dto.order.OrderItemRequestDTO;
import org.SmartShop.dto.order.OrderRequestDTO;
import org.SmartShop.dto.order.OrderResponseDTO;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.Order;
import org.SmartShop.entity.Product;
import org.SmartShop.entity.enums.OrderStatus;
import org.SmartShop.mapper.OrderMapper;
import org.SmartShop.repository.ClientRepository;
import org.SmartShop.repository.OrderRepository;
import org.SmartShop.repository.ProductRepository;
import org.SmartShop.repository.PromoCodeRepository;
import org.SmartShop.service.LoyaltyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PromoCodeRepository promoCodeRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private LoyaltyService loyaltyService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client mockClient;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderService, "tvaRate", new BigDecimal("0.20"));

        mockClient = new Client();
        mockClient.setId(1L);

        mockProduct = Product.builder()
                .id(1L)
                .name("Laptop Test")
                .unitPriceHT(new BigDecimal("1000.00"))
                .stockAvailable(10)
                .build();
    }

    @Test
    @DisplayName("Devrait créer une commande standard avec calcul TVA correct")
    void testCreateOrder_StandardCalculation() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setClientId(1L);
        OrderItemRequestDTO itemDto = new OrderItemRequestDTO();
        itemDto.setProductId(1L);
        itemDto.setQuantity(2);
        request.setItems(List.of(itemDto));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(loyaltyService.calculateLoyaltyDiscount(any(), any())).thenReturn(BigDecimal.ZERO);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderResponseDTO());

        // --- ACT ---
        orderService.createOrder(request);

        // --- ASSERT ---
        // On capture l'objet Order passé à la méthode save() pour vérifier les calculs internes
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        // 1. Vérification Statut
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());

        // 2. Vérification Calculs Financiers
        // Sous-total = 1000 * 2 = 2000
        assertEquals(0, new BigDecimal("2000.00").compareTo(savedOrder.getSubTotalHT()), "Le sous-total est incorrect");

        // HT après remise (0 remise) = 2000
        assertEquals(0, new BigDecimal("2000.00").compareTo(savedOrder.getHtAfterDiscount()));

        // TVA = 2000 * 0.20 = 400
        assertEquals(0, new BigDecimal("400.00").compareTo(savedOrder.getTaxAmount()), "Le montant TVA est incorrect");

        // TTC = 2000 + 400 = 2400
        assertEquals(0, new BigDecimal("2400.00").compareTo(savedOrder.getTotalTTC()), "Le TTC est incorrect");
    }

    @Test
    @DisplayName("Devrait rejeter la commande si le stock est insuffisant")
    void testCreateOrder_InsufficientStock_ShouldReject() {
        // --- ARRANGE ---
        // Le produit a 10 en stock (défini dans setUp), on en demande 15
        OrderRequestDTO request = new OrderRequestDTO();
        request.setClientId(1L);
        OrderItemRequestDTO itemDto = new OrderItemRequestDTO();
        itemDto.setProductId(1L);
        itemDto.setQuantity(15);
        request.setItems(List.of(itemDto));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        // Les autres mocks (loyalty, mapper) sont nécessaires pour éviter les NullPointer
        when(loyaltyService.calculateLoyaltyDiscount(any(), any())).thenReturn(BigDecimal.ZERO);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        when(orderMapper.toDto(any())).thenReturn(new OrderResponseDTO());

        // --- ACT ---
        orderService.createOrder(request);

        // --- ASSERT ---
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        // Vérification cruciale : Le statut doit être REJECTED
        assertEquals(OrderStatus.REJECTED, savedOrder.getStatus(), "La commande devrait être REJECTED pour stock insuffisant");
    }
}