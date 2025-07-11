package com.bibliotheque.app.repositories.utilisateur;

import com.bibliotheque.app.models.utilisateur.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {} 