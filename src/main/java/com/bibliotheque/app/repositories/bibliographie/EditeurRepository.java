package com.bibliotheque.app.repositories.bibliographie;

import com.bibliotheque.app.models.bibliographie.Editeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditeurRepository extends JpaRepository<Editeur, Long> {} 