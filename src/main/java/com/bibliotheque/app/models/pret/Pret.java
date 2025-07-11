package com.bibliotheque.app.models.pret;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.suivi.Notification;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePret;
    private LocalDateTime dateRetourPrevu;
    private LocalDateTime dateRetourEffectuer;
    private String typePret;
    private String notes;
    private LocalDateTime dateValidation;
    private LocalDateTime dateAnnulation;

    @ManyToOne
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Personnel admin;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    private Exemplaire exemplaire;

    @OneToMany(mappedBy = "pret")
    private List<ProlongementPret> prolongements;

    @OneToMany(mappedBy = "pret")
    private List<StatutExemplaire> statutExemplaires;

    @OneToMany(mappedBy = "pret")
    private List<Notification> notifications;
} 