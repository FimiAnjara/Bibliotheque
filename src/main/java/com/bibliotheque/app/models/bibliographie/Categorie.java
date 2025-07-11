package com.bibliotheque.app.models.bibliographie;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Integer limitAge;

    @OneToMany(mappedBy = "categorie")
    private List<LivreCategorie> livreCategories;
} 