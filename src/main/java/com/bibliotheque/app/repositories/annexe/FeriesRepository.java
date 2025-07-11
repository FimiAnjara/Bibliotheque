package com.bibliotheque.app.repositories.annexe;

import com.bibliotheque.app.models.annexe.Feries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeriesRepository extends JpaRepository<Feries, Long> {} 