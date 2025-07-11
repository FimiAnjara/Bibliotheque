package com.bibliotheque.app.services.gestion;

import com.bibliotheque.app.models.gestion.Reparation;
import com.bibliotheque.app.repositories.gestion.ReparationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReparationService {
    @Autowired
    private ReparationRepository reparationRepository;

    public List<Reparation> findAll() { return reparationRepository.findAll(); }
    public Optional<Reparation> findById(Long id) { return reparationRepository.findById(id); }
    public Reparation save(Reparation reparation) { return reparationRepository.save(reparation); }
    public void deleteById(Long id) { reparationRepository.deleteById(id); }
} 