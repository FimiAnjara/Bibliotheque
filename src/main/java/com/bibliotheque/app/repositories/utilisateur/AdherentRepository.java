package com.bibliotheque.app.repositories.utilisateur;

import com.bibliotheque.app.models.utilisateur.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, Long> {} 