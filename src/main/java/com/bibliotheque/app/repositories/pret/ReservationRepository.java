package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {} 