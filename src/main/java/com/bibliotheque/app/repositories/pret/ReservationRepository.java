package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAdherent(Adherent adherent);
    List<Reservation> findByExemplaire(Exemplaire exemplaire);
} 