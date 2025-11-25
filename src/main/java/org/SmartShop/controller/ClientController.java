package org.SmartShop.controller;

import org.SmartShop.dto.*;
import org.SmartShop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
@Tag(name = "Client Management", description = "API for managing clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Client created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody CreateClientRequest request) {
        ClientDto createdClient = clientService.createClient(request);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieves a client by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client found"),
        @ApiResponse(responseCode = "404", description = "Client not found")
    })
    public ResponseEntity<ClientDto> getClientById(
            @Parameter(description = "Client ID") @PathVariable Long id) {
        ClientDto client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    /**
     * Lister tous les clients avec pagination
     */
    @GetMapping
    public ResponseEntity<Page<ClientDto>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ClientDto> clients = clientService.getAllClients(pageable);
        
        return ResponseEntity.ok(clients);
    }

    /**
     * Mettre à jour les informations d'un client
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClientRequest request) {
        
        ClientDto updatedClient = clientService.updateClient(id, request);
        return ResponseEntity.ok(updatedClient);
    }

    /**
     * Supprimer un client
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer l'historique des commandes d'un client
     */
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderHistoryDto>> getClientOrderHistory(@PathVariable Long id) {
        List<OrderHistoryDto> orderHistory = clientService.getClientOrderHistory(id);
        return ResponseEntity.ok(orderHistory);
    }

    /**
     * Mettre à jour les statistiques d'un client
     */
    @PostMapping("/{id}/update-statistics")
    public ResponseEntity<ClientDto> updateClientStatistics(@PathVariable Long id) {
        ClientDto updatedClient = clientService.updateClientStatistics(id);
        return ResponseEntity.ok(updatedClient);
    }

    /**
     * Rechercher des clients
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ClientDto>> searchClients(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nom"));
        Page<ClientDto> clients = clientService.searchClients(q, pageable);
        
        return ResponseEntity.ok(clients);
    }
}