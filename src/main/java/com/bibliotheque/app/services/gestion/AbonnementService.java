package com.bibliotheque.app.services.gestion;

import com.bibliotheque.app.models.gestion.Abonnement;
import com.bibliotheque.app.repositories.gestion.AbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbonnementService {
    @Autowired
    private AbonnementRepository abonnementRepository;

    public List<Abonnement> findAll() { return abonnementRepository.findAll(); }
    public Abonnement findById(Long id) { return abonnementRepository.findById(id).orElse(null); }
    public Abonnement save(Abonnement abonnement) { return abonnementRepository.save(abonnement); }
    public void deleteById(Long id) { abonnementRepository.deleteById(id); }
} 