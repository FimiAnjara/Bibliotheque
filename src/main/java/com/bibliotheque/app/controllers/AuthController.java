package com.bibliotheque.app.controllers;

import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.repositories.utilisateur.UtilisateurRepository;
import com.bibliotheque.app.repositories.utilisateur.AdherentRepository;
import com.bibliotheque.app.repositories.utilisateur.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private AdherentRepository adherentRepository;
    @Autowired
    private PersonnelRepository personnelRepository;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/personnel/home")
    public String home(Model model) {
        model.addAttribute("activePage", "home");
        return "personnel/home";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        Utilisateur user = utilisateurRepository.findByEmail(email).orElse(null);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                // Vérifier si Adherent
                Adherent adherent = adherentRepository.findById(user.getId()).orElse(null);
                if (adherent != null) {
                    session.setAttribute("user", user);
                    return "redirect:/adherent/home";
                }
                // Vérifier si Personnel
                Personnel personnel = personnelRepository.findById(user.getId()).orElse(null);
                if (personnel != null) {
                    session.setAttribute("user", user);
                    return "redirect:/personnel/home";
                }
                model.addAttribute("error", "Aucun profil associé à cet utilisateur.");
                return "redirect:/";
            } else {
                model.addAttribute("error", "Mot de passe incorrect.");
                return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Email inconnu.");
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}