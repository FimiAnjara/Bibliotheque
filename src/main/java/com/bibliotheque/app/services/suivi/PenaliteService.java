package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.Penalite;
import com.bibliotheque.app.models.suivi.TypePenalite;
import com.bibliotheque.app.repositories.suivi.PenaliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PenaliteService {
    @Autowired
    private PenaliteRepository penaliteRepository;

    public List<Penalite> findAll() { return penaliteRepository.findAll(); }
    public Penalite findById(Long id) { return penaliteRepository.findById(id).orElse(null); }
    public Penalite save(Penalite penalite) { return penaliteRepository.save(penalite); }
    public void deleteById(Long id) { penaliteRepository.deleteById(id); }

    /**
     * Retourne le type de pénalité adapté selon le nombre de jours de retard.
     * Prend le type le plus récent (id max) dont retardJours <= joursRetard.
     * Retourne null si aucun ne correspond.
     */
    public TypePenalite getTypePenalitePourRetard(List<TypePenalite> types, long joursRetard) {
        return types.stream()
            .filter(t -> t.getRetardJours() != null && joursRetard >= t.getRetardJours())
            .sorted((t1, t2) -> t2.getId().compareTo(t1.getId())) // le plus récent
            .findFirst()
            .orElse(null);
    }

    /**
     * Retourne le type de pénalité adapté selon le nombre de jours de retard.
     * Prend le type le plus récent (id max) dont retardJours <= joursRetard.
     * Si aucun ne correspond, retourne un type par défaut (7 jours).
     */
    public TypePenalite getTypePenalitePourRetardOuDefaut(List<TypePenalite> types, long joursRetard) {
        TypePenalite type = getTypePenalitePourRetard(types, joursRetard);
        if (type == null) {
            type = new TypePenalite();
            type.setDescription("Pénalité par défaut");
            type.setDureeJours(7);
            type.setRetardJours(0);
        }
        return type;
    }

    /**
     * Calcule le nombre de jours de retard entre la date prévue et la date effective de retour.
     * Retourne 0 si pas de retard ou si une des dates est nulle.
     */
    public long calculerJoursRetard(LocalDateTime datePrevue, LocalDateTime dateEffective) {
        if (datePrevue == null || dateEffective == null || !dateEffective.isAfter(datePrevue)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(datePrevue.toLocalDate(), dateEffective.toLocalDate());
    }
} 