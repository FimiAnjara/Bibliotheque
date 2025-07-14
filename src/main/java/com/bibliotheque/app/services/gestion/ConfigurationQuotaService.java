package com.bibliotheque.app.services.gestion;

import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.models.utilisateur.Profil;
import com.bibliotheque.app.repositories.gestion.ConfigurationQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationQuotaService {
    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;

    public List<ConfigurationQuota> findAll() { return configurationQuotaRepository.findAll(); }
    public ConfigurationQuota findById(Long id) { return configurationQuotaRepository.findById(id).orElse(null); }
    public ConfigurationQuota save(ConfigurationQuota configurationQuota) { return configurationQuotaRepository.save(configurationQuota); }
    public void deleteById(Long id) { configurationQuotaRepository.deleteById(id); }

    public ConfigurationQuota findByProfil(Profil profil) { return configurationQuotaRepository.findByProfil(profil).orElse(null); }
}   