package com.bibliotheque.app.repositories.suivi;

import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatutExemplaireRepository extends JpaRepository<StatutExemplaire, Long> {
    
    List<StatutExemplaire> findByExemplaireOrderByDateChangementDesc(Exemplaire exemplaire);
    
    @Query("SELECT se FROM StatutExemplaire se WHERE se.exemplaire = :exemplaire ORDER BY se.dateChangement DESC")
    List<StatutExemplaire> findStatutsByExemplaire(@Param("exemplaire") Exemplaire exemplaire);
    
    @Query("SELECT se FROM StatutExemplaire se WHERE se.exemplaire = :exemplaire ORDER BY se.dateChangement DESC LIMIT 1")
    Optional<StatutExemplaire> findCurrentStatutByExemplaire(@Param("exemplaire") Exemplaire exemplaire);
} 