-- SmartShop - Données de test

-- Nettoyage des données existantes (optionnel)
-- TRUNCATE TABLE paiement CASCADE;
-- TRUNCATE TABLE order_item CASCADE;
-- TRUNCATE TABLE commande CASCADE;
-- TRUNCATE TABLE app_user CASCADE;
-- TRUNCATE TABLE product CASCADE;
-- TRUNCATE TABLE client CASCADE;

-- Insertion de clients de test avec différents niveaux de fidélité
INSERT INTO client (nom, email, tier, total_orders, total_spent, first_order_date, last_order_date) VALUES
('Microsoft Maroc SARL', 'contact@microsoft-ma.com', 'PLATINUM', 25, 18500.00, '2024-01-15', '2024-11-20'),
('Dell Technologies Morocco', 'orders@dell.ma', 'GOLD', 12, 7800.50, '2024-03-10', '2024-11-18'),
('HP Maroc Distribution', 'procurement@hp.ma', 'SILVER', 8, 4200.75, '2024-05-20', '2024-11-15'),
('Lenovo Morocco', 'sales@lenovo.ma', 'SILVER', 5, 2800.00, '2024-06-05', '2024-11-10'),
('Apple Store Casablanca', 'orders@apple.ma', 'BASIC', 2, 950.00, '2024-10-01', '2024-11-05'),
('Acer Morocco', 'info@acer.ma', 'GOLD', 10, 6500.00, '2024-02-20', '2024-11-22'),
('Asus Morocco', 'sales@asus.ma', 'SILVER', 6, 3800.00, '2024-04-15', '2024-11-19'),
('Samsung Electronics Morocco', 'contact@samsung.ma', 'PLATINUM', 30, 22000.00, '2024-01-10', '2024-11-24'),
('Toshiba Morocco', 'orders@toshiba.ma', 'BASIC', 3, 1500.00, '2024-09-01', '2024-11-08'),
('Sony Morocco', 'procurement@sony.ma', 'GOLD', 14, 9200.00, '2024-02-28', '2024-11-21');

-- Insertion de produits de test
INSERT INTO product (nom, prixht, stock, deleted) VALUES
-- Ordinateurs portables
('Ordinateur Portable Dell Latitude 5520', 8500.00, 15, false),
('MacBook Pro 13 pouces M2', 12000.00, 8, false),
('HP EliteBook 840 G9', 7200.00, 12, false),
('Lenovo ThinkPad X1 Carbon Gen 10', 9800.00, 10, false),
('Acer Aspire 5', 4500.00, 20, false),
('Asus ZenBook 14', 6800.00, 14, false),
('Samsung Galaxy Book Pro', 7500.00, 9, false),
('Microsoft Surface Laptop 5', 11000.00, 7, false),

-- Périphériques
('Écran Dell UltraSharp 27 pouces 4K', 3200.00, 25, false),
('Clavier Logitech MX Keys', 850.00, 30, false),
('Souris Logitech MX Master 3', 650.00, 35, false),
('Webcam Logitech C920 HD Pro', 420.00, 20, false),
('Casque Sony WH-1000XM5', 2800.00, 15, false),
('Hub USB-C Anker 7-en-1', 550.00, 40, false),

-- Composants
('Disque SSD Samsung 1TB', 1200.00, 40, false),
('Mémoire RAM DDR4 32GB Corsair', 2800.00, 18, false),
('Disque SSD Kingston 512GB', 680.00, 50, false),
('Carte Graphique NVIDIA RTX 3060', 4500.00, 6, false),

-- Accessoires
('Sacoche Laptop Targus 15.6 pouces', 320.00, 60, false),
('Tapis de Souris RGB', 180.00, 80, false),
('Support Laptop Réglable', 250.00, 45, false);

-- Insertion d'utilisateurs de test (pour l'authentification future)
INSERT INTO app_user (username, password, role, client_id) VALUES
('admin', 'admin123', 'ADMIN', NULL),
('manager', 'manager123', 'ADMIN', NULL),
('microsoft.ma', 'pass123', 'CLIENT', 1),
('dell.ma', 'pass123', 'CLIENT', 2),
('hp.ma', 'pass123', 'CLIENT', 3),
('lenovo.ma', 'pass123', 'CLIENT', 4),
('apple.ma', 'pass123', 'CLIENT', 5),
('acer.ma', 'pass123', 'CLIENT', 6),
('asus.ma', 'pass123', 'CLIENT', 7),
('samsung.ma', 'pass123', 'CLIENT', 8),
('toshiba.ma', 'pass123', 'CLIENT', 9),
('sony.ma', 'pass123', 'CLIENT', 10);

