package com.bibliotheque.app.models.utilisateur;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import com.bibliotheque.app.models.suivi.Notification;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String email;
    private String address;
    private String password;
    private String telephone;

    @OneToOne(mappedBy = "utilisateur")
    private Adherent adherent;

    @OneToOne(mappedBy = "utilisateur")
    private Personnel personnel;

    @OneToMany(mappedBy = "utilisateur")
    private List<Notification> notifications;
} 