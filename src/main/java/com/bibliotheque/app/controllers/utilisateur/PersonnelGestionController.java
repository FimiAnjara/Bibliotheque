package com.bibliotheque.app.controllers.utilisateur;

import com.bibliotheque.app.models.bibliographie.*;
import com.bibliotheque.app.services.bibliographie.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/personnel")
public class PersonnelGestionController {

    @Autowired
    private CategorieService categorieService;

    // ========== CATEGORIE ==========
    @GetMapping("/categorie/add")
    public String showAddCategorieForm(Model model) {
         model.addAttribute("activePage", "categorie");
        model.addAttribute("categorie", new Categorie());
        return "personnel/categorie/add";
    }

    @PostMapping("/categorie/add")
    public String addCategorie(@ModelAttribute Categorie categorie, RedirectAttributes redirectAttributes) {
        try {
            categorieService.save(categorie);
            redirectAttributes.addFlashAttribute("success", "Catégorie ajoutée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la catégorie : " + e.getMessage());
        }
        return "redirect:/personnel/categorie/add";
    }
} 