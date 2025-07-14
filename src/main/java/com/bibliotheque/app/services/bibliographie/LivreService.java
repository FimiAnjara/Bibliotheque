package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Livre;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.repositories.bibliographie.LivreRepository;
import com.bibliotheque.app.repositories.bibliographie.ExemplaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivreService {
    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private ExemplaireService exemplaireService;
    
    @Autowired
    private ExemplaireRepository exemplaireRepository;

    public List<Livre> findAll() { return livreRepository.findAll(); }
    public Livre findById(Long id) { return livreRepository.findById(id).orElse(null); }
    public Livre save(Livre livre) { return livreRepository.save(livre); }
    public void deleteById(Long id) { livreRepository.deleteById(id); }
    
    public List<Livre> searchLivres(String search) {
        String searchLower = search.toLowerCase();
        return livreRepository.findAll().stream()
            .filter(livre -> 
                (livre.getTitre() != null && livre.getTitre().toLowerCase().contains(searchLower)) ||
                (livre.getIsbn() != null && livre.getIsbn().toLowerCase().contains(searchLower)) ||
                (livre.getAuteur() != null && livre.getAuteur().getNom() != null && 
                 livre.getAuteur().getNom().toLowerCase().contains(searchLower)) ||
                (livre.getAuteur() != null && livre.getAuteur().getPrenom() != null && 
                 livre.getAuteur().getPrenom().toLowerCase().contains(searchLower))
            )
            .collect(Collectors.toList());
    }
    
    public List<Livre> findByAuteur(String auteur) {
        String auteurLower = auteur.toLowerCase();
        return livreRepository.findAll().stream()
            .filter(livre -> livre.getAuteur() != null && 
                ((livre.getAuteur().getNom() != null && 
                  livre.getAuteur().getNom().toLowerCase().contains(auteurLower)) ||
                 (livre.getAuteur().getPrenom() != null && 
                  livre.getAuteur().getPrenom().toLowerCase().contains(auteurLower))))
            .collect(Collectors.toList());
    }
    
    public List<Livre> findByCategorie(String categorie) {
        return livreRepository.findAll();
    }
    
    public int getNombreExemplairesDisponibles(Livre livre) {
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivre(livre);
        if (exemplaires == null || exemplaires.isEmpty()) {
            return 0;
        }
        
        return (int) exemplaires.stream()
            .filter(exemplaire -> {
                return exemplaireService.getCurrentStatut(exemplaire).getCode() == 1;
            })
            .count();
    }

    public int getNombreTotalExemplaires(Livre livre) {
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivre(livre);
        return exemplaires != null ? exemplaires.size() : 0;
    }
} 