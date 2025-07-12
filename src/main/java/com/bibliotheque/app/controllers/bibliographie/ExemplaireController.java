package com.bibliotheque.app.controllers.bibliographie;

import com.bibliotheque.app.models.bibliographie.*;
import com.bibliotheque.app.services.bibliographie.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personnel/exemplaire")
public class ExemplaireController {

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private LivreService livreService;

    @GetMapping("/add")
    public String showAddExemplaireForm(Model model) {
        model.addAttribute("exemplaire", new Exemplaire());
        model.addAttribute("livres", livreService.findAll());
        return "personnel/exemplaire/add";
    }

    @PostMapping("/add")
    public String addExemplaire(@ModelAttribute Exemplaire exemplaire,
                               @RequestParam Long livreId,
                               RedirectAttributes redirectAttributes) {
        try {
            Livre livre = livreService.findById(livreId).orElse(null);
            if (livre != null) exemplaire.setLivre(livre);
            
            exemplaireService.save(exemplaire);
            redirectAttributes.addFlashAttribute("success", "Exemplaire ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de l'exemplaire : " + e.getMessage());
        }
        return "redirect:/personnel/exemplaire/add";
    }
} 