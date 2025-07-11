package com.bibliotheque.app.models.bibliographie;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LivreCategorieId.class)
public class LivreCategorie {
    @Id
    @ManyToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;

    @Id
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
} 