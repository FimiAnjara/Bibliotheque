package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.repositories.suivi.StatutExemplaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StatutExemplaireService {
    @Autowired
    private StatutExemplaireRepository statutExemplaireRepository;

    public List<StatutExemplaire> findAll() { 
        return statutExemplaireRepository.findAll(); 
    }
    
    public Optional<StatutExemplaire> findById(Long id) { 
        return statutExemplaireRepository.findById(id); 
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
    
    public Optional<StatutExemplaire> findCurrentStatutByExemplaire(Exemplaire exemplaire) {
        return statutExemplaireRepository.findCurrentStatutByExemplaire(exemplaire);
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
        Optional<StatutExemplaire> currentStatut = findCurrentStatutByExemplaire(exemplaire);
        return currentStatut.map(StatutExemplaire::getStatutEnum)
                           .orElse(StatutExemplaire.Statut.DISPONIBLE);
    }
} 