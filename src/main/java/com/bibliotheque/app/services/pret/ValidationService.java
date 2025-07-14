package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.repositories.pret.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidationService {
    @Autowired
    private ValidationRepository validationRepository;

    public List<Validation> findAll() { return validationRepository.findAll(); }
    public Optional<Validation> findById(Long id) { return validationRepository.findById(id); }
    public Validation save(Validation validation) { return validationRepository.save(validation); }
    public void deleteById(Long id) { validationRepository.deleteById(id); }
} 