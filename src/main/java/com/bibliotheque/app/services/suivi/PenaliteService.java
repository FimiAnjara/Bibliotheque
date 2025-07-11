package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.Penalite;
import com.bibliotheque.app.repositories.suivi.PenaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PenaliteService {
    @Autowired
    private PenaliteRepository penaliteRepository;

    public List<Penalite> findAll() { return penaliteRepository.findAll(); }
    public Optional<Penalite> findById(Long id) { return penaliteRepository.findById(id); }
    public Penalite save(Penalite penalite) { return penaliteRepository.save(penalite); }
    public void deleteById(Long id) { penaliteRepository.deleteById(id); }
} 