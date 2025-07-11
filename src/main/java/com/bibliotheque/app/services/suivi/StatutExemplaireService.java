package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.repositories.suivi.StatutExemplaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutExemplaireService {
    @Autowired
    private StatutExemplaireRepository statutExemplaireRepository;

    public List<StatutExemplaire> findAll() { return statutExemplaireRepository.findAll(); }
    public Optional<StatutExemplaire> findById(Long id) { return statutExemplaireRepository.findById(id); }
    public StatutExemplaire save(StatutExemplaire statutExemplaire) { return statutExemplaireRepository.save(statutExemplaire); }
    public void deleteById(Long id) { statutExemplaireRepository.deleteById(id); }
} 