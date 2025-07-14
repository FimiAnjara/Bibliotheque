package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.repositories.suivi.StatutExemplaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatutExemplaireService {
    @Autowired
    private StatutExemplaireRepository statutExemplaireRepository;

    public List<StatutExemplaire> findAll() { 
        return statutExemplaireRepository.findAll(); 
    }
    
    public StatutExemplaire findById(Long id) { 
        return statutExemplaireRepository.findById(id).orElse(null); 
    }
    
    public StatutExemplaire save(StatutExemplaire statutExemplaire) { 
        return statutExemplaireRepository.save(statutExemplaire); 
    }
    
    public void deleteById(Long id) { 
        statutExemplaireRepository.deleteById(id); 
    }
    
    public List<StatutExemplaire> findByExemplaire(Exemplaire exemplaire) {
        return statutExemplaireRepository.findByExemplaireOrderByDateChangementDesc(exemplaire);
    }
    
    public StatutExemplaire findCurrentStatutByExemplaire(Exemplaire exemplaire) {
        return statutExemplaireRepository.findCurrentStatutByExemplaire(exemplaire).orElse(null);
    }
    
    public StatutExemplaire changeStatut(Exemplaire exemplaire, StatutExemplaire.Statut nouveauStatut, 
                                       Personnel admin, String notes) {
        StatutExemplaire statutExemplaire = new StatutExemplaire();
        statutExemplaire.setExemplaire(exemplaire);
        statutExemplaire.setStatutEnum(nouveauStatut);
        statutExemplaire.setAdmin(admin);
        statutExemplaire.setNotes(notes);
        statutExemplaire.setDateChangement(LocalDateTime.now());
        
        return statutExemplaireRepository.save(statutExemplaire);
    }
    
    public StatutExemplaire.Statut getCurrentStatut(Exemplaire exemplaire) {
        StatutExemplaire currentStatut = findCurrentStatutByExemplaire(exemplaire);
        return currentStatut != null ? currentStatut.getStatutEnum() : StatutExemplaire.Statut.DISPONIBLE;
    }
} 