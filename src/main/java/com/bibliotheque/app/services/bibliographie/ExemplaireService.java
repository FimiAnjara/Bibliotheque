package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.repositories.bibliographie.ExemplaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExemplaireService {
    @Autowired
    private ExemplaireRepository exemplaireRepository;

    public List<Exemplaire> findAll() { return exemplaireRepository.findAll(); }
    public Optional<Exemplaire> findById(Long id) { return exemplaireRepository.findById(id); }
    public Exemplaire save(Exemplaire exemplaire) { return exemplaireRepository.save(exemplaire); }
    public void deleteById(Long id) { exemplaireRepository.deleteById(id); }
} 