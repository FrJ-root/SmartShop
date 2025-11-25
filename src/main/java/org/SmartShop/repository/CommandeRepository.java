package org.SmartShop.repository;

import org.SmartShop.entity.Client;
import org.SmartShop.entity.Commande;
import org.SmartShop.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByClientAndStatus(Client client, OrderStatus status);

    List<Commande> findByClientOrderByDateDesc(Client client);

    List<Commande> findByClient(Client client);

}