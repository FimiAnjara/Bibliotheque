package com.bibliotheque.app.models.gestion;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.bibliotheque.app.models.bibliographie.Exemplaire;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reparation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    private Exemplaire exemplaire;

    private LocalDate dateDebut;
    private LocalDate dateFinPrevue;
    private LocalDate dateFinReelle;
    private String description;
    private Double cout;
    private String technicien;
} 