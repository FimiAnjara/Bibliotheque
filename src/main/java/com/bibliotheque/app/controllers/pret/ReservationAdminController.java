package com.bibliotheque.app.controllers.pret;

import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.services.pret.ReservationService;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.services.pret.ReservationValidationService;
import com.bibliotheque.app.services.utilisateur.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/personnel/reservation")
public class ReservationAdminController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ReservationValidationService reservationValidationService;

    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private PretService pretService;

    @GetMapping("/list")
    public String listReservationsActives(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        Personnel personnel = personnelService.findById(user.getId());
        if (personnel == null) {
            return "redirect:/";
        }

        List<Reservation> reservationsActives = reservationService.findAllReservationsActives();
        
        model.addAttribute("reservations", reservationsActives);
        model.addAttribute("activePage", "reservation");
        model.addAttribute("activeSubPage", "reservation-list");
        
        return "personnel/reservation/list";
    }

    @GetMapping("/check-validation/{reservationId}")
    @ResponseBody
    public Map<String, Object> checkValidation(@PathVariable Long reservationId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {  
            response.put("success", false);
            response.put("message", "Utilisateur non connecté");
            return response;
        }

        try {
            Reservation reservation = reservationService.findById(reservationId);
            if (reservation == null) {
                response.put("success", false);
                response.put("message", "Réservation non trouvée");
                return response;
            }

            Boolean result = reservationValidationService.canValidateReservation(reservation);
            
            response.put("success", true);
            response.put("canValidate", result);

            if (result) {
                LocalDateTime dateRetourPrevue = pretService.getDateRetourPrevue(reservation.getDateSouhaiter(), reservation.getAdherent());
                
                response.put("reservationDetails", Map.of(
                    "adherent", reservation.getAdherent().getPrenom() + " " + reservation.getAdherent().getNom(),
                    "numeroAdherent", reservation.getAdherent().getNumeroAdherent(),
                    "livre", reservation.getExemplaire().getLivre().getTitre(),
                    "exemplaire", reservation.getExemplaire().getReference(),
                    "dateSouhaiter", reservation.getDateSouhaiter(),
                    "dateRetourPrevue", dateRetourPrevue
                ));
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la vérification : " + e.getMessage());
        }
        
        return response;
    }

    @PostMapping("/valider/{reservationId}")
    @ResponseBody
    public Map<String, Object> validerReservation(@PathVariable Long reservationId, 
                                                 HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Utilisateur non connecté");
            return response;
        }

        try {
           Reservation reservation = reservationService.findById(reservationId);
            if (reservation == null) {
                response.put("success", false);
                response.put("message", "Réservation non trouvée");
                return response;
            }

            Boolean validationResult = reservationValidationService.canValidateReservation(reservation);
            if (!validationResult) {
                response.put("success", false);
                response.put("message", "La réservation ne peut pas être validée");
                return response;
            }

            Personnel personnel = personnelService.findById(user.getId());
            Pret pret = reservationValidationService.validateReservation(reservation, personnel);
            
            response.put("success", true);
            response.put("message", "Réservation validée avec succès. Prêt #" + pret.getId() + " créé.");
            response.put("pretId", pret.getId());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la validation : " + e.getMessage());
        }
        
        return response;
    }

    @PostMapping("/annuler/{reservationId}")
    @ResponseBody
    public String annulerReservation(@PathVariable Long reservationId, 
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        Personnel personnel = personnelService.findById(user.getId());
        if (personnel == null) {
            return "redirect:/";
        }

        try {
            Reservation reservation = reservationService.findById(reservationId);
            if (reservation == null) {
                return "Réservation non trouvée";
            }

            Validation validation = new Validation();
            validation.setReservation(reservation);
            validation.setValidationStatus(false);
            validation.setDate(LocalDateTime.now());
            validation.setAdmin(personnel);

            validationService.save(validation);

            return "Réservation annulée avec succès";
        } catch (Exception e) {
            return "Erreur lors de l'annulation : " + e.getMessage();
        }
    }
} 