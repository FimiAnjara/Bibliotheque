package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Categorie;
import com.bibliotheque.app.repositories.bibliographie.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {
    @Autowired
    private CategorieRepository categorieRepository;

    public List<Categorie> findAll() { return categorieRepository.findAll(); }
    public Categorie findById(Long id) { return categorieRepository.findById(id).orElse(null); }
    public Categorie save(Categorie categorie) { return categorieRepository.save(categorie); }
    public void deleteById(Long id) { categorieRepository.deleteById(id); }
} 