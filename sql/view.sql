CREATE OR REPLACE VIEW exemplaire_statut_aujourdhui AS
SELECT
    e.id AS exemplaire_id,
    e.reference,
    l.titre AS livre,
    CASE
        WHEN EXISTS (
            SELECT 1
            FROM pret p
            WHERE p.exemplaire_id = e.id
              AND p.date_retour_effectuer IS NULL
        )
        THEN 'EMPRUNTE'
        ELSE 'DISPONIBLE'
    END AS statut_aujourdhui
FROM exemplaire e
LEFT JOIN livre l ON e.livre_id = l.id;