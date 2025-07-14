package com.bibliotheque.app.models.suivi;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "type_penalite")
@ToString(exclude = "penalites")
public class TypePenalite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String description;
    private Integer dureeJours;
    private Integer retardJours;

    @OneToMany(mappedBy = "typePenalite")
    private List<Penalite> penalites;
} 