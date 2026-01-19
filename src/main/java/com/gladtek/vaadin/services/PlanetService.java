package com.gladtek.vaadin.services;

import com.gladtek.vaadin.models.Planet;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PlanetService {

    public List<Planet> getPlanets() {
        return Arrays.asList(
                new Planet("Tatooine", "Arid", "Desert", "200,000"),
                new Planet("Alderaan", "Temperate", "Grasslands, Mountains", "2,000,000,000"),
                new Planet("Yavin IV", "Temperate, Tropical", "Jungle, Rainforests", "1,000"),
                new Planet("Hoth", "Frozen", "Tundra, Ice Caves", "Unknown"),
                new Planet("Dagobah", "Murky", "Swamp, Jungles", "Unknown"),
                new Planet("Bespin", "Temperate", "Gas Giant", "6,000,000"),
                new Planet("Endor", "Temperate", "Forests, Mountains, Lakes", "Unknown"),
                new Planet("Naboo", "Temperate", "Grassy Hills, Swamps, Forests, Mountains", "4,500,000,000"),
                new Planet("Coruscant", "Temperate", "Cityscape, Mountains", "1,000,000,000,000"),
                new Planet("Kamino", "Temperate", "Ocean", "1,000,000,000")
        );
    }


    public long parsePopulation(String population) {
        if (population == null || population.isEmpty() || "unknown".equalsIgnoreCase(population)) {
            return 0;
        }
        try {
            return Long.parseLong(population.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long getTotalPopulation() {
        return getPlanets().stream()
                .mapToLong(p -> parsePopulation(p.population()))
                .sum();
    }

    public List<Planet> getTop5Populated() {
        return getPlanets().stream()
                .sorted((p1, p2) -> Long.compare(parsePopulation(p2.population()), parsePopulation(p1.population())))
                .limit(5)
                .collect(java.util.stream.Collectors.toList());
    }

    public Planet getFeaturedPlanet() {
        // Return Tatooine as featured, or get the first one
        return getPlanets().stream()
                .filter(p -> "Tatooine".equalsIgnoreCase(p.name()))
                .findFirst()
                .orElse(getPlanets().get(0));
    }
}
