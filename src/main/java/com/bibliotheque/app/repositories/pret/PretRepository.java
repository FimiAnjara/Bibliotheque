package com.bibliotheque.app.repositories.pret;

import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PretRepository extends JpaRepository<Pret, Long> {
    List<Pret> findByAdherent(Adherent adherent);
    List<Pret> findByAdherentAndDateRetourEffectuerIsNull(Adherent adherent);
    List<Pret> findByExemplaire(Exemplaire exemplaire);
    List<Pret> findByAdherentOrderByDatePretDesc(Adherent adherent);
    List<Pret> findByDateRetourEffectuerIsNotNull();
} 