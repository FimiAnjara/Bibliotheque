package com.bibliotheque.app.models.bibliographie;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivreCategorieId implements Serializable {
    private Long livre;
    private Long categorie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LivreCategorieId that = (LivreCategorieId) o;
        return Objects.equals(livre, that.livre) && Objects.equals(categorie, that.categorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(livre, categorie);
    }
} 