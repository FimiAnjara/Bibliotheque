package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.TypePenalite;
import com.bibliotheque.app.repositories.suivi.TypePenaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypePenaliteService {
    @Autowired
    private TypePenaliteRepository typePenaliteRepository;

    public List<TypePenalite> findAll() { return typePenaliteRepository.findAll(); }
    public Optional<TypePenalite> findById(Long id) { return typePenaliteRepository.findById(id); }
    public TypePenalite save(TypePenalite typePenalite) { return typePenaliteRepository.save(typePenalite); }
    public void deleteById(Long id) { typePenaliteRepository.deleteById(id); }
} 