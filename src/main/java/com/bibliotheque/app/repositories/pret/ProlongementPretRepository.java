package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.ProlongementPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProlongementPretRepository extends JpaRepository<ProlongementPret, Long> {} 