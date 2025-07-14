package com.bibliotheque.app.controllers.utilisateur;

import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.bibliographie.Livre;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.services.utilisateur.AdherentService;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.pret.ReservationService;
import com.bibliotheque.app.services.pret.ProlongementPretService;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.services.bibliographie.LivreService;
import com.bibliotheque.app.services.bibliographie.ExemplaireService;
import com.bibliotheque.app.repositories.bibliographie.ExemplaireRepository;
import com.bibliotheque.app.services.suivi.NotificationService;
import com.bibliotheque.app.models.suivi.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/adherent")
public class AdherentController {
    
    @Autowired
    private AdherentService adherentService;
    
    @Autowired
    private PretService pretService;
    
    @Autowired
    private LivreService livreService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private ExemplaireService exemplaireService;
    
    @Autowired
    private ExemplaireRepository exemplaireRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProlongementPretService prolongementPretService;

    @Autowired
    private ValidationService validationService;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
        if (adherentOpt.isPresent()) {
            Adherent adherent = adherentOpt.get();
            model.addAttribute("adherent", adherent);
            model.addAttribute("user", user);
            
            List<Pret> empruntsEnCours = pretService.findByAdherentAndDateRetourEffectuerIsNull(adherent);
            model.addAttribute("empruntsEnCours", empruntsEnCours);
            
            List<Pret> historiqueEmprunts = pretService.findByAdherentOrderByDatePretDesc(adherent);
            model.addAttribute("historiqueEmprunts", historiqueEmprunts);
            
            Map<Long, Map<String, Integer>> disponibiliteMap = new HashMap<>();
            for (Pret pret : empruntsEnCours) {
                Livre livre = pret.getExemplaire().getLivre();
                int exemplairesDisponibles = livreService.getNombreExemplairesDisponibles(livre);
                int totalExemplaires = livreService.getNombreTotalExemplaires(livre);
                
                Map<String, Integer> livreInfo = new HashMap<>();
                livreInfo.put("disponibles", exemplairesDisponibles);
                livreInfo.put("total", totalExemplaires);
                disponibiliteMap.put(livre.getId(), livreInfo);
            }
            model.addAttribute("disponibiliteMap", disponibiliteMap);
            
            Map<Long, java.time.LocalDateTime> dateRetourPrevueEffective = new HashMap<>();
            for (Pret pret : empruntsEnCours) {
                dateRetourPrevueEffective.put(pret.getId(), pretService.getDateRetourPrevueEffective(pret.getId()));
            }
            model.addAttribute("dateRetourPrevueEffective", dateRetourPrevueEffective);
            
            return "adherent/home";
        }
        
