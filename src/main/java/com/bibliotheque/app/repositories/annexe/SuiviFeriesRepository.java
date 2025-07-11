package com.bibliotheque.app.repositories.annexe;

import com.bibliotheque.app.models.annexe.SuiviFeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuiviFeriesRepository extends JpaRepository<SuiviFeries, Long> {} 