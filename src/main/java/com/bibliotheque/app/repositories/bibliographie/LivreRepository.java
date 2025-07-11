package com.bibliotheque.app.repositories.bibliographie;

import com.bibliotheque.app.models.bibliographie.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {} 