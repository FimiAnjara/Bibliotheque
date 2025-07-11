package com.bibliotheque.app.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.suivi.Penalite;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.pret.ProlongementPret;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Personnel {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utilisateur utilisateur;

    private LocalDateTime dateEmbauche;

    @Column(unique = true, nullable = false)
    private String matricule;

    private String status;

    @OneToMany(mappedBy = "admin")
    private List<Pret> prets;

    @OneToMany(mappedBy = "admin")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "admin")
    private List<Penalite> penalites;

    @OneToMany(mappedBy = "admin")
    private List<StatutExemplaire> statutExemplaires;

    @OneToMany(mappedBy = "admin")
    private List<ProlongementPret> prolongementPrets;
} 