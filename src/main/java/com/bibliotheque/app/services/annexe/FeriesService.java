package com.bibliotheque.app.services.annexe;

import com.bibliotheque.app.models.annexe.Feries;
import com.bibliotheque.app.repositories.annexe.FeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeriesService {
    @Autowired
    private FeriesRepository feriesRepository;

    public List<Feries> findAll() { return feriesRepository.findAll(); }
    public Optional<Feries> findById(Long id) { return feriesRepository.findById(id); }
    public Feries save(Feries feries) { return feriesRepository.save(feries); }
    public void deleteById(Long id) { feriesRepository.deleteById(id); }
} 