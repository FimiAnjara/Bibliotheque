package com.bibliotheque.app.models.bibliographie;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String isbn;
    private Integer anneePublication;
    private Integer nombrePages;
    private Integer limitAge;
    private String resume;

    @ManyToOne
    @JoinColumn(name = "editeur_id")
    private Editeur editeur;

    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private Auteur auteur;

    @OneToMany(mappedBy = "livre")
    private List<Exemplaire> exemplaires;

    @ManyToMany
    @JoinTable(
        name = "livre_categorie",
        joinColumns = @JoinColumn(name = "livre_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private Set<Categorie> categories = new HashSet<>();
} 