package org.SmartShop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.client.ClientOrderHistoryDTO;
import org.SmartShop.dto.client.ClientRequestDTO;
import org.SmartShop.dto.client.ClientResponseDTO;
import org.SmartShop.entity.Client;
import org.SmartShop.entity.enums.UserRole;
import org.SmartShop.repository.ClientRepository;
import org.SmartShop.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository; // Needed for ownership check

    // Create Client (Admin Only - usually)
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(dto));
    }

    // Get Client Details
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable Long id, HttpSession session) {
        checkAccess(id, session); // Security check
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    // Update Client
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequestDTO dto, HttpSession session) {
        checkAccess(id, session);
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

    // Delete Client (Admin Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        // Note: Interceptor usually blocks Clients from DELETE, but double check logic if needed
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // Get Order History [cite: 23]
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<ClientOrderHistoryDTO>> getOrderHistory(@PathVariable Long id, HttpSession session) {
        checkAccess(id, session);
        return ResponseEntity.ok(clientService.getClientOrderHistory(id));
    }

    // --- Helper for Ownership Security ---
    private void checkAccess(Long requestedClientId, HttpSession session) {
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        Long sessionUserId = (Long) session.getAttribute("USER_ID");

        if (role == UserRole.ADMIN) return; // Admin can see all

        // If Client, they must own this profile
        Client client = clientRepository.findById(requestedClientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!client.getLinkedAccount().getId().equals(sessionUserId)) {
            throw new RuntimeException("Access Denied: You can only view your own data");
            // Global Exception Handler will map this to 403 or 401
        }
    }
}