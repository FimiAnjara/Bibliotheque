package com.bibliotheque.app.repositories.utilisateur;

import com.bibliotheque.app.models.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    java.util.Optional<Utilisateur> findByEmail(String email);
} 