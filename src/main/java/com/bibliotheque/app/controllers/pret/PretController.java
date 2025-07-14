package com.bibliotheque.app.controllers.pret;

import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.services.pret.ProlongementPretService;
import com.bibliotheque.app.services.utilisateur.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/personnel/pret")
public class PretController {
    @Autowired
    private PretService pretService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ProlongementPretService prolongementPretService;
    @Autowired
    private PersonnelService personnelService;

    // Liste des prêts non rendus et validés
    @GetMapping("/non-rendu")
    public String pretsNonRendus(Model model) {
        // On ne prend que les prêts validés (présence d'une Validation avec validationStatus=true)
        List<Pret> pretsNonRendus = pretService.findNonRendusEtValides();
        model.addAttribute("pretsNonRendus", pretsNonRendus);
        return "personnel/pret-non-rendu";
    }

    // Formulaire de retour
    @GetMapping("/retour/{id}")
    public String retourForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Pret> pretOpt = pretService.findById(id);
        if (pretOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Prêt introuvable.");
            return "redirect:/personnel/pret/non-rendu";
        }
        model.addAttribute("pret", pretOpt.get());
        return "personnel/pret-retour";
    }

    // Traitement du retour
    @PostMapping("/retour/{id}")
    public String validerRetour(@PathVariable Long id, @RequestParam("dateRetour") String dateRetourStr, RedirectAttributes redirectAttributes) {
        Optional<Pret> pretOpt = pretService.findById(id);
        if (pretOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Prêt introuvable.");
            return "redirect:/personnel/pret/non-rendu";
        }
        Pret pret = pretOpt.get();
        try {
            LocalDate dateRetour = LocalDate.parse(dateRetourStr);
            pret.setDateRetourEffectuer(dateRetour.atStartOfDay());
            pretService.save(pret);
            redirectAttributes.addFlashAttribute("success", "Retour enregistré avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement du retour : " + e.getMessage());
            return "redirect:/personnel/pret/retour/" + id;
        }
        return "redirect:/personnel/pret/non-rendu";
    }

    // Liste des prolongements en attente de validation
    @GetMapping("/prolongements/attente")
    public String prolongementsAttente(Model model) {
        List<ProlongementPret> prolongements = prolongementPretService.findAll().stream()
            .filter(p -> !validationService.findAll().stream().anyMatch(v -> v.getProlongement() != null && v.getProlongement().getId().equals(p.getId()) && Boolean.TRUE.equals(v.getValidationStatus())))
            .toList();
        model.addAttribute("prolongements", prolongements);
        return "personnel/prolongement/prolongement-attente";
    }

    // Formulaire de validation d'un prolongement
    @GetMapping("/prolongements/valider/{id}")
    public String validerProlongementForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProlongementPret> prolongementOpt = prolongementPretService.findById(id);
        if (prolongementOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Prolongement introuvable.");
            return "redirect:/personnel/pret/prolongements/attente";
        }
        model.addAttribute("prolongement", prolongementOpt.get());
        return "personnel/prolongement/prolongement-valider";
    }

    // Traitement de la validation/refus d'un prolongement
    @PostMapping("/prolongements/valider/{id}")
    public String validerProlongement(@PathVariable Long id, @RequestParam("validation") boolean validation, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<ProlongementPret> prolongementOpt = prolongementPretService.findById(id);
        if (prolongementOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Prolongement introuvable.");
            return "redirect:/personnel/pret/prolongements/attente";
        }
        ProlongementPret prolongement = prolongementOpt.get();
        com.bibliotheque.app.models.utilisateur.Utilisateur user = (com.bibliotheque.app.models.utilisateur.Utilisateur) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Utilisateur non connecté");
            return "redirect:/personnel/pret/prolongements/attente";
        }
        com.bibliotheque.app.models.utilisateur.Personnel personnel = personnelService.findById(user.getId());
        if (personnel == null) {
            redirectAttributes.addFlashAttribute("error", "Personnel introuvable");
            return "redirect:/personnel/pret/prolongements/attente";
        }
        Validation v = new Validation();
        v.setProlongement(prolongement);
        v.setValidationStatus(validation);
        v.setDate(java.time.LocalDateTime.now());
        v.setAdmin(personnel);
        validationService.save(v);
        redirectAttributes.addFlashAttribute("success", validation ? "Prolongement validé." : "Prolongement refusé.");
        return "redirect:/personnel/pret/prolongements/attente";
    }
} 