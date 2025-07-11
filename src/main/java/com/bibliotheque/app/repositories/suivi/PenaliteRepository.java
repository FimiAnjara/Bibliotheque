package com.bibliotheque.app.repositories.suivi;

import com.bibliotheque.app.models.suivi.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaliteRepository extends JpaRepository<Penalite, Long> {} 