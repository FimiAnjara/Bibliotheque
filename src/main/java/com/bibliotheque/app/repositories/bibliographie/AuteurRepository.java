package com.bibliotheque.app.repositories.bibliographie;

import com.bibliotheque.app.models.bibliographie.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Long> {} 