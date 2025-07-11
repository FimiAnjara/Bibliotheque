package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.Statut;
import com.bibliotheque.app.repositories.suivi.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutService {
    @Autowired
    private StatutRepository statutRepository;

    public List<Statut> findAll() { return statutRepository.findAll(); }
    public Optional<Statut> findById(Long id) { return statutRepository.findById(id); }
    public Statut save(Statut statut) { return statutRepository.save(statut); }
    public void deleteById(Long id) { statutRepository.deleteById(id); }
} 