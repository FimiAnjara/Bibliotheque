package com.bibliotheque.app.controllers.suivi;

import com.bibliotheque.app.models.suivi.TypePenalite;
import com.bibliotheque.app.services.suivi.TypePenaliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/personnel/type-penalite")
public class TypePenaliteController {
    @Autowired
    private TypePenaliteService typePenaliteService;

    @GetMapping
    public String list(Model model) {
        List<TypePenalite> typePenalites = typePenaliteService.findAll();
        model.addAttribute("typePenalites", typePenalites);
        model.addAttribute("typePenaliteForm", new TypePenalite());
        return "personnel/type-penalite";
    }

    @PostMapping
    public String saveOrUpdate(@ModelAttribute("typePenaliteForm") TypePenalite typePenaliteForm, RedirectAttributes redirectAttributes) {
        try {
            typePenaliteService.save(typePenaliteForm);
            redirectAttributes.addFlashAttribute("success", typePenaliteForm.getId() == null ? "Type de pénalité ajouté !" : "Type de pénalité mis à jour !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/personnel/type-penalite";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        List<TypePenalite> typePenalites = typePenaliteService.findAll();
        TypePenalite typePenalite = typePenaliteService.findById(id).orElse(null);
        if (typePenalite == null) {
            typePenalite = new TypePenalite();
        }
        model.addAttribute("typePenalites", typePenalites);
        model.addAttribute("typePenaliteForm", typePenalite);
        return "personnel/type-penalite";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            typePenaliteService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Type de pénalité supprimé !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/personnel/type-penalite";
    }
} 