package com.bibliotheque.app.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.gestion.Abonnement;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.suivi.Penalite;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"profil", "abonnements", "prets", "reservations", "penalites"})
public class Adherent extends Utilisateur {

    @Column(unique = true, nullable = false)
    private String numeroAdherent;

    private LocalDateTime dateInscription;

    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;

    @OneToMany(mappedBy = "adherent")
    private List<Abonnement> abonnements;

    @OneToMany(mappedBy = "adherent")
    private List<Pret> prets;

    @OneToMany(mappedBy = "adherent")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "adherent")
    private List<Penalite> penalites;
} 