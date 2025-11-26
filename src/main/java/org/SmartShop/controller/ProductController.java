package org.SmartShop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.product.ProductRequestDTO;
import org.SmartShop.dto.product.ProductResponseDTO;
import org.SmartShop.entity.enums.UserRole;
import org.SmartShop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Create (Admin Only)
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO dto, HttpSession session) {
        checkAdminAccess(session);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    // Update (Admin Only)
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO dto, HttpSession session) {
        checkAdminAccess(session);
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // Delete (Admin Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpSession session) {
        checkAdminAccess(session);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Get All with Filters & Pagination (Public/Client/Admin)
    // Example URL: /api/products?page=0&size=10&search=Laptop
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return ResponseEntity.ok(productService.getAllProducts(search, pageable));
    }

    // Helper for Admin check
    private void checkAdminAccess(HttpSession session) {
        UserRole role = (UserRole) session.getAttribute("USER_ROLE");
        if (role != UserRole.ADMIN) {
            throw new RuntimeException("Access Denied: Admin role required");
        }
    }
}