-- Active: 1736646695640@@127.0.0.1@5432@bibliotheque_webdyn
-- ==========================================
-- TABLES DE BASE
-- ==========================================

CREATE TABLE utilisateur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(128) NOT NULL,
    prenom VARCHAR(128) NOT NULL,
    date_naissance DATE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address TEXT,
    password VARCHAR(255) NOT NULL,
    telephone VARCHAR(20)
);

CREATE TABLE profil (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(128) NOT NULL
);

-- ==========================================
-- TABLES UTILISATEURS
-- ==========================================

CREATE TABLE adherent (
    id INTEGER PRIMARY KEY,
    numero_adherent VARCHAR(120) UNIQUE NOT NULL,
    date_inscription TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profil_id INTEGER NOT NULL,
    CONSTRAINT fk_adherent_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id),
    CONSTRAINT fk_adherent_profil FOREIGN KEY (profil_id) REFERENCES profil(id)
);

CREATE TABLE personnel (
    id INTEGER PRIMARY KEY,
    date_embauche TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    matricule VARCHAR(120) UNIQUE NOT NULL,
    status VARCHAR(120) NOT NULL CHECK (status IN ('Bibliothécaire', 'Administrateur')),
    CONSTRAINT fk_personnel_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id)
);

-- ==========================================
-- TABLES BIBLIOGRAPHIQUES
-- ==========================================

CREATE TABLE auteur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    date_naissance DATE,
    date_deces DATE,
    nationalite VARCHAR(100)
);

CREATE TABLE editeur (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    pays VARCHAR(100),
    annee_fondation INTEGER
);

CREATE TABLE livre (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    isbn VARCHAR(120) UNIQUE,
    annee_publication INTEGER,
    nombre_pages INTEGER,
    limit_age INTEGER,
    editeur_id INTEGER,
    auteur_id INTEGER,
    resume VARCHAR(280),
    CONSTRAINT fk_livre_editeur FOREIGN KEY (editeur_id) REFERENCES editeur(id),
    CONSTRAINT fk_livre_auteur FOREIGN KEY (auteur_id) REFERENCES auteur(id)
);

CREATE TABLE categorie (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE livre_categorie (
    livre_id INTEGER NOT NULL,
    categorie_id INTEGER NOT NULL,
    PRIMARY KEY(livre_id,categorie_id),
    CONSTRAINT fk_lc_livre FOREIGN KEY (livre_id) REFERENCES livre(id),
    CONSTRAINT fk_lc_categorie FOREIGN KEY (categorie_id) REFERENCES categorie(id)
);

CREATE TABLE exemplaire (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(150) UNIQUE NOT NULL,
    date_acquisition DATE,
    livre_id INTEGER NOT NULL,
    CONSTRAINT fk_exemplaire_livre FOREIGN KEY (livre_id) REFERENCES livre(id)
);

-- ==========================================
-- TABLES DE GESTION
-- ==========================================

CREATE TABLE reparation (
    id SERIAL PRIMARY KEY,
    exemplaire_id INTEGER NOT NULL,
    date_debut DATE NOT NULL,
    date_fin_prevue DATE,
    date_fin_reelle DATE,
    description TEXT NOT NULL,
    cout DECIMAL(10,2),
    technicien VARCHAR(100),
    CONSTRAINT fk_reparation_exemplaire FOREIGN KEY (exemplaire_id) REFERENCES exemplaire(id),
    CHECK (date_fin_prevue IS NULL OR date_fin_prevue >= date_debut),
    CHECK (date_fin_reelle IS NULL OR date_fin_reelle >= date_debut)
);

CREATE TABLE configuration_quota (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    profil_id INTEGER NOT NULL,
    duree_max_pret INTEGER,
    quota_pret INTEGER NOT NULL CHECK (quota_pret >= 1),
    quota_reservation INTEGER NOT NULL CHECK (quota_reservation >= 1),
    date_configuration TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_config_quota_profil FOREIGN KEY (profil_id) REFERENCES profil(id)
);

CREATE TABLE type_abonnement (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    libelle VARCHAR(100) NOT NULL,
    duree_mois INTEGER NOT NULL CHECK (duree_mois > 0),
    montant DECIMAL(10,2) NOT NULL CHECK (montant >= 0),
    conditions TEXT
);

CREATE TABLE abonnement (
    id SERIAL PRIMARY KEY,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    date_paiement DATE NOT NULL,
    montant_paye DECIMAL(10,2) NOT NULL CHECK (montant_paye >= 0),
    adherent_id INTEGER NOT NULL,
    type_abonnement_id INTEGER NOT NULL,
    CONSTRAINT fk_abonnement_adherent FOREIGN KEY (adherent_id) REFERENCES adherent(id),
    CONSTRAINT fk_abonnement_type FOREIGN KEY (type_abonnement_id) REFERENCES type_abonnement(id),
    CHECK (date_fin > date_debut),
    CHECK (date_paiement <= date_debut)
);

CREATE TABLE pret (
    id SERIAL PRIMARY KEY,
    date_pret TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_retour_prevu TIMESTAMP NOT NULL,
    date_retour_effectuer TIMESTAMP,
    adherent_id INTEGER NOT NULL,
    exemplaire_id INTEGER NOT NULL,
    type_pret VARCHAR(50) NOT NULL CHECK (type_pret IN ('Domicile', 'sur place')),
    notes TEXT,
    CONSTRAINT fk_pret_adherent FOREIGN KEY (adherent_id) REFERENCES adherent(id),
    CONSTRAINT fk_pret_exemplaire FOREIGN KEY (exemplaire_id) REFERENCES exemplaire(id),
    CHECK (date_retour_prevu > date_pret)
);

CREATE TABLE prolongement_pret (
    id SERIAL PRIMARY KEY,
    pret_id INTEGER NOT NULL,
    date_retour_prevu TIMESTAMP NOT NULL,
    date_prolongement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    admin_id INTEGER,
    CONSTRAINT fk_prolongement_pret FOREIGN KEY (pret_id) REFERENCES pret(id),
    CONSTRAINT fk_prolongement_personnel FOREIGN KEY (admin_id) REFERENCES personnel(id)
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    date_reservation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    date_expiration TIMESTAMP NOT NULL,
    date_souhaiter TIMESTAMP NOT NULL,
    adherent_id INTEGER NOT NULL,
    exemplaire_id INTEGER NOT NULL,
    CONSTRAINT fk_reservation_adherent FOREIGN KEY (adherent_id) REFERENCES adherent(id),
    CONSTRAINT fk_reservation_exemplaire FOREIGN KEY (exemplaire_id) REFERENCES exemplaire(id),
    CHECK (date_expiration > date_reservation)
);

CREATE TABLE validation (
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER,
    pret_id INTEGER,
    prolongement_id INTEGER,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    validation_status BOOLEAN NOT NULL,
    admin_id INTEGER NOT NULL,
    CONSTRAINT fk_validation_pret FOREIGN KEY (pret_id) REFERENCES pret(id),
    CONSTRAINT fk_validation_prolongement FOREIGN KEY (prolongement_id) REFERENCES prolongement_pret(id),
    CONSTRAINT fk_validation_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id),
    CONSTRAINT fk_validation_personnel FOREIGN KEY (admin_id) REFERENCES personnel(id)
);


