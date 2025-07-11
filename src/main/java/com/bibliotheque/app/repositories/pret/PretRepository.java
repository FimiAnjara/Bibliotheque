package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PretRepository extends JpaRepository<Pret, Long> {} 