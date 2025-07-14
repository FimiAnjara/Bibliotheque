package com.bibliotheque.app.models.pret;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.utilisateur.Personnel;

@Entity
@Table(name = "prolongement_pret")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProlongementPret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_retour_prevu", nullable = false)
    private LocalDateTime dateRetourPrevu;

    @Column(name = "date_prolongement", nullable = false)
    private LocalDateTime dateProlongement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pret_id", nullable = false)
    private Pret pret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Personnel admin;
} 