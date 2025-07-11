package com.bibliotheque.app.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.bibliotheque.app.models.gestion.ConfigurationQuota;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Integer quotaMax;
    private Integer dureeMaxPret;

    @OneToMany(mappedBy = "profil")
    private List<Adherent> adherents;

    @OneToMany(mappedBy = "profil")
    private List<ConfigurationQuota> configurationQuotas;
} 