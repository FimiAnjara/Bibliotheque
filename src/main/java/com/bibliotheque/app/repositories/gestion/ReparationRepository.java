package com.bibliotheque.app.repositories.gestion;

import com.bibliotheque.app.models.gestion.Reparation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReparationRepository extends JpaRepository<Reparation, Long> {} 