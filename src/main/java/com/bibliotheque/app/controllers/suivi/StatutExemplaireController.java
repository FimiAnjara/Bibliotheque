package com.bibliotheque.app.controllers.suivi;

import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.services.suivi.StatutExemplaireService;
import com.bibliotheque.app.services.bibliographie.ExemplaireService;
import com.bibliotheque.app.services.utilisateur.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/personnel/statut-exemplaire")
public class StatutExemplaireController {
    
    @Autowired
    private StatutExemplaireService statutExemplaireService;
    
    @Autowired
    private ExemplaireService exemplaireService;
    
    @Autowired
    private PersonnelService personnelService;
    
    @GetMapping("/change/{exemplaireId}")
    public String showChangeStatutForm(@PathVariable Long exemplaireId, Model model, HttpSession session) {
        // Vérifier que l'utilisateur est connecté et est un personnel
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Personnel personnel = personnelService.findById(user.getId());
        if (personnel == null) {
            return "redirect:/";
        }
        
        Optional<Exemplaire> exemplaireOpt = exemplaireService.findById(exemplaireId);
        if (exemplaireOpt.isEmpty()) {
            return "redirect:/personnel/exemplaire/list";
        }
        
        Exemplaire exemplaire = exemplaireOpt.get();
        StatutExemplaire.Statut currentStatut = exemplaireService.getCurrentStatut(exemplaire);
        
        model.addAttribute("exemplaire", exemplaire);
        model.addAttribute("currentStatut", currentStatut);
        model.addAttribute("statuts", StatutExemplaire.Statut.values());
        model.addAttribute("activePage", "exemplaire");
        model.addAttribute("activeSubPage", "exemplaire-list");
        
        return "personnel/statut-exemplaire/change";
    }
    
    @PostMapping("/change/{exemplaireId}")
    public String changeStatut(@PathVariable Long exemplaireId,
                              @RequestParam Integer nouveauStatut,
                              @RequestParam(required = false) String notes,
                              Model model, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // Vérifier que l'utilisateur est connecté et est un personnel
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Personnel personnel = personnelService.findById(user.getId());
        if (personnel == null) {
            return "redirect:/";
        }
        
        Optional<Exemplaire> exemplaireOpt = exemplaireService.findById(exemplaireId);
        if (exemplaireOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Exemplaire non trouvé.");
            return "redirect:/personnel/exemplaire/list";
        }
        
        try {
            Exemplaire exemplaire = exemplaireOpt.get();
            StatutExemplaire.Statut statut = StatutExemplaire.Statut.fromCode(nouveauStatut);
            
            statutExemplaireService.changeStatut(exemplaire, statut, personnel, notes);
            
            redirectAttributes.addFlashAttribute("success", 
                "Statut de l'exemplaire " + exemplaire.getReference() + " changé vers " + statut.getLibelle() + " avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors du changement de statut : " + e.getMessage());
        }
        
        return "redirect:/personnel/exemplaire/list";
    }
}
