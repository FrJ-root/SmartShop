package org.SmartShop.repository;

import org.SmartShop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find active products with name filter (case insensitive) and pagination
    Page<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);

    // Find all active products with pagination (when no filter applied)
    Page<Product> findByDeletedFalse(Pageable pageable);
}