package com.bibliotheque.app.models.suivi;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.utilisateur.Adherent;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"admin", "typePenalite", "adherent"})
public class Penalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateApplication;
    private LocalDate dateFin;
    private String notes;
    private LocalDateTime dateAnnulation;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Personnel admin;

    @ManyToOne
    @JoinColumn(name = "type_penalite_id")
    private TypePenalite typePenalite;

    @ManyToOne
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;
} 