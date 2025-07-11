package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.LivreCategorie;
import com.bibliotheque.app.models.bibliographie.LivreCategorieId;
import com.bibliotheque.app.repositories.bibliographie.LivreCategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivreCategorieService {
    @Autowired
    private LivreCategorieRepository livreCategorieRepository;

    public List<LivreCategorie> findAll() { return livreCategorieRepository.findAll(); }
    public Optional<LivreCategorie> findById(LivreCategorieId id) { return livreCategorieRepository.findById(id); }
    public LivreCategorie save(LivreCategorie livreCategorie) { return livreCategorieRepository.save(livreCategorie); }
    public void deleteById(LivreCategorieId id) { livreCategorieRepository.deleteById(id); }
} 