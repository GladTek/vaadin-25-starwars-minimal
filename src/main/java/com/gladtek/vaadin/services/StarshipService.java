package com.gladtek.vaadin.services;

import com.gladtek.vaadin.models.Starship;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class StarshipService {

    private final List<Starship> starships;

    public StarshipService(ObjectMapper objectMapper) {
        this.starships = loadStarships(objectMapper);
    }

    private static List<Starship> loadStarships(ObjectMapper objectMapper) {
        try (InputStream in = new ClassPathResource("data/starships.json").getInputStream()) {
            return objectMapper.readValue(in,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Starship.class));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load starships.json", e);
        }
    }

    public List<Starship> getStarships() {
        return starships;
    }

    public Starship getFeaturedStarship() {
        return starships.get(0);
    }
}
