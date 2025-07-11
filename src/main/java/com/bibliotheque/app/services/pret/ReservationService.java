package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.repositories.pret.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> findAll() { return reservationRepository.findAll(); }
    public Optional<Reservation> findById(Long id) { return reservationRepository.findById(id); }
    public Reservation save(Reservation reservation) { return reservationRepository.save(reservation); }
    public void deleteById(Long id) { reservationRepository.deleteById(id); }
} 