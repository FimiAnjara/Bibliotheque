package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Auteur;
import com.bibliotheque.app.repositories.bibliographie.AuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuteurService {
    @Autowired
    private AuteurRepository auteurRepository;

    public List<Auteur> findAll() { return auteurRepository.findAll(); }
    public Optional<Auteur> findById(Long id) { return auteurRepository.findById(id); }
    public Auteur save(Auteur auteur) { return auteurRepository.save(auteur); }
    public void deleteById(Long id) { auteurRepository.deleteById(id); }
} 