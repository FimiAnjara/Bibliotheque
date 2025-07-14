package com.bibliotheque.app.controllers.pret;

import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.models.pret.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/personnel/pret")
public class PretController {
    @Autowired
    private PretService pretService;
    @Autowired
    private ValidationService validationService;

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
} 