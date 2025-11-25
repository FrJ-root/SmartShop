package org.SmartShop.service.impl;

import org.SmartShop.dto.*;

import org.SmartShop.entity.enums.CustomerTier;
import org.SmartShop.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public ClientDto createClient(CreateClientRequest request) {
        return ClientDto.builder()
                .id(1L)
                .nom(request.getNom())
                .email(request.getEmail())
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(0.0)
                .build();
    }

    @Override
    public ClientDto getClientById(Long id) {
        return ClientDto.builder()
                .id(id)
                .nom("Test Client")
                .email("test@example.com")
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                .totalSpent(0.0)
                .build();
    }

    @Override
    public Page<ClientDto> getAllClients(Pageable pageable) {
        List<ClientDto> clients = new ArrayList<>();
        return new PageImpl<>(clients, pageable, 0);
    }

    @Override
    public ClientDto updateClient(Long id, UpdateClientRequest request) {
        return getClientById(id);
    }

    @Override
    public void deleteClient(Long id) {
        // Do nothing for now
    }

    @Override
    public List<OrderHistoryDto> getClientOrderHistory(Long clientId) {
        return new ArrayList<>();
    }

    @Override
    public ClientDto updateClientStatistics(Long clientId) {
        return getClientById(clientId);
    }

    @Override
    public Page<ClientDto> searchClients(String searchTerm, Pageable pageable) {
        List<ClientDto> clients = new ArrayList<>();
        return new PageImpl<>(clients, pageable, 0);
    }
}