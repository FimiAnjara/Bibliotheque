package com.bibliotheque.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.time.LocalTime;

@Configuration
public class BibliothequeConfig {
    @Value("${bibliotheque.heure.fermeture:16:00}")
    private String heureFermetureStr;

    public LocalTime getHeureFermeture() {
        return LocalTime.parse(heureFermetureStr);
    }
}
