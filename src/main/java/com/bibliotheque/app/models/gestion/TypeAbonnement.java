package com.bibliotheque.app.models.gestion;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeAbonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String libelle;
    private Integer dureeMois;
    private Double montant;
    private String conditions;

    @OneToMany(mappedBy = "typeAbonnement")
    private List<Abonnement> abonnements;
} 