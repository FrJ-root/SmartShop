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
    private final ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable Long id, HttpSession session) {
        checkAccess(id, session); // Security check
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequestDTO dto, HttpSession session) {
        checkAccess(id, session);
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<ClientOrderHistoryDTO>> getOrderHistory(@PathVariable Long id, HttpSession session) {
        checkAccess(id, session);
        return ResponseEntity.ok(clientService.getClientOrderHistory(id));
    }

    private void checkAccess(Long requestedClientId, HttpSession session) {
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        Long sessionUserId = (Long) session.getAttribute("USER_ID");

        if (role == UserRole.ADMIN) return;

        Client client = clientRepository.findById(requestedClientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (!client.getLinkedAccount().getId().equals(sessionUserId)) {
            throw new RuntimeException("Access Denied: You can only view your own data");
        }
    }
}