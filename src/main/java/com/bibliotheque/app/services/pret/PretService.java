package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.repositories.pret.PretRepository;
import com.bibliotheque.app.services.gestion.ConfigurationQuotaService;
import com.bibliotheque.app.services.suivi.PenaliteService;
import com.bibliotheque.app.models.suivi.Penalite;
import com.bibliotheque.app.services.gestion.AbonnementService;
import com.bibliotheque.app.models.gestion.Abonnement;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.config.BibliothequeConfig;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PretService {
    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private ConfigurationQuotaService configurationQuotaService;

    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private AbonnementService abonnementService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private BibliothequeConfig bibliothequeConfig;

    public List<Pret> findAll() { return pretRepository.findAll(); }
    public Optional<Pret> findById(Long id) { return pretRepository.findById(id); }
    public Pret save(Pret pret) { return pretRepository.save(pret); }
    public void deleteById(Long id) { pretRepository.deleteById(id); }
    
    @Transactional
    public Pret saveWithChecks(Pret pret) throws Exception {
        // Vérifier pénalité active
        List<Penalite> penalites = pret.getAdherent().getPenalites();
        java.time.LocalDate datePret = pret.getDatePret().toLocalDate();
        boolean hasActivePenalite = penalites.stream().anyMatch(p ->
            (p.getDateApplication() != null && p.getDateFin() != null &&
             !datePret.isBefore(p.getDateApplication()) && !datePret.isAfter(p.getDateFin()) && p.getDateAnnulation() == null)
        );
        if (hasActivePenalite) {
            throw new Exception("L'adhérent a une pénalité active à cette date.");
        }
        // Vérifier quota de prêt uniquement pour les prêts Domicile
        if (pret.getTypePret() == Pret.TypePret.Domicile) {
            List<Pret> pretsActifs = pretRepository.findByAdherentAndDateRetourEffectuerIsNull(pret.getAdherent());
            long pretsDomicileActifs = pretsActifs.stream()
                .filter(p -> p.getTypePret() == Pret.TypePret.Domicile)
                .count();
            ConfigurationQuota config = configurationQuotaService.findByProfil(pret.getAdherent().getProfil());
            if (config == null) {
                throw new Exception("Aucune configuration de quota trouvée pour ce profil.");
            }
            if (pretsDomicileActifs >= config.getQuotaPret()) {
                throw new Exception("Quota de prêt dépassé.");
            }
        }
        // Vérifier abonnement valide
        List<Abonnement> abonnements = pret.getAdherent().getAbonnements();
        boolean hasValidAbonnement = abonnements.stream().anyMatch(a ->
            a.getDateDebut() != null && a.getDateFin() != null &&
            !datePret.isBefore(a.getDateDebut()) && !datePret.isAfter(a.getDateFin())
        );
        if (!hasValidAbonnement) {
            throw new Exception("Aucun abonnement valide à la date du prêt.");
        }
        return pretRepository.save(pret);
    }
    
    public List<Pret> findByAdherentAndDateRetourEffectuerIsNull(Adherent adherent) {
        return pretRepository.findByAdherentAndDateRetourEffectuerIsNull(adherent);
    }
    
    public List<Pret> findByAdherentOrderByDatePretDesc(Adherent adherent) {
        return pretRepository.findByAdherentOrderByDatePretDesc(adherent);
    }

    public LocalDateTime getDateRetourPrevue(LocalDateTime datePret, Adherent adherent) {
        ConfigurationQuota config = configurationQuotaService.findByProfil(adherent.getProfil());
        return datePret.plusDays(config.getDureeMaxPret());
    } 

    public LocalDateTime getDateRetourPrevueSurPlace(LocalDateTime datePret) {
        return datePret.with(bibliothequeConfig.getHeureFermeture());
    }

    public List<Pret> findNonRendusEtValides() {
        List<Pret> tous = pretRepository.findAll();
        return tous.stream()
            .filter(pret -> pret.getDateRetourEffectuer() == null)
            .filter(pret -> validationService.findAll().stream()
                .anyMatch(v -> v.getPret() != null && v.getPret().getId().equals(pret.getId()) && Boolean.TRUE.equals(v.getValidationStatus()))
            )
            .toList();
    }

    public java.time.LocalDateTime getDateRetourPrevueEffective(Long pretId) {
        Optional<Pret> pretOpt = findById(pretId);
        if (pretOpt.isEmpty()) return null;
        Pret pret = pretOpt.get();
        java.util.List<Validation> validations = validationService.findAll();
        return validations.stream()
            .filter(v -> v.getProlongement() != null && v.getProlongement().getPret().getId().equals(pretId) && Boolean.TRUE.equals(v.getValidationStatus()))
            .sorted((v1, v2) -> v2.getDate().compareTo(v1.getDate()))
            .map(v -> v.getProlongement().getDateRetourPrevu())
            .findFirst()
            .orElse(pret.getDateRetourPrevu());
    }

    public List<Pret> findPretsRendus() {
        return pretRepository.findByDateRetourEffectuerIsNotNull();
    }
} 