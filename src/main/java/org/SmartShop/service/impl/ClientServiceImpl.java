package org.SmartShop.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.SmartShop.dto.client.ClientOrderHistoryDTO;
import org.SmartShop.dto.client.ClientResponseDTO;
import org.SmartShop.dto.client.ClientRequestDTO;
import org.SmartShop.repository.ClientRepository;
import org.SmartShop.repository.OrderRepository;
import org.SmartShop.repository.UserRepository;
import org.SmartShop.entity.enums.CustomerTier;
import org.springframework.stereotype.Service;
import org.SmartShop.entity.enums.UserRole;
import org.SmartShop.service.ClientService;
import org.SmartShop.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.User;
import java.math.BigDecimal;
import java.util.List;

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
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (clientRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(UserRole.CLIENT)
                .build();
        user = userRepository.save(user);

        Client client = clientMapper.toEntity(dto);
        client.setLinkedAccount(user);
        client.setTier(CustomerTier.BASIC);
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

        client.setName(dto.getName());
        client.setEmail(dto.getEmail());

        if (client.getLinkedAccount() != null) {
            client.getLinkedAccount().setUsername(dto.getUsername());
            client.getLinkedAccount().setPassword(dto.getPassword());
        }

        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public void deleteClient(Long id) {
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
        return orderRepository.findByClientId(id).stream()
                .map(clientMapper::toHistoryDto)
                .collect(Collectors.toList());
    }

}