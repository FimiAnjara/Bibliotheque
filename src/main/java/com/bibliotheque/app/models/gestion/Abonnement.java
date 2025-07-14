package com.bibliotheque.app.models.gestion;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.gestion.TypeAbonnement;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate datePaiement;
    private Double montantPaye;

    @ManyToOne
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "type_abonnement_id")
    private TypeAbonnement typeAbonnement;
} 