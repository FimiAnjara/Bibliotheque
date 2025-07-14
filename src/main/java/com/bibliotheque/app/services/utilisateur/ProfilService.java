package com.bibliotheque.app.services.utilisateur;

import com.bibliotheque.app.models.utilisateur.Profil;
import com.bibliotheque.app.repositories.utilisateur.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfilService {
    @Autowired
    private ProfilRepository profilRepository;

    public List<Profil> findAll() { return profilRepository.findAll(); }
    public Profil findById(Long id) { return profilRepository.findById(id).orElse(null); }
    public Profil save(Profil profil) { return profilRepository.save(profil); }
    public void deleteById(Long id) { profilRepository.deleteById(id); }
} 