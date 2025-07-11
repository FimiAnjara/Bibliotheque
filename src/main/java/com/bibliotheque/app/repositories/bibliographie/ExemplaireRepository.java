package com.bibliotheque.app.repositories.bibliographie;

import com.bibliotheque.app.models.bibliographie.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {} 