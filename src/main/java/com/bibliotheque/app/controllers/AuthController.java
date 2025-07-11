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

    @GetMapping("/adherent/home")
    public String adherentHome() {
        return "adherent/home";
    }

    @GetMapping("/personnel/home")
    public String personnelHome() {
        return "personnel/home";
    }
    
    

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();
            if (user.getPassword().equals(password)) {
                // Vérifier si Adherent
                Optional<Adherent> adherentOpt = adherentRepository.findById(user.getId());
                if (adherentOpt.isPresent()) {
                    session.setAttribute("user", user);
                    return "redirect:/adherent/home";
                }
                // Vérifier si Personnel
                Optional<Personnel> personnelOpt = personnelRepository.findById(user.getId());
                if (personnelOpt.isPresent()) {
                    session.setAttribute("user", user);
                    return "redirect:/personnel/home";
                }
                model.addAttribute("error", "Aucun profil associé à cet utilisateur.");
                return "login";
            } else {
                model.addAttribute("error", "Mot de passe incorrect.");
                return "login";
            }
        } else {
            model.addAttribute("error", "Email inconnu.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
} 