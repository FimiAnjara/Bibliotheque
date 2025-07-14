package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.repositories.pret.PretRepository;
import com.bibliotheque.app.repositories.pret.ProlongementPretRepository;
import com.bibliotheque.app.repositories.pret.ValidationRepository;
import com.bibliotheque.app.repositories.utilisateur.UtilisateurRepository;
import com.bibliotheque.app.services.gestion.ConfigurationQuotaService;
import com.bibliotheque.app.services.suivi.NotificationService;
import com.bibliotheque.app.models.suivi.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.utilisateur.Utilisateur;

@Service
public class ReservationValidationService {

    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ProlongementPretRepository prolongementPretRepository;

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private ConfigurationQuotaService configurationQuotaService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PretService pretService;

    @Autowired
    UtilisateurRepository utilisateurRepository;


    public Boolean canValidateReservation(Reservation reservation) {
        
        if (hasDateConflicts(reservation)) {
            return false;
        }
        
        if (!checkPretQuota(reservation.getAdherent())) {
            return false;
        }
        
        return true;
    }

    private boolean hasDateConflicts(Reservation reservation) {
        LocalDateTime dateSouhaiter = reservation.getDateSouhaiter();
        LocalDateTime dateRetourPrevue = calculateDateRetourPrevue(reservation);
        
        List<Pret> pretsExemplaire = pretRepository.findByExemplaire(reservation.getExemplaire());
        
        for (Pret pret : pretsExemplaire) {
            if (pret.getDateRetourEffectuer() != null) {
                continue;
            }
            
            LocalDateTime dateDebutPret = pret.getDatePret();
            LocalDateTime dateFinPret = getEffectiveDateRetourPrevue(pret);
            
            if (dateSouhaiter.isBefore(dateFinPret) && dateRetourPrevue.isAfter(dateDebutPret)) {
                return true;
            }
        }
        
        return false;
    }

    private LocalDateTime getEffectiveDateRetourPrevue(Pret pret) {
        List<ProlongementPret> prolongements = prolongementPretRepository.findByPretOrderByDateProlongementDesc(pret);
        
        if (!prolongements.isEmpty()) {
            return prolongements.get(0).getDateRetourPrevu();
        }
        
        return pret.getDateRetourPrevu();
    }

    private LocalDateTime calculateDateRetourPrevue(Reservation reservation) {
        ConfigurationQuota config = configurationQuotaService.findByProfil(reservation.getAdherent().getProfil());
        
        return reservation.getDateSouhaiter().plusDays(config.getDureeMaxPret());
    }

    private boolean checkPretQuota(Adherent adherent) {
        List<Pret> pretsActifs = pretRepository.findByAdherentAndDateRetourEffectuerIsNull(adherent);
        int pretsActifsCount = pretsActifs.size();
        
        ConfigurationQuota config = configurationQuotaService.findByProfil(adherent.getProfil());
        
        if (config == null) {
            return false;
        }
        
        int quotaMax = config.getQuotaPret();
        return pretsActifsCount < quotaMax;
    }

    public Pret validateReservation(Reservation reservation, Personnel personnel) {
        Pret pret = new Pret();
        pret.setAdherent(reservation.getAdherent());
        pret.setExemplaire(reservation.getExemplaire());
        pret.setDatePret(reservation.getDateSouhaiter());
        pret.setDateRetourPrevu(pretService.getDateRetourPrevue(reservation.getDateSouhaiter(), reservation.getAdherent()));
        pret.setTypePret(Pret.TypePret.Domicile);
        pret.setNotes("Prêt créé à partir de la réservation #" + reservation.getId());
        
        pret = pretRepository.save(pret);
        
        Validation validation = new Validation();
        validation.setReservation(reservation);
        validation.setPret(pret);
        validation.setValidationStatus(true);
        validation.setDate(LocalDateTime.now());
        validation.setAdmin(personnel);
        
        validationRepository.save(validation);      
        
        createNotificationForAdherent(pret, reservation);
        
        return pret;
    }
    
    /**
     * Crée une notification pour informer l'adhérent que sa réservation a été validée
     */
    private void createNotificationForAdherent(Pret pret, Reservation reservation) {
        Notification notification = new Notification();
        Utilisateur utilisateur = utilisateurRepository.findById(reservation.getAdherent().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé pour l'adhérent " + reservation.getAdherent().getId()));
        notification.setUtilisateur(utilisateur);
        notification.setPret(pret);
        notification.setReservation(reservation);
        notification.setDateCreation(LocalDateTime.now());
        notification.setEstLu(false);
        
        // Créer un message détaillé
        String message = String.format(
            "Votre réservation pour le livre \"%s\" (exemplaire %s) a été validée et transformée en prêt. " +
            "Prêt #%d - Date de prêt : %s - Date de retour prévue : %s. " +
            "Merci de respecter la date de retour pour éviter des pénalités.",
            pret.getExemplaire().getLivre().getTitre(),
            pret.getExemplaire().getReference(),
            pret.getId(),
            pret.getDatePret().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm")),
            pret.getDateRetourPrevu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"))
        );
        
        notification.setMessage(message);
        notificationService.save(notification);
    }
} 