package org.SmartShop.service;

import org. SmartShop.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClientService {

    ClientDto createClient(CreateClientRequest request);

    ClientDto getClientById(Long id);

    Page<ClientDto> getAllClients(Pageable pageable);

    ClientDto updateClient(Long id, UpdateClientRequest request);

    void deleteClient(Long id);

    List<OrderHistoryDto> getClientOrderHistory(Long clientId);

    ClientDto updateClientStatistics(Long clientId);

    Page<ClientDto> searchClients(String searchTerm, Pageable pageable);
}