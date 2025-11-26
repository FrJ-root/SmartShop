package org.SmartShop.controller;

import org.SmartShop.dto. client.*; // Changed from org.SmartShop.dto
import org.SmartShop. dto.order. OrderHistoryDto;
import org.SmartShop.service.ClientService;
import org. springframework.beans.factory.annotation. Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain. Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework. web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody CreateClientRequest request) {
        ClientDto createdClient = clientService.createClient(request);
        return ResponseEntity.ok(createdClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> getAllClients(Pageable pageable) {
        Page<ClientDto> clients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequest request) {
        ClientDto updatedClient = clientService.updateClient(id, request);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderHistoryDto>> getClientOrderHistory(@PathVariable Long id) {
        List<OrderHistoryDto> orderHistory = clientService.getClientOrderHistory(id);
        return ResponseEntity.ok(orderHistory);
    }

    @PutMapping("/{id}/statistics")
    public ResponseEntity<ClientDto> updateClientStatistics(@PathVariable Long id) {
        ClientDto updatedClient = clientService.updateClientStatistics(id);
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClientDto>> searchClients(
            @RequestParam String term,
            Pageable pageable) {
        Page<ClientDto> clients = clientService.searchClients(term, pageable);
        return ResponseEntity.ok(clients);
    }
}