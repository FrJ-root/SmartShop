package org.SmartShop.service;

import org.SmartShop.dto.client.ClientOrderHistoryDTO;
import org.SmartShop.dto.client.ClientRequestDTO;
import org.SmartShop.dto.client.ClientResponseDTO;
import java.util.List;

public interface ClientService {
    ClientResponseDTO createClient(ClientRequestDTO dto);
    ClientResponseDTO getClientById(Long id);
    ClientResponseDTO updateClient(Long id, ClientRequestDTO dto);
    void deleteClient(Long id);
    List<ClientOrderHistoryDTO> getClientOrderHistory(Long id);
}