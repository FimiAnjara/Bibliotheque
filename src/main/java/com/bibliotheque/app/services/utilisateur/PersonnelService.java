package com.bibliotheque.app.services.utilisateur;

import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.repositories.utilisateur.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {
    @Autowired
    private PersonnelRepository personnelRepository;

    public List<Personnel> findAll() { 
        return personnelRepository.findAll(); 
    }
    
    public Personnel findById(Long id) { 
        return personnelRepository.findById(id).orElse(null); 
    }
    
    public Personnel save(Personnel personnel) { 
        return personnelRepository.save(personnel); 
    }
    
    public void deleteById(Long id) { 
        personnelRepository.deleteById(id); 
    }
} 