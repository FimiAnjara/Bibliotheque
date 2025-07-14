package com.bibliotheque.app.repositories.suivi;

import com.bibliotheque.app.models.suivi.Notification;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUtilisateurOrderByDateCreationDesc(Utilisateur utilisateur);
    
    List<Notification> findByUtilisateurAndEstLuFalseOrderByDateCreationDesc(Utilisateur utilisateur);
    
    long countByUtilisateurAndEstLuFalse(Utilisateur utilisateur);
} 