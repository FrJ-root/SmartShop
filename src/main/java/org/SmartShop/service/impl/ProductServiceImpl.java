package org.SmartShop.service.impl;

import lombok.RequiredArgsConstructor;
import org.SmartShop.dto.product.ProductRequestDTO;
import org.SmartShop.dto.product.ProductResponseDTO;
import org.SmartShop.entity.Product;
import org.SmartShop.mapper.ProductMapper;
import org.SmartShop.repository.OrderItemRepository;
import org.SmartShop.repository.ProductRepository;
import org.SmartShop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        product.setDeleted(false);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setUnitPriceHT(dto.getUnitPriceHT());
        product.setStockAvailable(dto.getStockAvailable());

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Rule: Soft delete if used in orders, Hard delete otherwise [cite: 63]
        boolean isUsedInOrders = orderItemRepository.existsByProductId(id);

        if (isUsedInOrders) {
            product.setDeleted(true);
            productRepository.save(product); // Soft Delete
        } else {
            productRepository.delete(product); // Hard Delete
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(String search, Pageable pageable) {
        Page<Product> page;

        if (search != null && !search.isBlank()) {
            page = productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(search, pageable);
        } else {
            page = productRepository.findByDeletedFalse(pageable);
        }

        return page.map(productMapper::toDto);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.isDeleted()) {
            throw new RuntimeException("Product not found (deleted)");
        }

        return productMapper.toDto(product);
    }
}