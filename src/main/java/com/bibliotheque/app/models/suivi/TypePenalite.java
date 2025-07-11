package com.bibliotheque.app.models.suivi;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypePenalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;
    private Integer dureeJours;

    @OneToMany(mappedBy = "typePenalite")
    private List<Penalite> penalites;
} 