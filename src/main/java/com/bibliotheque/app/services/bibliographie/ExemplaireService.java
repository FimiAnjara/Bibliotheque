package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.repositories.bibliographie.ExemplaireRepository;
import com.bibliotheque.app.services.suivi.StatutExemplaireService;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExemplaireService {
    @Autowired
    private ExemplaireRepository exemplaireRepository;
    
    @Autowired
    private StatutExemplaireService statutExemplaireService;

    public List<Exemplaire> findAll() { 
        return exemplaireRepository.findAll(); 
    }
    
    public Optional<Exemplaire> findById(Long id) { 
        return exemplaireRepository.findById(id); 
    }
    
    public Exemplaire save(Exemplaire exemplaire) { 
        return exemplaireRepository.save(exemplaire); 
    }
    
    public void deleteById(Long id) { 
        exemplaireRepository.deleteById(id); 
    }

    
    public List<Exemplaire> findAllWithCurrentStatut() {
        List<Exemplaire> exemplaires = findAll();
        exemplaires.forEach(exemplaire -> {
            StatutExemplaire.Statut currentStatut = statutExemplaireService.getCurrentStatut(exemplaire);
            
        });
        return exemplaires;
    }

    public List<Exemplaire> findDispoExemplaires(List<Exemplaire> exemplaires){
        exemplaires = exemplaires.stream()
                .filter(ex -> statutExemplaireService.getCurrentStatut(ex).getCode() == 1)
                .collect(Collectors.toList());
        return exemplaires;
    }
    
    public StatutExemplaire.Statut getCurrentStatut(Exemplaire exemplaire) {
        return statutExemplaireService.getCurrentStatut(exemplaire);
    }
    
    /**
     * Sauvegarde un exemplaire et crée automatiquement un statut "Disponible"
     * @param exemplaire L'exemplaire à sauvegarder
     * @param personnel Le personnel qui ajoute l'exemplaire
     * @return L'exemplaire sauvegardé
     */
    public Exemplaire saveWithDefaultStatus(Exemplaire exemplaire, Personnel personnel) {
        // Sauvegarder l'exemplaire
        Exemplaire savedExemplaire = save(exemplaire);
        
        // Créer automatiquement un statut "Disponible"
        statutExemplaireService.changeStatut(
            savedExemplaire,
            StatutExemplaire.Statut.DISPONIBLE,
            personnel,
            "Statut initial - Exemplaire nouvellement ajouté"
        );
        
        return savedExemplaire;
    }
} 