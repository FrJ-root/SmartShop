package org.SmartShop.service.impl;

import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.client.ClientOrderHistoryDTO;
import org.SmartShop.dto.client.ClientRequestDTO;
import org.SmartShop.dto.client.ClientResponseDTO;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.User;
import org.SmartShop.entity.enums.CustomerTier;
import org.SmartShop.entity.enums.UserRole;
import org.SmartShop.mapper.ClientMapper;
import org.SmartShop.repository.ClientRepository;
import org.SmartShop.repository.OrderRepository;
import org.SmartShop.repository.UserRepository;
import org.SmartShop.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDTO createClient(ClientRequestDTO dto) {
        // 1. Check uniqueness
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // 2. Create User Account (Role CLIENT) [cite: 13]
        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword()) // Plain text as per no-security rule
                .role(UserRole.CLIENT)
                .build();
        user = userRepository.save(user);

        // 3. Create Client Profile [cite: 34]
        Client client = clientMapper.toEntity(dto);
        client.setLinkedAccount(user);
        client.setTier(CustomerTier.BASIC); // Default Tier
        client.setTotalOrders(0);
        client.setTotalSpent(BigDecimal.ZERO);

        client = clientRepository.save(client);

        return clientMapper.toDto(client);
    }

    @Override
    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return clientMapper.toDto(client);
    }

    @Override
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Only update personal info [cite: 18]
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());

        // Update User credentials if provided
        if (client.getLinkedAccount() != null) {
            client.getLinkedAccount().setUsername(dto.getUsername());
            client.getLinkedAccount().setPassword(dto.getPassword());
        }

        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public void deleteClient(Long id) {
        // Deleting client also deletes the User due to CascadeType.ALL in Entity
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(id);
    }

    @Override
    public List<ClientOrderHistoryDTO> getClientOrderHistory(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }
        // Fetch orders and map to History DTO [cite: 23]
        return orderRepository.findByClientId(id).stream()
                .map(clientMapper::toHistoryDto)
                .collect(Collectors.toList());
    }
}