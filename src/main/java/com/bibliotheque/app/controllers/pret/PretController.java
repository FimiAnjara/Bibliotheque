package com.bibliotheque.app.controllers.pret;

import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.models.pret.Validation;
import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.services.pret.ProlongementPretService;
import com.bibliotheque.app.services.utilisateur.PersonnelService;
import com.bibliotheque.app.services.suivi.TypePenaliteService;
import com.bibliotheque.app.services.suivi.PenaliteService;
import com.bibliotheque.app.models.suivi.TypePenalite;
import com.bibliotheque.app.models.suivi.Penalite;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.services.suivi.StatutExemplaireService;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
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
import java.util.Map;

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
    @Autowired
    private TypePenaliteService typePenaliteService;
    @Autowired
    private PenaliteService penaliteService;
    @Autowired
    private StatutExemplaireService statutExemplaireService;

    @GetMapping("/non-rendu")
    public String pretsNonRendus(Model model) {
        List<Pret> pretsNonRendus = pretService.findNonRendusEtValides();
        Map<Long, java.time.LocalDateTime> dateRetourPrevueEffective = new java.util.HashMap<>();
        for (Pret pret : pretsNonRendus) {
            dateRetourPrevueEffective.put(pret.getId(), pretService.getDateRetourPrevueEffective(pret.getId()));
        }
        model.addAttribute("pretsNonRendus", pretsNonRendus);
        model.addAttribute("dateRetourPrevueEffective", dateRetourPrevueEffective);
        return "personnel/pret-non-rendu";
    }

    @GetMapping("/retour/{id}")
    public String retourForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Pret pret = pretService.findById(id).orElse(null);
        if (pret == null) {
            redirectAttributes.addFlashAttribute("error", "Prêt introuvable.");
            return "redirect:/personnel/pret/non-rendu";
        }
        model.addAttribute("pret", pret);
        return "personnel/pret-retour";
    }

    @PostMapping("/retour/{id}")
    public String validerRetour(@PathVariable Long id, @RequestParam("dateRetour") String dateRetourStr, RedirectAttributes redirectAttributes, HttpSession session) {
        Pret pret = pretService.findById(id).orElse(null);
        if (pret == null) {
            redirectAttributes.addFlashAttribute("error", "Prêt introuvable.");
            return "redirect:/personnel/pret/non-rendu";
        }
        try {
            LocalDateTime dateRetour = LocalDateTime.parse(dateRetourStr);
            pret.setDateRetourEffectuer(dateRetour);
            pretService.save(pret);
            Utilisateur user = (Utilisateur) session.getAttribute("user");
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Utilisateur non connecté");
                return "redirect:/personnel/pret/non-rendu";
            }
            Personnel personnel = personnelService.findById(user.getId());
            Exemplaire exemplaire = pret.getExemplaire();
            statutExemplaireService.changeStatut(exemplaire, StatutExemplaire.Statut.DISPONIBLE, personnel, "Retour de prêt");
            java.time.LocalDateTime datePrevue = pretService.getDateRetourPrevueEffective(pret.getId());
            if (pret.getDateRetourEffectuer() != null && datePrevue != null && pret.getDateRetourEffectuer().isAfter(datePrevue)) {
                long joursRetard = penaliteService.calculerJoursRetard(datePrevue, pret.getDateRetourEffectuer());
                if (joursRetard > 0) {
                List<TypePenalite> types = typePenaliteService.findAll();
                TypePenalite type = penaliteService.getTypePenalitePourRetardOuDefaut(types, joursRetard);
                Penalite penalite = new Penalite();
                penalite.setAdherent(pret.getAdherent());
                penalite.setTypePenalite(type);
                penalite.setDateApplication(pret.getDateRetourEffectuer().toLocalDate());
                penalite.setDateFin(penalite.getDateApplication().plusDays(type.getDureeJours() != null ? type.getDureeJours() : 7));
                penalite.setNotes("Retard de " + joursRetard + " jours sur le prêt #" + pret.getId());
                penalite.setAdmin(personnel);
                penaliteService.save(penalite);
                redirectAttributes.addFlashAttribute("warning", "Retour en retard : pénalité appliquée (" + type.getDureeJours() + " jours)");
                }
            } else {
                redirectAttributes.addFlashAttribute("success", "Retour enregistré avec succès.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement du retour : " + e.getMessage());
            return "redirect:/personnel/pret/retour/" + id;
        }
        return "redirect:/personnel/pret/non-rendu";
    }

    @GetMapping("/prolongements/attente")
    public String prolongementsAttente(Model model) {
        List<ProlongementPret> prolongements = prolongementPretService.findAll().stream()
            .filter(p -> !validationService.findAll().stream().anyMatch(v -> v.getProlongement() != null && v.getProlongement().getId().equals(p.getId()) && Boolean.TRUE.equals(v.getValidationStatus())))
            .toList();
        model.addAttribute("prolongements", prolongements);
        return "personnel/prolongement/prolongement-attente";
    }

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

    @GetMapping("/rendu")
    public String pretsRendus(Model model) {
        List<Pret> pretsRendus = pretService.findPretsRendus();
        model.addAttribute("pretsRendus", pretsRendus);
        return "personnel/pret-rendu";
    }
} 