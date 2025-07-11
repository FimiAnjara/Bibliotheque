package com.bibliotheque.app.repositories.suivi;

import com.bibliotheque.app.models.suivi.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Long> {} 