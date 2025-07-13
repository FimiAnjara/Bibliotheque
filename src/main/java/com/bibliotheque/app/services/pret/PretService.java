package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.repositories.pret.PretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PretService {
    @Autowired
    private PretRepository pretRepository;

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
} 