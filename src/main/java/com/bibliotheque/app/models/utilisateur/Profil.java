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

    @OneToMany(mappedBy = "profil")
    @ToString.Exclude
    private List<Adherent> adherents;

    @OneToMany(mappedBy = "profil")
    @ToString.Exclude
    private List<ConfigurationQuota> configurationQuotas;
} 