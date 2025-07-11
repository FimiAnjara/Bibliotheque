package com.bibliotheque.app.models.gestion;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.bibliotheque.app.models.utilisateur.Profil;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationQuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;

    private Integer dureeMaxPret;
    private Integer quotaPret;
    private Integer quotaReservation;
    private LocalDateTime dateConfiguration;
} 