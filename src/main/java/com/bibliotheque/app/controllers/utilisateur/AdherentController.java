package com.bibliotheque.app.controllers.utilisateur;

import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.bibliographie.Livre;
import com.bibliotheque.app.services.utilisateur.AdherentService;
import com.bibliotheque.app.services.pret.PretService;
import com.bibliotheque.app.services.bibliographie.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/adherent")
public class AdherentController {
    
    @Autowired
    private AdherentService adherentService;
    
    @Autowired
    private PretService pretService;
    
    @Autowired
    private LivreService livreService;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
        if (adherentOpt.isPresent()) {
            Adherent adherent = adherentOpt.get();
            model.addAttribute("adherent", adherent);
            model.addAttribute("user", user);
            
            // Récupérer les emprunts en cours
            List<Pret> empruntsEnCours = pretService.findByAdherentAndDateRetourEffectuerIsNull(adherent);
            model.addAttribute("empruntsEnCours", empruntsEnCours);
            
            // Récupérer l'historique des emprunts
            List<Pret> historiqueEmprunts = pretService.findByAdherentOrderByDatePretDesc(adherent);
            model.addAttribute("historiqueEmprunts", historiqueEmprunts);
            
            // Créer une map pour stocker les informations de disponibilité des livres empruntés
            Map<Long, Map<String, Integer>> disponibiliteMap = new HashMap<>();
            for (Pret pret : empruntsEnCours) {
                Livre livre = pret.getExemplaire().getLivre();
                int exemplairesDisponibles = livreService.getNombreExemplairesDisponibles(livre);
                int totalExemplaires = livreService.getNombreTotalExemplaires(livre);
                
                Map<String, Integer> livreInfo = new HashMap<>();
                livreInfo.put("disponibles", exemplairesDisponibles);
                livreInfo.put("total", totalExemplaires);
                disponibiliteMap.put(livre.getId(), livreInfo);
            }
            model.addAttribute("disponibiliteMap", disponibiliteMap);
            
            return "adherent/home";
        }
        
        return "redirect:/";
    }

    @GetMapping("/livres")
    public String livres(Model model, HttpSession session, 
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String categorie,
                        @RequestParam(required = false) String auteur) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        model.addAttribute("user", user);
        
        List<Livre> livres;
        if (search != null && !search.trim().isEmpty()) {
            livres = livreService.searchLivres(search);
        } else if (categorie != null && !categorie.trim().isEmpty()) {
            livres = livreService.findByCategorie(categorie);
        } else if (auteur != null && !auteur.trim().isEmpty()) {
            livres = livreService.findByAuteur(auteur);
        } else {
            livres = livreService.findAll();
        }
        
        model.addAttribute("livres", livres);
        model.addAttribute("search", search);
        model.addAttribute("categorie", categorie);
        model.addAttribute("auteur", auteur);
        
        // Créer une map pour stocker les informations de disponibilité
        Map<Long, Map<String, Integer>> disponibiliteMap = new HashMap<>();
        for (Livre livre : livres) {
            int exemplairesDisponibles = livreService.getNombreExemplairesDisponibles(livre);
            int totalExemplaires = livreService.getNombreTotalExemplaires(livre);
            
            Map<String, Integer> livreInfo = new HashMap<>();
            livreInfo.put("disponibles", exemplairesDisponibles);
            livreInfo.put("total", totalExemplaires);
            disponibiliteMap.put(livre.getId(), livreInfo);
        }
        model.addAttribute("disponibiliteMap", disponibiliteMap);
        
        return "adherent/livres";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        
        Optional<Adherent> adherentOpt = adherentService.findById(user.getId());
        if (adherentOpt.isPresent()) {
            Adherent adherent = adherentOpt.get();
            model.addAttribute("adherent", adherent);
            model.addAttribute("user", user);
            
            // Récupérer les emprunts en cours
            List<Pret> empruntsEnCours = pretService.findByAdherentAndDateRetourEffectuerIsNull(adherent);
            model.addAttribute("empruntsEnCours", empruntsEnCours);
            
            // Récupérer l'historique des emprunts
            List<Pret> historiqueEmprunts = pretService.findByAdherentOrderByDatePretDesc(adherent);
            model.addAttribute("historiqueEmprunts", historiqueEmprunts);
            
            // Créer une map pour stocker les informations de disponibilité des livres empruntés
            Map<Long, Map<String, Integer>> disponibiliteMap = new HashMap<>();
            for (Pret pret : empruntsEnCours) {
                Livre livre = pret.getExemplaire().getLivre();
                int exemplairesDisponibles = livreService.getNombreExemplairesDisponibles(livre);
                int totalExemplaires = livreService.getNombreTotalExemplaires(livre);
                
                Map<String, Integer> livreInfo = new HashMap<>();
                livreInfo.put("disponibles", exemplairesDisponibles);
                livreInfo.put("total", totalExemplaires);
                disponibiliteMap.put(livre.getId(), livreInfo);
            }
            model.addAttribute("disponibiliteMap", disponibiliteMap);
            
            return "adherent/profile";
        }
        
        return "redirect:/";
    }
} 