package com.bibliotheque.app.models.bibliographie;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Editeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String pays;
    private Integer anneeFondation;

    @OneToMany(mappedBy = "editeur")
    private List<Livre> livres;
} 