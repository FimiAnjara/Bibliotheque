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