-- Insertion de commandes de test
INSERT INTO commande (client_id, created_at, sub_total_ht, discount_amount, ht_after_discount, tax_amount, total_ttc, amount_remaining, promo_code, status) VALUES
-- Commandes pour Microsoft Maroc (client PLATINUM - 15% de réduction)
(1, '2024-11-20 10:00:00', 10000.00, 1500.00, 8500.00, 1700.00, 10200.00, 0.00, 'PROMO-2024', 'CONFIRMED'),
(1, '2024-11-15 14:30:00', 5000.00, 750.00, 4250.00, 850.00, 5100.00, 0.00, NULL, 'CONFIRMED'),
(1, '2024-11-10 09:15:00', 3000.00, 450.00, 2550.00, 510.00, 3060.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour Dell Technologies (client GOLD - 10% de réduction)
(2, '2024-11-18 11:20:00', 4500.00, 450.00, 4050.00, 810.00, 4860.00, 0.00, NULL, 'CONFIRMED'),
(2, '2024-11-12 15:45:00', 2800.00, 280.00, 2520.00, 504.00, 3024.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour HP Maroc (client SILVER - 5% de réduction)
(3, '2024-11-15 10:10:00', 2200.00, 110.00, 2090.00, 418.00, 2508.00, 0.00, NULL, 'CONFIRMED'),
(3, '2024-11-08 13:25:00', 1500.00, 75.00, 1425.00, 285.00, 1710.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour Samsung (client PLATINUM - 15% de réduction)
(8, '2024-11-24 16:00:00', 8000.00, 1200.00, 6800.00, 1360.00, 8160.00, 0.00, NULL, 'CONFIRMED'),
(8, '2024-11-22 11:30:00', 6500.00, 975.00, 5525.00, 1105.00, 6630.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour Sony (client GOLD - 10% de réduction)
(10, '2024-11-21 14:20:00', 5200.00, 520.00, 4680.00, 936.00, 5616.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour Acer (client GOLD - 10% de réduction)
(6, '2024-11-22 09:45:00', 3800.00, 380.00, 3420.00, 684.00, 4104.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes pour Asus (client SILVER - 5% de réduction)
(7, '2024-11-19 13:10:00', 2900.00, 145.00, 2755.00, 551.00, 3306.00, 0.00, NULL, 'CONFIRMED'),

-- Commandes en cours (PENDING)
(4, '2024-11-25 08:00:00', 3200.00, 0.00, 3200.00, 640.00, 3840.00, 1840.00, NULL, 'PENDING'),
(5, '2024-11-25 09:30:00', 1200.00, 0.00, 1200.00, 240.00, 1440.00, 1440.00, NULL, 'PENDING'),
(9, '2024-11-25 10:15:00', 2500.00, 0.00, 2500.00, 500.00, 3000.00, 3000.00, NULL, 'PENDING');

-- Insertion d'articles de commandes (OrderItem)
INSERT INTO order_item (product_id, quantity, unit_price, line_total, commande_id) VALUES
-- Articles pour la commande 1 (Microsoft)
(1, 1, 8500.00, 8500.00, 1),
(9, 1, 3200.00, 3200.00, 1),

-- Articles pour la commande 2 (Microsoft)
(2, 1, 12000.00, 12000.00, 2),

-- Articles pour la commande 3 (Microsoft) 
(3, 1, 7200.00, 7200.00, 3),

-- Articles pour la commande 4 (Dell)
(4, 1, 9800.00, 9800.00, 4),

-- Articles pour la commande 5 (Dell)
(10, 2, 850.00, 1700.00, 5),
(11, 2, 650.00, 1300.00, 5),

-- Articles pour la commande 6 (HP)
(12, 5, 420.00, 2100.00, 6),

-- Articles pour la commande 7 (HP)
(15, 1, 1200.00, 1200.00, 7),

-- Articles pour la commande 8 (Samsung)
(7, 1, 7500.00, 7500.00, 8),
(13, 1, 2800.00, 2800.00, 8),

-- Articles pour la commande 9 (Samsung)
(8, 1, 11000.00, 11000.00, 9),

-- Articles pour la commande 10 (Sony)
(13, 2, 2800.00, 5600.00, 10),

-- Articles pour la commande 11 (Acer)
(5, 1, 4500.00, 4500.00, 11),

-- Articles pour la commande 12 (Asus)
(6, 1, 6800.00, 6800.00, 12),

-- Articles pour la commande 13 (Lenovo - en cours)
(9, 1, 3200.00, 3200.00, 13),

-- Articles pour la commande 14 (Apple - en cours)
(15, 1, 1200.00, 1200.00, 14),

-- Articles pour la commande 15 (Toshiba - en cours)
(16, 1, 2800.00, 2800.00, 15);

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
(8, 1, 8160.00, 'ENCAISSE', '2024-11-24', '2024-11-24', 'VIR-2024-11-24-001', 'Bank of Africa'),
(9, 1, 6630.00, 'ENCAISSE', '2024-11-22', '2024-11-23', 'CHQ-456789', 'Banque Populaire'),
(10, 1, 5616.00, 'ENCAISSE', '2024-11-21', '2024-11-21', 'RECU-006', NULL),
(11, 1, 4104.00, 'ENCAISSE', '2024-11-22', '2024-11-22', 'VIR-2024-11-22-003', 'Crédit Agricole'),
(12, 1, 3306.00, 'ENCAISSE', '2024-11-19', '2024-11-20', 'CHQ-321654', 'CDM'),

-- Paiements partiels pour les commandes en cours
(13, 1, 2000.00, 'ENCAISSE', '2024-11-25', '2024-11-25', 'RECU-007', NULL),
-- Commandes 14 et 15 n'ont pas encore de paiement