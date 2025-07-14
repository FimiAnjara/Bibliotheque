package com.bibliotheque.app.models.pret;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.utilisateur.Personnel;

@Entity
@Table(name = "validation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Validation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "validation_status", nullable = false)
    private Boolean validationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pret_id")
    private Pret pret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prolongement_id")
    private ProlongementPret prolongement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Personnel admin;
} 