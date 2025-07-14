-- Active: 1736646695640@@127.0.0.1@5432@bibliotheque_webdyn
-- Profils
INSERT INTO profil (id, nom, quota_max, duree_max_pret) VALUES
  (1, 'etudiant', 5, 30),
  (2, 'professeur', 10, 60),
  (3, 'particulier', 3, 15);

-- Utilisateurs (3 adherents, 1 personnel)
INSERT INTO utilisateur (id, nom, prenom, date_naissance, email, address, password, telephone) VALUES
  (1, 'Martin', 'Alice', '2000-05-10', 'alice.martin@email.com', '1 rue des Fleurs', 'password1', '0600000001'),
  (2, 'Durand', 'Bob', '1998-11-22', 'bob.durand@email.com', '2 avenue des Champs', 'password2', '0600000002'),
  (3, 'Petit', 'Claire', '1985-03-15', 'claire.petit@email.com', '3 boulevard du Midi', 'password3', '0600000003'),
  (4, 'Lemoine', 'David', '1975-07-30', 'david.lemoine@email.com', '4 place de la Gare', 'adminpass', '0600000004');

-- Adherents (liés aux utilisateurs 1, 2, 3)
INSERT INTO adherent (id, numero_adherent, date_inscription, profil_id) VALUES
  (1, 'A1001', '2023-09-01 10:00:00', 1), -- Alice, etudiant
  (2, 'A1002', '2022-10-15 14:30:00', 2), -- Bob, professeur
  (3, 'A1003', '2024-01-20 09:15:00', 3); -- Claire, particulier

