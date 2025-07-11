package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.repositories.pret.ProlongementPretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProlongementPretService {
    @Autowired
    private ProlongementPretRepository prolongementPretRepository;

    public List<ProlongementPret> findAll() { return prolongementPretRepository.findAll(); }
    public Optional<ProlongementPret> findById(Long id) { return prolongementPretRepository.findById(id); }
    public ProlongementPret save(ProlongementPret prolongementPret) { return prolongementPretRepository.save(prolongementPret); }
    public void deleteById(Long id) { prolongementPretRepository.deleteById(id); }
} 