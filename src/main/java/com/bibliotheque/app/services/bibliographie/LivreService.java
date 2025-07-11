package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Livre;
import com.bibliotheque.app.repositories.bibliographie.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivreService {
    @Autowired
    private LivreRepository livreRepository;

    public List<Livre> findAll() { return livreRepository.findAll(); }
    public Optional<Livre> findById(Long id) { return livreRepository.findById(id); }
    public Livre save(Livre livre) { return livreRepository.save(livre); }
    public void deleteById(Long id) { livreRepository.deleteById(id); }
} 