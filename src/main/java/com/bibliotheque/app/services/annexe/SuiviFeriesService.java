package com.bibliotheque.app.services.annexe;

import com.bibliotheque.app.models.annexe.SuiviFeries;
import com.bibliotheque.app.repositories.annexe.SuiviFeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuiviFeriesService {
    @Autowired
    private SuiviFeriesRepository suiviFeriesRepository;

    public List<SuiviFeries> findAll() { return suiviFeriesRepository.findAll(); }
    public Optional<SuiviFeries> findById(Long id) { return suiviFeriesRepository.findById(id); }
    public SuiviFeries save(SuiviFeries suiviFeries) { return suiviFeriesRepository.save(suiviFeries); }
    public void deleteById(Long id) { suiviFeriesRepository.deleteById(id); }
} 