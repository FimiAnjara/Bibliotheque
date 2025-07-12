package com.bibliotheque.app.controllers.bibliographie;

import com.bibliotheque.app.models.bibliographie.Editeur;
import com.bibliotheque.app.services.bibliographie.EditeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personnel/editeur")
public class EditeurController {

    @Autowired
    private EditeurService editeurService;

    @GetMapping("/add")
    public String showAddEditeurForm(Model model) {
        model.addAttribute("activePage", "editeur");
        model.addAttribute("activeSubPage", "editeur-add");
        model.addAttribute("editeur", new Editeur());
        return "personnel/editeur/add";
    }

    @PostMapping("/add")
    public String addEditeur(@ModelAttribute Editeur editeur, RedirectAttributes redirectAttributes) {
        try {
            editeurService.save(editeur);
            redirectAttributes.addFlashAttribute("success", "Éditeur ajouté avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de l'éditeur : " + e.getMessage());
        }
        return "redirect:/personnel/editeur/add";
    }

    @GetMapping("/list")
    public String listEditeurs(Model model) {
        model.addAttribute("activePage", "editeur");
        model.addAttribute("activeSubPage", "editeur-list");
        model.addAttribute("editeurs", editeurService.findAll());
        return "personnel/editeur/list";
    }
} 