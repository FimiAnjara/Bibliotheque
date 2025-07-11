package com.bibliotheque.app.models.pret;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.utilisateur.Personnel;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProlongementPret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateRetourPrevu;
    private LocalDateTime dateProlongement;

    @ManyToOne
    @JoinColumn(name = "pret_id")
    private Pret pret;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Personnel admin;
} 