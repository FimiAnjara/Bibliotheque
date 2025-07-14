package com.bibliotheque.app.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.suivi.Penalite;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personnel extends Utilisateur {

    private LocalDateTime dateEmbauche;

    @Column(unique = true, nullable = false)
    private String matricule;

    private String status;

    @OneToMany(mappedBy = "admin")
    private List<Penalite> penalites;
} 