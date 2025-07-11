package com.bibliotheque.app.services.bibliographie;

import com.bibliotheque.app.models.bibliographie.Editeur;
import com.bibliotheque.app.repositories.bibliographie.EditeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EditeurService {
    @Autowired
    private EditeurRepository editeurRepository;

    public List<Editeur> findAll() { return editeurRepository.findAll(); }
    public Optional<Editeur> findById(Long id) { return editeurRepository.findById(id); }
    public Editeur save(Editeur editeur) { return editeurRepository.save(editeur); }
    public void deleteById(Long id) { editeurRepository.deleteById(id); }
} 