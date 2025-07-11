package com.bibliotheque.app.models.annexe;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomEvenement;

    @Embedded
    private JourMois dateDebut;

    private Integer dureeJours;
    private LocalDateTime dateCreation;
    private String description;

    @OneToMany(mappedBy = "ferie")
    private List<SuiviFeries> suivis;
} 