package com.bibliotheque.app.repositories.bibliographie;

import com.bibliotheque.app.models.bibliographie.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {} 