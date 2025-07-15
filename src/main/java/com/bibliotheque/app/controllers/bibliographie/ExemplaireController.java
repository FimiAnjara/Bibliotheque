package com.bibliotheque.app.controllers.bibliographie;

import com.bibliotheque.app.models.bibliographie.*;
import com.bibliotheque.app.services.bibliographie.*;
import com.bibliotheque.app.services.suivi.StatutExemplaireService;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.services.utilisateur.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.utilisateur.AdherentService;
import com.bibliotheque.app.services.pret.ValidationService;
import com.bibliotheque.app.models.pret.Validation;

@Controller
@RequestMapping("/personnel/exemplaire")
public class ExemplaireController {

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private LivreService livreService;

    @Autowired
    private StatutExemplaireService statutExemplaireService;

    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private PretService pretService;

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private ValidationService validationService;

    @GetMapping("/add")
    public String showAddExemplaireForm(Model model) {
        model.addAttribute("exemplaire", new Exemplaire());
        model.addAttribute("livres", livreService.findAll());
        return "personnel/exemplaire/add";
    }

    @PostMapping("/add")
    public String addExemplaire(@ModelAttribute Exemplaire exemplaire,
            @RequestParam Long livreId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            Livre livre = livreService.findById(livreId);
            if (livre != null)
                exemplaire.setLivre(livre);

            com.bibliotheque.app.models.utilisateur.Utilisateur user = (com.bibliotheque.app.models.utilisateur.Utilisateur) session
                    .getAttribute("user");

            if (user != null) {
                Personnel personnel = personnelService.findById(user.getId());
                if (personnel != null) {
                    exemplaireService.saveWithDefaultStatus(exemplaire, personnel);
                    redirectAttributes.addFlashAttribute("success",
                            "Exemplaire ajouté avec succès ! Statut initial : Disponible");
                } else {
                    exemplaireService.save(exemplaire);
                    redirectAttributes.addFlashAttribute("success", "Exemplaire ajouté avec succès !");
                }
            } else {
                exemplaireService.save(exemplaire);
                redirectAttributes.addFlashAttribute("success", "Exemplaire ajouté avec succès !");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de l'exemplaire : " + e.getMessage());
        }
        return "redirect:/personnel/exemplaire/add";
    }

