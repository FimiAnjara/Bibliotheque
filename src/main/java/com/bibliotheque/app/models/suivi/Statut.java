package com.bibliotheque.app.models.suivi;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    @OneToMany(mappedBy = "statut")
    private List<StatutExemplaire> statutExemplaires;
} 