-- Personnel (lié à l'utilisateur 4)
INSERT INTO personnel (id, date_embauche, matricule, status) VALUES
  (4, '2020-05-15 08:00:00', 'P001', 'Bibliothécaire');


INSERT INTO auteur (nom, prenom, date_naissance, date_deces, nationalite) VALUES
('Rowling', 'J.K.', '1965-07-31', NULL, 'Britannique'),
('Martin', 'George R.R.', '1948-09-20', NULL, 'Américain'),
('Tolkien', 'J.R.R.', '1892-01-03', '1973-09-02', 'Britannique'),
('Orwell', 'George', '1903-06-25', '1950-01-21', 'Britannique'),
('Hugo', 'Victor', '1802-02-26', '1885-05-22', 'Français'),
('Christie', 'Agatha', '1890-09-15', '1976-01-12', 'Britannique'),
('Austen', 'Jane', '1775-12-16', '1817-07-18', 'Britannique'),
('Dumas', 'Alexandre', '1802-07-24', '1870-12-05', 'Français'),
('Zola', 'Émile', '1840-04-02', '1902-09-29', 'Français'),
('Verne', 'Jules', '1828-02-08', '1905-03-24', 'Français');

INSERT INTO editeur (nom, pays, annee_fondation) VALUES
('Gallimard', 'France', 1911),
('Hachette', 'France', 1826),
('Flammarion', 'France', 1876),
('Éditions du Seuil', 'France', 1935),
('Penguin Books', 'Royaume-Uni', 1935),
('HarperCollins', 'États-Unis', 1989),
('Bloomsbury', 'Royaume-Uni', 1986),
('Bantam Books', 'États-Unis', 1945),
('Allen & Unwin', 'Australie', 1936),
('Éditions Robert Laffont', 'France', 1941);

INSERT INTO categorie (nom) VALUES
('Fantasy'),
('Science-Fiction'),
('Policier'),
('Romance'),
('Historique'),
('Biographie'),
('Aventure'),
('Horreur'),
('Thriller'),
('Classique');

INSERT INTO livre (titre, isbn, annee_publication, nombre_pages, limit_age, editeur_id, auteur_id, resume) VALUES
('Harry Potter à l''école des sorciers', '9782070643028', 1997, 320, 10, 1, 1, 'Un jeune sorcier découvre son héritage magique et entre à Poudlard.'),
('Le Trône de Fer', '9782253147531', 1996, 694, 16, 2, 2, 'Dans un monde médiéval fantastique, des familles nobles se disputent le pouvoir.'),
('Le Seigneur des Anneaux', '9782266282361', 1954, 1216, 12, 3, 3, 'Une quête épique pour détruire un anneau maléfique.'),
('1984', '9782070368228', 1949, 328, 16, 1, 4, 'Une dystopie sur un régime totalitaire omniprésent.'),
('Les Misérables', '9782253004889', 1862, 1488, 12, 4, 5, 'L''histoire de Jean Valjean, un ancien forçat en quête de rédemption.'),
('Orgueil et Préjugés', '9782253006333', 1813, 432, 12, 6, 7, 'L''histoire d''amour entre Elizabeth Bennet et Mr. Darcy.');

INSERT INTO exemplaire (reference, date_acquisition, livre_id) VALUES
-- 3 exemplaires de Harry Potter
('HP1-001', '2020-01-15', 1),
('HP1-002', '2020-01-15', 1),
('HP1-003', '2021-03-10', 1),
-- 2 exemplaires du Trône de Fer
('TDF1-001', '2019-11-20', 2),
('TDF1-002', '2020-05-12', 2),
-- 2 exemplaires du Seigneur des Anneaux
('SDA1-001', '2018-09-05', 3),
('SDA1-002', '2020-02-28', 3),
-- 1 exemplaire de 1984
('1984-001', '2021-01-10', 4),
-- 2 exemplaires des Misérables
('LM1-001', '2017-06-18', 5),
('LM1-002', '2020-04-22', 5),
-- 1 exemplaire d'Orgueil et Préjugés
('OP1-001', '2020-07-14', 6);

-- Statuts d'exemplaires (données de test)
INSERT INTO statut_exemplaire (exemplaire_id, statut, date_changement, admin_id, notes) VALUES
-- HP1-001 : Disponible
(1, 1, '2024-01-01 10:00:00', 4, 'Exemplaire disponible'),
-- HP1-002 : Emprunté
(2, 2, '2024-01-15 14:30:00', 4, 'Exemplaire emprunté par un étudiant'),
-- HP1-003 : Disponible
(3, 1, '2024-01-01 10:00:00', 4, 'Exemplaire disponible'),
-- TDF1-001 : En réparation
(4, 4, '2024-01-10 09:15:00', 4, 'Exemplaire en réparation - page détachée'),
-- TDF1-002 : Disponible
(5, 1, '2024-01-01 10:00:00', 4, 'Exemplaire disponible'),
-- SDA1-001 : Réservé
(6, 3, '2024-01-20 16:45:00', 4, 'Exemplaire réservé pour un cours'),
-- SDA1-002 : Disponible
(7, 1, '2024-01-01 10:00:00', 4, 'Exemplaire disponible'),
-- 1984-001 : Perdu
(8, 5, '2024-01-05 11:20:00', 4, 'Exemplaire signalé comme perdu'),
-- LM1-001 : Disponible
(9, 1, '2024-01-01 10:00:00', 4, 'Exemplaire disponible'),
-- LM1-002 : Hors service
(10, 6, '2024-01-12 13:30:00', 4, 'Exemplaire hors service - état dégradé'),
-- OP1-001 : Disponible
(11, 1, '2024-01-01 10:00:00', 4, 'Exemplaire disponible');

-- Configurations de quota par profil
INSERT INTO configuration_quota (libelle, profil_id, duree_max_pret, quota_pret, quota_reservation, date_configuration) VALUES
('Configuration Étudiant', 1, 30, 5, 3, '2024-01-01 10:00:00'),
('Configuration Professeur', 2, 60, 10, 5, '2024-01-01 10:00:00'),
('Configuration Particulier', 3, 15, 3, 2, '2024-01-01 10:00:00');

-- Réservations de test
INSERT INTO reservation (date_reservation, date_expiration, date_souhaiter, adherent_id, exemplaire_id) VALUES
-- Alice (étudiante) a 2 réservations actives sur 3 autorisées
('2024-01-15 10:00:00', '2024-01-22 10:00:00', '2024-01-20 14:00:00', 1, 1),
('2024-01-16 14:30:00', '2024-01-23 14:30:00', '2024-01-21 16:00:00', 1, 3),
-- Bob (professeur) a 1 réservation active sur 5 autorisées
('2024-01-17 09:15:00', '2024-01-24 09:15:00', '2024-01-22 10:00:00', 2, 5),
-- Claire (particulière) a 0 réservation active sur 2 autorisées
-- (pas de réservation pour tester le quota disponible)

-- Validations de test (pour tester les réservations validées/annulées)
INSERT INTO validation (reservation_id, date, validation_status, admin_id) VALUES
-- Une réservation validée (ne doit pas apparaître dans les réservations actives)
(1, '2024-01-18 10:00:00', true, 4),
-- Une réservation annulée (ne doit pas apparaître dans les réservations actives)
(2, '2024-01-19 14:00:00', false, 4);


ALTER TABLE notification ALTER COLUMN message TYPE TEXT;
SELECT * FROM notification;
-- Prêts de test (pour les notifications)
INSERT INTO pret (date_pret, date_retour_prevu, adherent_id, exemplaire_id, type_pret, notes) VALUES
('2024-01-20 14:00:00', '2024-02-19 14:00:00', 1, 1, 'Domicile', 'Prêt créé à partir de la réservation #1');

-- Notifications de test
INSERT INTO notification (utilisateur_id, message, date_creation, est_lu, pret_id, reservation_id) VALUES
-- Notification pour Alice (étudiante) - réservation validée
(1, 'Votre réservation pour le livre "Harry Potter à l''école des sorciers" (exemplaire HP1-001) a été validée et transformée en prêt. Prêt #1 - Date de prêt : 20/01/2024 à 14:00 - Date de retour prévue : 19/02/2024 à 14:00. Merci de respecter la date de retour pour éviter des pénalités.', '2024-01-18 10:00:00', false, 1, 1),
-- Notification pour Bob (professeur) - réservation annulée
(2, 'Votre réservation pour le livre "Le Trône de Fer" (exemplaire TDF1-002) a été annulée par le personnel de la bibliothèque.', '2024-01-19 14:00:00', false, NULL, 2),
-- Notification lue pour Alice
(1, 'Rappel : Votre livre "Harry Potter à l''école des sorciers" doit être retourné avant le 19/02/2024.', '2024-01-20 09:00:00', true, 1, NULL);