-- ==========================================
-- TABLES DE SUIVI
-- ==========================================

CREATE TABLE type_penalite (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    retard_jours INTEGER NOT NULL,
    duree_jours INTEGER NOT NULL
);

CREATE TABLE penalite (
    id SERIAL PRIMARY KEY,
    date_application DATE NOT NULL DEFAULT CURRENT_DATE,
    date_fin DATE NOT NULL,
    admin_id INTEGER NOT NULL,
    type_penalite_id INTEGER NOT NULL,
    adherent_id INTEGER,
    notes TEXT,
    date_annulation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_penalite_personnel FOREIGN KEY (admin_id) REFERENCES personnel(id),
    CONSTRAINT fk_penalite_type FOREIGN KEY (type_penalite_id) REFERENCES type_penalite(id),
    CONSTRAINT fk_penalite_adherent FOREIGN KEY (adherent_id) REFERENCES adherent(id),
    CHECK (date_fin >= date_application)
);

CREATE TABLE statut_exemplaire (
    id SERIAL PRIMARY KEY,
    exemplaire_id INTEGER NOT NULL,
    statut SMALLINT NOT NULL,
    date_changement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    admin_id INTEGER,
    pret_id INTEGER,
    reservation_id INTEGER,
    notes TEXT,
    CONSTRAINT fk_statut_exemplaire FOREIGN KEY (exemplaire_id) REFERENCES exemplaire(id),
    CONSTRAINT fk_statut_personnel FOREIGN KEY (admin_id) REFERENCES personnel(id),
    CONSTRAINT fk_statut_pret FOREIGN KEY (pret_id) REFERENCES pret(id),
    CONSTRAINT fk_statut_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    utilisateur_id INTEGER NOT NULL,
    message TEXT NOT NULL,
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    est_lu BOOLEAN DEFAULT FALSE,
    pret_id INTEGER,
    reservation_id INTEGER,
    CONSTRAINT fk_notification_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id),
    CONSTRAINT fk_notification_pret FOREIGN KEY (pret_id) REFERENCES pret(id),
    CONSTRAINT fk_notification_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);
ALTER Table notification ALTER COLUMN message TYPE TEXT;

-- ==========================================
-- TABLES ANNEXES
-- ==========================================

CREATE TYPE jour_mois AS (
    jour SMALLINT,
    mois SMALLINT
);

CREATE TABLE feries (
    id SERIAL PRIMARY KEY,
    nom_evenement VARCHAR(100) NOT NULL,
    date_debut jour_mois,
    duree_jours INTEGER NOT NULL CHECK (duree_jours > 0),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT
);

CREATE TABLE suivi_feries (
    id SERIAL PRIMARY KEY,
    ferie_id INTEGER NOT NULL,
    annee INTEGER NOT NULL,
    date_valide TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    CONSTRAINT fk_suivi_ferie FOREIGN KEY (ferie_id) REFERENCES feries(id)
);