package com.bibliotheque.app.repositories.gestion;

import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.models.utilisateur.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationQuotaRepository extends JpaRepository<ConfigurationQuota, Long> {
    Optional<ConfigurationQuota> findByProfil(Profil profil);
} 