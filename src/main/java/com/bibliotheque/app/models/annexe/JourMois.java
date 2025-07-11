package com.bibliotheque.app.models.annexe;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourMois {
    private Short jour;
    private Short mois;
} 