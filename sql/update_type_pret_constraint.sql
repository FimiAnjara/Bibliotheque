-- Script pour mettre à jour la contrainte type_pret
-- À exécuter si la base de données existe déjà

-- Supprimer l'ancienne contrainte
ALTER TABLE pret DROP CONSTRAINT IF EXISTS pret_type_pret_check;

-- Ajouter la nouvelle contrainte avec les valeurs en minuscule
ALTER TABLE pret ADD CONSTRAINT pret_type_pret_check CHECK (type_pret IN ('domicile', 'sur place'));

-- Mettre à jour les données existantes si nécessaire
UPDATE pret SET type_pret = LOWER(type_pret) WHERE type_pret IN ('Domicile', 'Sur place'); 