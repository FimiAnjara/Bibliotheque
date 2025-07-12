package com.bibliotheque.app.controllers.bibliographie;

import com.bibliotheque.app.models.bibliographie.Auteur;
import com.bibliotheque.app.services.bibliographie.AuteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personnel/auteur")
public class AuteurController {

    @Autowired
    private AuteurService auteurService;

    @GetMapping("/add")
    public String showAddAuteurForm(Model model) {
        model.addAttribute("activePage", "auteur");
        model.addAttribute("activeSubPage", "auteur-add");
        model.addAttribute("auteur", new Auteur());
        return "personnel/auteur/add";
    }

    @PostMapping("/add")
    public String addAuteur(@ModelAttribute Auteur auteur, RedirectAttributes redirectAttributes) {
        try {
            auteurService.save(auteur);
            redirectAttributes.addFlashAttribute("success", "Auteur ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de l'auteur : " + e.getMessage());
        }
        return "redirect:/personnel/auteur/add";
    }

    @GetMapping("/list")
    public String listAuteurs(Model model) {
        model.addAttribute("activePage", "auteur");
        model.addAttribute("activeSubPage", "auteur-list");
        model.addAttribute("auteurs", auteurService.findAll());
        return "personnel/auteur/list";
    }
} 