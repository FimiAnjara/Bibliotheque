package com.bibliotheque.app.models.bibliographie;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import com.bibliotheque.app.models.gestion.Reparation;
import com.bibliotheque.app.models.pret.Reservation;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.suivi.StatutExemplaire;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exemplaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String reference;

    private LocalDate dateAcquisition;

    @ManyToOne
    @JoinColumn(name = "livre_id")
    private Livre livre;

    @OneToMany(mappedBy = "exemplaire")
    private List<Reparation> reparations;

    @OneToMany(mappedBy = "exemplaire")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "exemplaire")
    private List<Pret> prets;

    @OneToMany(mappedBy = "exemplaire")
    private List<StatutExemplaire> statutExemplaires;
} 