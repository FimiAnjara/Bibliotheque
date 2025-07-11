package com.bibliotheque.app.repositories.suivi;

import com.bibliotheque.app.models.suivi.StatutExemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutExemplaireRepository extends JpaRepository<StatutExemplaire, Long> {} 