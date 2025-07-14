package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.models.pret.Pret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProlongementPretRepository extends JpaRepository<ProlongementPret, Long> {
    List<ProlongementPret> findByPretOrderByDateProlongementDesc(Pret pret);
} 