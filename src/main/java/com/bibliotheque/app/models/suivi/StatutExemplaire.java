package com.bibliotheque.app.models.suivi;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.pret.Reservation;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutExemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateChangement;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    private Exemplaire exemplaire;

    @ManyToOne
    private Integer statut;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Personnel admin;

    @ManyToOne
    @JoinColumn(name = "pret_id")
    private Pret pret;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
} 