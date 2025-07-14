package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.repositories.pret.ReservationRepository;
import com.bibliotheque.app.repositories.pret.ValidationRepository;
import com.bibliotheque.app.services.gestion.ConfigurationQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ValidationRepository validationRepository;
    
    @Autowired
    private ConfigurationQuotaService configurationQuotaService;

    public List<Reservation> findAll() { return reservationRepository.findAll(); }
    public Reservation findById(Long id) { return reservationRepository.findById(id).orElse(null); }
    public Reservation save(Reservation reservation) { return reservationRepository.save(reservation); }
    public void deleteById(Long id) { reservationRepository.deleteById(id); }
    
    public List<Reservation> findByAdherent(Adherent adherent) {
        return reservationRepository.findByAdherent(adherent);
    }
    
    public List<Reservation> findByExemplaire(Exemplaire exemplaire) {
        return reservationRepository.findByExemplaire(exemplaire);
    }
    

    public List<Reservation> findAllReservationsActives() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<Reservation> toutesReservations = reservationRepository.findAll();
        
        return toutesReservations.stream()
            .filter(reservation -> {
                if (reservation.getDateExpiration().isBefore(maintenant)) {
                    return false;
                }
                
                List<Validation> validations = validationRepository.findByReservation(reservation);
                
                return validations.isEmpty() || validations.stream().noneMatch(Validation::getValidationStatus);
            })
            .toList();
    }
    

    public int countReservationsActives(Adherent adherent) {
        LocalDateTime maintenant = LocalDateTime.now();
        
        List<Reservation> reservations = reservationRepository.findByAdherent(adherent);
        
        int count = 0;
        for (Reservation reservation : reservations) {
            if (reservation.getDateExpiration().isAfter(maintenant)) {
                List<Validation> validations = validationRepository.findByReservation(reservation);
                
                if (validations.isEmpty() || validations.stream().noneMatch(Validation::getValidationStatus)) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    public boolean peutReserver(Adherent adherent) {
        ConfigurationQuota config = configurationQuotaService.findByProfil(adherent.getProfil());
        
        if (config == null) {
            return false;
        }
        
        int quotaMax = config.getQuotaReservation();
        int reservationsActives = countReservationsActives(adherent);
        
        return reservationsActives < quotaMax;
    }
    
    public int getQuotaReservation(Adherent adherent) {
        ConfigurationQuota config = configurationQuotaService.findByProfil(adherent.getProfil());
        if (config != null) {
            return config.getQuotaReservation();
        }
        return 0;
    }

    public int getReservationsActives(Adherent adherent) {
        return countReservationsActives(adherent);
    }
    
    public Reservation createReservation(Adherent adherent, Exemplaire exemplaire, LocalDateTime dateSouhaiter) {
        Reservation reservation = new Reservation();
        reservation.setAdherent(adherent);
        reservation.setExemplaire(exemplaire);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setDateSouhaiter(dateSouhaiter);
        reservation.setDateExpiration(LocalDateTime.now().plusDays(7));
        
        return reservationRepository.save(reservation);
    }
    
} 