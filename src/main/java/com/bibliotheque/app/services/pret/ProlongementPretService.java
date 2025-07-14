package com.bibliotheque.app.services.pret;

import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.repositories.pret.ProlongementPretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.pret.Pret;

@Service
public class ProlongementPretService {
    @Autowired
    private ProlongementPretRepository prolongementPretRepository;
    @Autowired
    private ValidationService validationService;

    public List<ProlongementPret> findAll() { return prolongementPretRepository.findAll(); }
    public Optional<ProlongementPret> findById(Long id) { return prolongementPretRepository.findById(id); }
    public ProlongementPret save(ProlongementPret prolongementPret) { return prolongementPretRepository.save(prolongementPret); }
    public void deleteById(Long id) { prolongementPretRepository.deleteById(id); }

    public boolean hasNonValideProlongement(Pret pret) {
        return prolongementPretRepository.findByPretOrderByDateProlongementDesc(pret).stream()
            .anyMatch(p -> p.getId() != null && !hasValidationForProlongement(p.getId()));
    }
    private boolean hasValidationForProlongement(Long prolongementId) {
        return validationService.findAll().stream()
            .anyMatch(v -> v.getProlongement() != null && v.getProlongement().getId().equals(prolongementId) && Boolean.TRUE.equals(v.getValidationStatus()));
    }
} 