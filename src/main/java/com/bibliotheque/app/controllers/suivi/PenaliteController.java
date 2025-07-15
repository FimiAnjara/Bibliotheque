package com.bibliotheque.app.controllers.suivi;

import com.bibliotheque.app.services.suivi.PenaliteService;
import com.bibliotheque.app.models.suivi.Penalite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/personnel/penalites")
public class PenaliteController {
    @Autowired
    private PenaliteService penaliteService;

    @GetMapping("")
    public String listPenalites(Model model) {
        List<Penalite> penalites = penaliteService.findAll();
        model.addAttribute("penalites", penalites);
        return "personnel/penalites";
    }
} 