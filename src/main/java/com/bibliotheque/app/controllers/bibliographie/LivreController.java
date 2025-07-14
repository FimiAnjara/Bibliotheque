package com.bibliotheque.app.controllers.bibliographie;

import com.bibliotheque.app.models.bibliographie.*;
import com.bibliotheque.app.services.bibliographie.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personnel/livre")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private EditeurService editeurService;

    @Autowired
    private CategorieService categorieService;

    @GetMapping("/add")
    public String showAddLivreForm(Model model) {
        model.addAttribute("activePage", "livre");
        model.addAttribute("activeSubPage", "livre-add");
        model.addAttribute("livre", new Livre());
        model.addAttribute("auteurs", auteurService.findAll());
        model.addAttribute("editeurs", editeurService.findAll());
        model.addAttribute("categories",categorieService.findAll());
        return "personnel/livre/add";
    }

    @PostMapping("/add")
    public String addLivre(@ModelAttribute Livre livre, 
                          @RequestParam Long auteurId,
                          @RequestParam Long editeurId,
                          RedirectAttributes redirectAttributes) {
        try {
            Auteur auteur = auteurService.findById(auteurId);
            Editeur editeur = editeurService.findById(editeurId).orElse(null);
            
            
            if (auteur != null) livre.setAuteur(auteur);
            if (editeur != null) livre.setEditeur(editeur);
            
            livreService.save(livre);
            redirectAttributes.addFlashAttribute("success", "Livre ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du livre : " + e.getMessage());
        }
        return "redirect:/personnel/livre/add";
    }

    @GetMapping("/list")
    public String listLivres(Model model) {
        model.addAttribute("activePage", "livre");
        model.addAttribute("activeSubPage", "livre-list");
        model.addAttribute("livres", livreService.findAll());
        return "personnel/livre/list";
    }
} 