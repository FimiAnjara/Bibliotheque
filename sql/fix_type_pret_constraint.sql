-- Script pour corriger la contrainte type_pret
-- À exécuter dans votre base de données PostgreSQL

-- 1. Supprimer l'ancienne contrainte
ALTER TABLE pret DROP CONSTRAINT IF EXISTS pret_type_pret_check;

-- 2. Mettre à jour les données existantes pour utiliser les bonnes valeurs
UPDATE pret SET type_pret = 'domicile' WHERE type_pret = 'Domicile';
UPDATE pret SET type_pret = 'sur place' WHERE type_pret = 'Sur place';

UPDATE pret SET type_pret = 'sur place' WHERE type_pret = 'sur_place';

-- 3. Ajouter la nouvelle contrainte avec les valeurs correctes
ALTER TABLE pret ADD CONSTRAINT pret_type_pret_check CHECK (type_pret IN ('Domicile', 'sur place'));

-- 4. Vérifier que tout est correct
SELECT DISTINCT type_pret FROM pret; 