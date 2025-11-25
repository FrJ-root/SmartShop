-- SmartShop - Données de test

-- Insertion de clients de test avec différents niveaux de fidélité
INSERT INTO client (nom, email, tier, total_orders, total_spent, first_order_date, last_order_date) VALUES
('MicroSoft Maroc SARL', 'contact@microsoft-ma.com', 'PLATINUM', 25, 18500.00, '2024-01-15', '2024-11-20'),
('Dell Technologies Morocco', 'orders@dell.ma', 'GOLD', 12, 7800.50, '2024-03-10', '2024-11-18'),
('HP Maroc Distribution', 'procurement@hp.ma', 'SILVER', 8, 4200.75, '2024-05-20', '2024-11-15'),
('Lenovo Morocco', 'sales@lenovo.ma', 'SILVER', 5, 2800.00, '2024-06-05', '2024-11-10'),
('Apple Store Casablanca', 'orders@apple.ma', 'BASIC', 2, 950.00, '2024-10-01', '2024-11-05');

-- Insertion de produits de test
INSERT INTO product (nom, prixht, stock, deleted) VALUES
('Ordinateur Portable Dell Latitude 5520', 8500.00, 15, false),
('MacBook Pro 13 pouces M2', 12000.00, 8, false),
('HP EliteBook 840 G9', 7200.00, 12, false),
('Lenovo ThinkPad X1 Carbon Gen 10', 9800.00, 10, false),
('Écran Dell UltraSharp 27 pouces 4K', 3200.00, 25, false),
('Clavier Logitech MX Keys', 850.00, 30, false),
('Souris Logitech MX Master 3', 650.00, 35, false),
('Webcam Logitech C920 HD Pro', 420.00, 20, false),
('Disque SSD Samsung 1TB', 1200.00, 40, false),
('Mémoire RAM DDR4 32GB Corsair', 2800.00, 18, false);

-- Insertion d'utilisateurs de test (pour l'authentification future)
INSERT INTO app_user (username, password, role, client_id) VALUES
('admin', 'admin123', 'ADMIN', NULL),
('microsoft.ma', 'password123', 'CLIENT', 1),
('dell.ma', 'password123', 'CLIENT', 2),
('hp.ma', 'password123', 'CLIENT', 3),
('lenovo.ma', 'password123', 'CLIENT', 4),
('apple.ma', 'password123', 'CLIENT', 5);

-- Insertion de commandes de test
INSERT INTO commande (client_id, date, sous_total, remise, montantht, tva, totalttc, montant_restant, code_promo, status) VALUES
-- Commandes pour Microsoft Maroc (client PLATINUM)
(1, '2024-11-20', 10000.00, 1500.00, 8500.00, 1700.00, 10200.00, 0.00, 'PROMO-2024', 'CONFIRMED'),
(1, '2024-11-15', 5000.00, 750.00, 4250.00, 850.00, 5100.00, 0.00, NULL, 'CONFIRMED'),
(1, '2024-11-10', 3000.00, 450.00, 2550.00, 510.00, 3060.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour Dell Technologies (client GOLD)
(2, '2024-11-18', 4500.00, 450.00, 4050.00, 810.00, 4860.00, 0.00, NULL, 'CONFIRMED'),
(2, '2024-11-12', 2800.00, 280.00, 2520.00, 504.00, 3024.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour HP Maroc (client SILVER)
(3, '2024-11-15', 2200.00, 110.00, 2090.00, 418.00, 2508.00, 0.00, NULL, 'CONFIRMED'),
(3, '2024-11-08', 1500.00, 75.00, 1425.00, 285.00, 1710.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes en cours
(4, '2024-11-25', 3200.00, 0.00, 3200.00, 640.00, 3840.00, 1840.00, NULL, 'PENDING'),
(5, '2024-11-25', 1200.00, 0.00, 1200.00, 240.00, 1440.00, 1440.00, NULL, 'PENDING');

-- Insertion d'articles de commandes (OrderItem)
INSERT INTO order_item (product_id, quantity, unit_price, line_total, commande_id) VALUES
-- Articles pour la commande 1 (Microsoft)
(1, 1, 8500.00, 8500.00, 1),
(5, 1, 3200.00, 3200.00, 1),

-- Articles pour la commande 2 (Microsoft)
(2, 1, 12000.00, 12000.00, 2),

-- Articles pour la commande 3 (Microsoft) 
(3, 1, 7200.00, 7200.00, 3),

-- Articles pour la commande 4 (Dell)
(4, 1, 9800.00, 9800.00, 4),

-- Articles pour la commande 5 (Dell)
(6, 2, 850.00, 1700.00, 5),
(7, 2, 650.00, 1300.00, 5),

-- Articles pour la commande 6 (HP)
(8, 5, 420.00, 2100.00, 6),

-- Articles pour la commande 7 (HP)
(9, 1, 1200.00, 1200.00, 7),

-- Articles pour la commande 8 (Lenovo - en cours)
(5, 1, 3200.00, 3200.00, 8),

-- Articles pour la commande 9 (Apple - en cours)
(9, 1, 1200.00, 1200.00, 9);

-- Insertion de paiements de test
INSERT INTO paiement (commande_id, numero_paiement, montant, status, date_paiement, date_encaissement, reference, banque) VALUES
-- Paiements pour les commandes confirmées (totalement payées)
(1, 1, 6000.00, 'ENCAISSE', '2024-11-20', '2024-11-20', 'RECU-001', NULL),
(1, 2, 4200.00, 'ENCAISSE', '2024-11-20', '2024-11-21', 'CHQ-123456', 'BMCE Bank'),
(2, 1, 5100.00, 'ENCAISSE', '2024-11-15', '2024-11-15', 'VIR-2024-11-15-001', 'Attijariwafa Bank'),
(3, 1, 3060.00, 'ENCAISSE', '2024-11-10', '2024-11-10', 'RECU-002', NULL),
(4, 1, 4860.00, 'ENCAISSE', '2024-11-18', '2024-11-19', 'VIR-2024-11-18-002', 'CIH Bank'),
(5, 1, 3024.00, 'ENCAISSE', '2024-11-12', '2024-11-12', 'RECU-003', NULL),
(6, 1, 2508.00, 'ENCAISSE', '2024-11-15', '2024-11-16', 'CHQ-789012', 'Société Générale'),
(7, 1, 1710.00, 'ENCAISSE', '2024-11-08', '2024-11-08', 'RECU-004', NULL),

-- Paiements partiels pour les commandes en cours
(8, 1, 2000.00, 'ENCAISSE', '2024-11-25', '2024-11-25', 'RECU-005', NULL);