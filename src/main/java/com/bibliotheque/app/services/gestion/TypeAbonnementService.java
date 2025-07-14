package com.bibliotheque.app.services.gestion;

import com.bibliotheque.app.models.gestion.TypeAbonnement;
import com.bibliotheque.app.repositories.gestion.TypeAbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeAbonnementService {
    @Autowired
    private TypeAbonnementRepository typeAbonnementRepository;

    public List<TypeAbonnement> findAll() { return typeAbonnementRepository.findAll(); }
    public TypeAbonnement findById(Long id) { return typeAbonnementRepository.findById(id).orElse(null); }
    public TypeAbonnement save(TypeAbonnement typeAbonnement) { return typeAbonnementRepository.save(typeAbonnement); }
    public void deleteById(Long id) { typeAbonnementRepository.deleteById(id); }
} 