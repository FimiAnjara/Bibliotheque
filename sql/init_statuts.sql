-- Script d'initialisation des statuts d'exemplaires
-- Ce script ajoute un statut "Disponible" par défaut pour tous les exemplaires qui n'en ont pas

-- Vérifier si des statuts existent déjà
DO $$
BEGIN
    -- Si aucun statut n'existe, créer des statuts par défaut pour tous les exemplaires
    IF NOT EXISTS (SELECT 1 FROM statut_exemplaire LIMIT 1) THEN
        INSERT INTO statut_exemplaire (exemplaire_id, statut, date_changement, admin_id, notes)
        SELECT 
            e.id,
            1, -- Statut DISPONIBLE
            CURRENT_TIMESTAMP,
            4, -- Admin ID (David Lemoine)
            'Statut initial - Exemplaire disponible'
        FROM exemplaire e;
        
        RAISE NOTICE 'Statuts par défaut créés pour % exemplaires', (SELECT COUNT(*) FROM exemplaire);
    ELSE
        RAISE NOTICE 'Des statuts existent déjà, aucune action nécessaire';
    END IF;
END $$; 

