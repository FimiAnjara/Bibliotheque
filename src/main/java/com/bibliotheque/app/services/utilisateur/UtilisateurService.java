package com.bibliotheque.app.services.utilisateur;

import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.repositories.utilisateur.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> findAll() { return utilisateurRepository.findAll(); }
    public Utilisateur findById(Long id) { return utilisateurRepository.findById(id).orElse(null); }
    public Utilisateur save(Utilisateur utilisateur) { return utilisateurRepository.save(utilisateur); }
    public void deleteById(Long id) { utilisateurRepository.deleteById(id); }
} 