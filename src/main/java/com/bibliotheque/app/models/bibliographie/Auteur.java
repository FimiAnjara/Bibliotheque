package com.bibliotheque.app.models.bibliographie;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private LocalDate dateDeces;
    private String nationalite;

    @OneToMany(mappedBy = "auteur")
    private List<Livre> livres;
} 