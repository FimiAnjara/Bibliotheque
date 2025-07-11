package com.bibliotheque.app.repositories.gestion;

import com.bibliotheque.app.models.gestion.TypeAbonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAbonnementRepository extends JpaRepository<TypeAbonnement, Long> {} 