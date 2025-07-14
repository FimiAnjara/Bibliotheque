package com.bibliotheque.app.services.suivi;

import com.bibliotheque.app.models.suivi.Notification;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.repositories.suivi.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> findAll() { return notificationRepository.findAll(); }
    public Optional<Notification> findById(Long id) { return notificationRepository.findById(id); }
    public Notification save(Notification notification) { return notificationRepository.save(notification); }
    public void deleteById(Long id) { notificationRepository.deleteById(id); }
    
    public List<Notification> findByUtilisateurAndEstLuFalseOrderByDateCreationDesc(Utilisateur utilisateur) {
        return notificationRepository.findByUtilisateurAndEstLuFalseOrderByDateCreationDesc(utilisateur);
    }
    
    public long countByUtilisateurAndEstLuFalse(Utilisateur utilisateur) {
        return notificationRepository.countByUtilisateurAndEstLuFalse(utilisateur);
    }
    public List<Notification> findByUtilisateurOrderByDateCreationDesc(Utilisateur user) {
    List<Notification> result = notificationRepository.findByUtilisateurOrderByDateCreationDesc(user);
    System.out.println("Notifications trouvées pour user {}: {}"+ user.getId()+ result);
    return result;
}
} 