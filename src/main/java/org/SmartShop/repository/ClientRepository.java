package org.SmartShop.repository;

import org.SmartShop.entity.Client;
import org.SmartShop.entity.enums.CustomerTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Client> findByTier(CustomerTier tier);

    Page<Client> findByNomContainingOrEmailContaining(String nom, String email, Pageable pageable);
    
    Page<Client> findByNomContainingIgnoreCaseOrEmailContainingIgnoreCase(String nom, String email, Pageable pageable);

}