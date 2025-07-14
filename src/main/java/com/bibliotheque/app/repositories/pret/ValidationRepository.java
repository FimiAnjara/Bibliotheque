package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.pret.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {
    List<Validation> findByReservation(Reservation reservation);
} 