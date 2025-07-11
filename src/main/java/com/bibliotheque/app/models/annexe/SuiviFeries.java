package com.bibliotheque.app.models.annexe;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuiviFeries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer annee;
    private LocalDateTime dateValide;
    private String description;

    @ManyToOne
    @JoinColumn(name = "ferie_id")
    private Feries ferie;
} 