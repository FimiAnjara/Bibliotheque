package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.repositories.pret.PretRepository;
import com.bibliotheque.app.services.gestion.ConfigurationQuotaService;

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

    public List<Pret> findAll() { return pretRepository.findAll(); }
    public Optional<Pret> findById(Long id) { return pretRepository.findById(id); }
    public Pret save(Pret pret) { return pretRepository.save(pret); }
    public void deleteById(Long id) { pretRepository.deleteById(id); }
    
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
} 