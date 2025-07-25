package com.bibliotheque.app.repositories.gestion;

import com.bibliotheque.app.models.gestion.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {} 