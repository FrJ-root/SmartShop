package org.SmartShop.service;

import org.SmartShop.dto.product.ProductRequestDTO;
import org.SmartShop.dto.product.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO dto);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto);
    void deleteProduct(Long id);
    Page<ProductResponseDTO> getAllProducts(String search, Pageable pageable);
    ProductResponseDTO getProductById(Long id);
}