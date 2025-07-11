package com.bibliotheque.app.models.pret;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.suivi.Notification;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateReservation;
    private LocalDateTime dateExpiration;
    private LocalDateTime dateSouhaiter;
    private LocalDateTime dateAnnulation;

    @ManyToOne
    @JoinColumn(name = "adherent_id")
    private Adherent adherent;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    private Exemplaire exemplaire;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Personnel admin;

    @OneToMany(mappedBy = "reservation")
    private List<StatutExemplaire> statutExemplaires;

    @OneToMany(mappedBy = "reservation")
    private List<Notification> notifications;
} 