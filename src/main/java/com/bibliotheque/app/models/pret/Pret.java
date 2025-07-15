package com.bibliotheque.app.models.pret;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import com.bibliotheque.app.models.utilisateur.Adherent;
import com.bibliotheque.app.models.utilisateur.Personnel;
import com.bibliotheque.app.models.bibliographie.Exemplaire;
import com.bibliotheque.app.models.pret.ProlongementPret;
import com.bibliotheque.app.models.suivi.StatutExemplaire;
import com.bibliotheque.app.models.suivi.Notification;

@Entity
@Table(name = "pret")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"adherent", "exemplaire", "prolongements", "reservation", "statutExemplaires", "notifications"})
public class Pret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_pret", nullable = false)
    private LocalDateTime datePret;

    @Column(name = "date_retour_prevu", nullable = false)
    private LocalDateTime dateRetourPrevu;

    @Column(name = "date_retour_effectuer")
    private LocalDateTime dateRetourEffectuer;

    @Convert(converter = TypePretConverter.class)
    @Column(name = "type_pret", nullable = false)
    private TypePret typePret;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adherent_id", nullable = false)
    private Adherent adherent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exemplaire_id", nullable = false)
    private Exemplaire exemplaire;

    @OneToMany(mappedBy = "pret", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProlongementPret> prolongements;

    @OneToMany(mappedBy = "pret", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatutExemplaire> statutExemplaires;

    @OneToMany(mappedBy = "pret", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    public enum TypePret {
        Domicile("Domicile"),
        sur_place("sur place");

        private final String label;

        TypePret(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static TypePret fromLabel(String label) {
            for (TypePret type : values()) {
                if (type.label.equalsIgnoreCase(label)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No enum constant for label: " + label);
        }
    }

    @Converter(autoApply = true)
    public static class TypePretConverter implements AttributeConverter<TypePret, String> {
        @Override
        public String convertToDatabaseColumn(TypePret attribute) {
            return attribute != null ? attribute.getLabel() : null;
        }

        @Override
        public TypePret convertToEntityAttribute(String dbData) {
            if (dbData == null) return null;
            return TypePret.fromLabel(dbData);
        }
    }
}