    @GetMapping("/list")
    public String listExemplaires(@RequestParam(required = false) String search,
            @RequestParam(required = false) String livre,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) Integer statut,
            Model model) {
        List<Exemplaire> exemplaires = exemplaireService.findAll();

        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            exemplaires = exemplaires.stream()
                    .filter(ex -> (ex.getReference() != null && ex.getReference().toLowerCase().contains(searchLower))
                            ||
                            (ex.getLivre() != null && ex.getLivre().getTitre() != null &&
                                    ex.getLivre().getTitre().toLowerCase().contains(searchLower))
                            ||
                            (ex.getLivre() != null && ex.getLivre().getAuteur() != null &&
                                    ((ex.getLivre().getAuteur().getNom() != null &&
                                            ex.getLivre().getAuteur().getNom().toLowerCase().contains(searchLower)) ||
                                            (ex.getLivre().getAuteur().getPrenom() != null &&
                                                    ex.getLivre().getAuteur().getPrenom().toLowerCase()
                                                            .contains(searchLower)))))
                    .collect(Collectors.toList());
        }

        if (livre != null && !livre.trim().isEmpty()) {
            String livreLower = livre.toLowerCase();
            exemplaires = exemplaires.stream()
                    .filter(ex -> ex.getLivre() != null && ex.getLivre().getTitre() != null &&
                            ex.getLivre().getTitre().toLowerCase().contains(livreLower))
                    .collect(Collectors.toList());
        }

        if (reference != null && !reference.trim().isEmpty()) {
            String referenceLower = reference.toLowerCase();
            exemplaires = exemplaires.stream()
                    .filter(ex -> ex.getReference() != null && ex.getReference().toLowerCase().contains(referenceLower))
                    .collect(Collectors.toList());
        }

        if (statut != null) {
            exemplaires = exemplaires.stream()
                    .filter(ex -> statutExemplaireService.getCurrentStatut(ex).getCode() == statut)
                    .collect(Collectors.toList());
        }

        Map<Long, StatutExemplaire.Statut> statutsActuels = exemplaires.stream()
                .collect(Collectors.toMap(
                        Exemplaire::getId,
                        exemplaire -> statutExemplaireService.getCurrentStatut(exemplaire)));

        model.addAttribute("activePage", "exemplaire");
        model.addAttribute("activeSubPage", "exemplaire-list");
        model.addAttribute("exemplaires", exemplaires);
        model.addAttribute("statutsActuels", statutsActuels);
        model.addAttribute("statuts", StatutExemplaire.Statut.values());

        model.addAttribute("search", search);
        model.addAttribute("livre", livre);
        model.addAttribute("reference", reference);
        model.addAttribute("statut", statut);

        return "personnel/exemplaire/list";
    }

    @GetMapping("/preter/{exemplaireId}")
    public String showPretForm(@PathVariable Long exemplaireId, Model model, HttpSession session) {
        com.bibliotheque.app.models.utilisateur.Utilisateur user = (com.bibliotheque.app.models.utilisateur.Utilisateur) session
                .getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        Exemplaire exemplaire = exemplaireService.findById(exemplaireId);
        if (exemplaire == null) {
            model.addAttribute("error", "Exemplaire non trouvé");
            return "redirect:/personnel/exemplaire/list";
        }
        if (exemplaireService.getCurrentStatut(exemplaire).getCode() != 1) { // 1 = DISPONIBLE
            model.addAttribute("error", "Exemplaire non disponible pour le prêt");
            return "redirect:/personnel/exemplaire/list";
        }
        model.addAttribute("exemplaire", exemplaire);
        return "personnel/exemplaire/pret";
    }

    @PostMapping("/preter/{exemplaireId}")
    public String effectuerPret(@PathVariable Long exemplaireId,
            @RequestParam Long adherentId,
            @RequestParam(required = false) String notes,
            @RequestParam(defaultValue = "Domicile") String typePret,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        com.bibliotheque.app.models.utilisateur.Utilisateur user = (com.bibliotheque.app.models.utilisateur.Utilisateur) session
                .getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Utilisateur non connecté");
            return "redirect:/personnel/exemplaire/list";
        }
        Exemplaire exemplaire = exemplaireService.findById(exemplaireId);
        if (exemplaire == null) {
            redirectAttributes.addFlashAttribute("error", "Exemplaire non trouvé");
            return "redirect:/personnel/exemplaire/list";
        }
        if (exemplaireService.getCurrentStatut(exemplaire).getCode() != 1) {
            redirectAttributes.addFlashAttribute("error", "Exemplaire non disponible pour le prêt");
            return "redirect:/personnel/exemplaire/list";
        }
        com.bibliotheque.app.models.utilisateur.Adherent adherent = adherentService.findById(adherentId);
        if (adherent == null) {
            redirectAttributes.addFlashAttribute("error", "Adhérent non trouvé");
            return "redirect:/personnel/exemplaire/list";
        }
        Pret pret = new Pret();

        try {
            pret.setAdherent(adherent);
            pret.setExemplaire(exemplaire);
            pret.setDatePret(java.time.LocalDateTime.now());
            pret.setNotes(notes);
            pret.setDateRetourPrevu(typePret.equals("sur place") 
                ? pretService.getDateRetourPrevueSurPlace(pret.getDatePret())
                : pretService.getDateRetourPrevue(pret.getDatePret(), adherent));
            pret.setTypePret(Pret.TypePret.fromLabel(typePret));
            pretService.saveWithChecks(pret);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/personnel/exemplaire/preter/" + exemplaireId;
        }
        Validation validation = new com.bibliotheque.app.models.pret.Validation();
        validation.setPret(pret);
        validation.setValidationStatus(true);
        validation.setDate(java.time.LocalDateTime.now());
        validation.setAdmin(personnelService.findById(user.getId()));
        validationService.save(validation);
        Personnel personnel = personnelService.findById(user.getId());
        statutExemplaireService.changeStatut(exemplaire, StatutExemplaire.Statut.EMPRUNTE, personnel, "Prêt effectué");
        redirectAttributes.addFlashAttribute("success", "Prêt effectué avec succès !");
        return "redirect:/personnel/exemplaire/list";
    }
}