        return "redirect:/";
    }

    @GetMapping("/livres")
    public String livres(Model model, HttpSession session, 
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String categorie,
                        @RequestParam(required = false) String auteur) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        model.addAttribute("user", user);
        
        List<Livre> livres;
        if (search != null && !search.trim().isEmpty()) {
            livres = livreService.searchLivres(search);
        } else if (categorie != null && !categorie.trim().isEmpty()) {
            livres = livreService.findByCategorie(categorie);
        } else if (auteur != null && !auteur.trim().isEmpty()) {
            livres = livreService.findByAuteur(auteur);
        } else {
            livres = livreService.findAll();
        }
        
        model.addAttribute("livres", livres);
        model.addAttribute("search", search);
        model.addAttribute("categorie", categorie);
        model.addAttribute("auteur", auteur);
        
        Map<Long, Map<String, Integer>> disponibiliteMap = new HashMap<>();
        for (Livre livre : livres) {
            int exemplairesDisponibles = livreService.getNombreExemplairesDisponibles(livre);
            int totalExemplaires = livreService.getNombreTotalExemplaires(livre);
            
            Map<String, Integer> livreInfo = new HashMap<>();
            livreInfo.put("disponibles", exemplairesDisponibles);
            livreInfo.put("total", totalExemplaires);
            disponibiliteMap.put(livre.getId(), livreInfo);
        }
        model.addAttribute("disponibiliteMap", disponibiliteMap);
        
        return "adherent/livres";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
        if (adherentOpt.isPresent()) {
            Adherent adherent = adherentOpt.get();
            model.addAttribute("adherent", adherent);
            model.addAttribute("user", user);
            
            List<Pret> empruntsEnCours = pretService.findByAdherentAndDateRetourEffectuerIsNull(adherent);
            model.addAttribute("empruntsEnCours", empruntsEnCours);
            Map<Long, java.time.LocalDateTime> dateRetourPrevueEffective = new HashMap<>();
            for (Pret pret : empruntsEnCours) {
                dateRetourPrevueEffective.put(pret.getId(), pretService.getDateRetourPrevueEffective(pret.getId()));
            }
            model.addAttribute("dateRetourPrevueEffective", dateRetourPrevueEffective);
            
            List<Pret> historiqueEmprunts = pretService.findByAdherentOrderByDatePretDesc(adherent);
            model.addAttribute("historiqueEmprunts", historiqueEmprunts);
            
            Map<Long, Map<String, Integer>> disponibiliteMap = new HashMap<>();
            for (Pret pret : empruntsEnCours) {
                Livre livre = pret.getExemplaire().getLivre();
                int exemplairesDisponibles = livreService.getNombreExemplairesDisponibles(livre);
                int totalExemplaires = livreService.getNombreTotalExemplaires(livre);
                
                Map<String, Integer> livreInfo = new HashMap<>();
                livreInfo.put("disponibles", exemplairesDisponibles);
                livreInfo.put("total", totalExemplaires);
                disponibiliteMap.put(livre.getId(), livreInfo);
            }
            model.addAttribute("disponibiliteMap", disponibiliteMap);
            
            long notificationsNonLues = notificationService.countByUtilisateurAndEstLuFalse(user);
            model.addAttribute("notificationsNonLues", notificationsNonLues);
            
            return "adherent/profile";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/livre/{livreId}/exemplaires")
    @ResponseBody
    public List<Map<String, Object>> getExemplairesDisponibles(@PathVariable Long livreId, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return new ArrayList<>();
        }
        
        Optional<Livre> livreOpt = livreService.findById(livreId);
        if (!livreOpt.isPresent()) {
            return new ArrayList<>();
        }
        
        Livre livre = livreOpt.get();
        List<Exemplaire> exemplaires = exemplaireRepository.findByLivre(livre);
        
        return exemplaires.stream()
            .filter(exemplaire -> exemplaireService.getCurrentStatut(exemplaire).getCode() == 1) // Disponible
            .map(exemplaire -> {
                Map<String, Object> exemplaireInfo = new HashMap<>();
                exemplaireInfo.put("id", exemplaire.getId());
                exemplaireInfo.put("reference", exemplaire.getReference());
                exemplaireInfo.put("dateAcquisition", exemplaire.getDateAcquisition());
                return exemplaireInfo;
            })
            .collect(Collectors.toList());
    }
    
    @GetMapping("/quota-reservation")
    @ResponseBody
    public Map<String, Object> getQuotaReservation(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Utilisateur non connecté");
            return response;
        }
        Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
        if (!adherentOpt.isPresent()) {
            response.put("success", false);
            response.put("message", "Adhérent non trouvé");
            return response;
        }
        Adherent adherent = adherentOpt.get();
        int quotaMax = reservationService.getQuotaReservation(adherent);
        int reservationsActives = reservationService.getReservationsActives(adherent);
        int reservationsRestantes = quotaMax - reservationsActives;
        boolean peutReserver = reservationsActives < quotaMax;
        response.put("success", true);
        response.put("quotaMax", quotaMax);
        response.put("reservationsActives", reservationsActives);
        response.put("reservationsRestantes", reservationsRestantes);
        response.put("peutReserver", peutReserver);
        return response;
    }
    
    @PostMapping("/reserver")
    @ResponseBody
    public Map<String, Object> reserverLivre(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Utilisateur non connecté");
            return response;
        }
        try {
            Long exemplaireId = Long.parseLong(request.get("exemplaireId").toString());
            String dateSouhaiterStr = request.get("dateSouhaiter").toString();
            Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
            if (!adherentOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Adhérent non trouvé");
                return response;
            }
            Optional<Exemplaire> exemplaireOpt = exemplaireService.findById(exemplaireId);
            if (!exemplaireOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Exemplaire non trouvé");
                return response;
            }
            Adherent adherent = adherentOpt.get();  
            if (!reservationService.peutReserver(adherent)) {
                int quotaMax = reservationService.getQuotaReservation(adherent);
                int reservationsActives = reservationService.getReservationsActives(adherent);
                int reservationsRestantes = quotaMax - reservationsActives;
                boolean peutReserver = reservationsActives < quotaMax;
                response.put("success", false);
                response.put("message", "Quota de réservation atteint. Vous avez " + reservationsActives + 
                           " réservation(s) active(s) sur " + quotaMax + " autorisée(s).");
                response.put("quotaMax", quotaMax);
                response.put("reservationsActives", reservationsActives);
                response.put("reservationsRestantes", reservationsRestantes);
                response.put("peutReserver", peutReserver);
                return response;
            }
            LocalDateTime dateSouhaiter = LocalDateTime.parse(dateSouhaiterStr);
            Exemplaire exemplaire = exemplaireOpt.get();
            Reservation reservation = reservationService.createReservation(adherent, exemplaire, dateSouhaiter);
            response.put("success", true);
            response.put("message", "Réservation créée avec succès");
            response.put("reservationId", reservation.getId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la réservation: " + e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/notifications")
public String notifications(Model model, HttpSession session) {
    Utilisateur user = (Utilisateur) session.getAttribute("user");
    if (user == null) {
        return "redirect:/";
    }
    
    List<Notification> notifications = notificationService.findByUtilisateurOrderByDateCreationDesc(user);
    
    if (notifications == null) {
        notifications = new ArrayList<>();
    }
    
    System.out.println("Notifications pour utilisateur {}: {}" + user.getId() + notifications);
    
    long totalNotifications = notifications.size();
    long notificationsNonLues = notifications.stream()
        .filter(n -> n != null && !n.getEstLu())
        .count();
    
    model.addAttribute("notifications", notifications);
    model.addAttribute("user", user);
    model.addAttribute("totalNotifications", totalNotifications);
    model.addAttribute("notificationsNonLues", notificationsNonLues);
    model.addAttribute("notificationsLues", totalNotifications - notificationsNonLues);
    
    return "adherent/notifications";
}
    
    @PostMapping("/notification/{notificationId}/marquer-lu")
    @ResponseBody
    public Map<String, Object> marquerNotificationLue(@PathVariable Long notificationId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Utilisateur non connecté");
            return response;
        }
        
        try {
            Optional<Notification> notificationOpt = notificationService.findById(notificationId);
            if (!notificationOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Notification non trouvée");
                return response;
            }
            
            Notification notification = notificationOpt.get();
            
            if (!notification.getUtilisateur().getId().equals(user.getId())) {
                response.put("success", false);
                response.put("message", "Accès non autorisé");
                return response;
            }
            
            notification.setEstLu(true);
            notificationService.save(notification);
            
            response.put("success", true);
            response.put("message", "Notification marquée comme lue");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors du marquage : " + e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/notifications/non-lues")
    @ResponseBody
    public Map<String, Object> getNotificationsNonLues(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Utilisateur non connecté");
            return response;
        }
        
        try {
            List<Notification> notificationsNonLues = notificationService.findByUtilisateurAndEstLuFalseOrderByDateCreationDesc(user);
            response.put("success", true);
            response.put("count", notificationsNonLues.size());
            response.put("notifications", notificationsNonLues);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la récupération : " + e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/prets")
    public String prets(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
        if (adherentOpt.isEmpty()) {
            return "redirect:/";
        }
        Adherent adherent = adherentOpt.get();
        List<Pret> pretsEnCours = pretService.findByAdherentAndDateRetourEffectuerIsNull(adherent);
        Map<Long, Boolean> pretAvecProlongementNonValide = new HashMap<>();
        Map<Long, java.time.LocalDateTime> dateRetourPrevueEffective = new HashMap<>();
        for (Pret pret : pretsEnCours) {
            boolean hasNonValide = prolongementPretService.hasNonValideProlongement(pret);
            pretAvecProlongementNonValide.put(pret.getId(), hasNonValide);
            dateRetourPrevueEffective.put(pret.getId(), pretService.getDateRetourPrevueEffective(pret.getId()));
        }
        model.addAttribute("pretsEnCours", pretsEnCours);
        model.addAttribute("pretAvecProlongementNonValide", pretAvecProlongementNonValide);
        model.addAttribute("dateRetourPrevueEffective", dateRetourPrevueEffective);
        model.addAttribute("user", user);
        return "adherent/prets";
    }

    @GetMapping("/pret/prolonger/{pretId}")
    public String demanderProlongementForm(@PathVariable Long pretId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        Optional<Pret> pretOpt = pretService.findById(pretId);
        if (pretOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Prêt introuvable.");
            return "redirect:/adherent/prets";
        }
        Pret pret = pretOpt.get();
        boolean hasNonValide = prolongementPretService.hasNonValideProlongement(pret);
        if (hasNonValide) {
            redirectAttributes.addFlashAttribute("error", "Vous avez déjà une demande de prolongement en attente de validation pour ce prêt.");
            return "redirect:/adherent/prets";
        }
        model.addAttribute("pret", pret);
        model.addAttribute("user", user);
        model.addAttribute("dateRetourPrevueEffective", pretService.getDateRetourPrevueEffective(pretId));
        return "adherent/demande-prolongement";
    }

    @PostMapping("/pret/prolonger/{pretId}")
    public String demanderProlongement(@PathVariable Long pretId, @RequestParam("nouvelleDateRetour") String nouvelleDateRetourStr, HttpSession session, RedirectAttributes redirectAttributes) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        Optional<Pret> pretOpt = pretService.findById(pretId);
        if (pretOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Prêt introuvable.");
            return "redirect:/adherent/prets";
        }
        Pret pret = pretOpt.get();
        try {
            java.time.LocalDateTime nouvelleDateRetour = java.time.LocalDate.parse(nouvelleDateRetourStr).atStartOfDay();
            ProlongementPret prolongement = new ProlongementPret();
            prolongement.setPret(pret);
            prolongement.setDateProlongement(java.time.LocalDateTime.now());
            prolongement.setDateRetourPrevu(nouvelleDateRetour);
            prolongementPretService.save(prolongement);
            redirectAttributes.addFlashAttribute("success", "Demande de prolongement envoyée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/adherent/prets";
    }
} 