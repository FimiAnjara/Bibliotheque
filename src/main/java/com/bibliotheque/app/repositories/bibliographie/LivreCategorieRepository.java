package com.bibliotheque.app.repositories.bibliographie;

import com.bibliotheque.app.models.bibliographie.LivreCategorie;
import com.bibliotheque.app.models.bibliographie.LivreCategorieId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreCategorieRepository extends JpaRepository<LivreCategorie, LivreCategorieId> {} 