package com.bibliotheque.app.models.suivi;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.utilisateur.Utilisateur;
import com.bibliotheque.app.models.pret.Pret;
import com.bibliotheque.app.models.pret.Reservation;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"utilisateur", "pret", "reservation"})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private LocalDateTime dateCreation;

    @Column(name = "est_lu", nullable = false)
    private Boolean estLu = false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "pret_id")
    private Pret pret;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
} 