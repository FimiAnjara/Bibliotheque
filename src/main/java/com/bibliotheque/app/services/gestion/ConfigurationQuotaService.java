package com.bibliotheque.app.services.gestion;

import com.bibliotheque.app.models.gestion.ConfigurationQuota;
import com.bibliotheque.app.repositories.gestion.ConfigurationQuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigurationQuotaService {
    @Autowired
    private ConfigurationQuotaRepository configurationQuotaRepository;

    public List<ConfigurationQuota> findAll() { return configurationQuotaRepository.findAll(); }
    public Optional<ConfigurationQuota> findById(Long id) { return configurationQuotaRepository.findById(id); }
    public ConfigurationQuota save(ConfigurationQuota configurationQuota) { return configurationQuotaRepository.save(configurationQuota); }
    public void deleteById(Long id) { configurationQuotaRepository.deleteById(id); }
} 