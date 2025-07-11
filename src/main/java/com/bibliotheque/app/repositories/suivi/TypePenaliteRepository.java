package com.bibliotheque.app.repositories.suivi;

import com.bibliotheque.app.models.suivi.TypePenalite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePenaliteRepository extends JpaRepository<TypePenalite, Long> {} 