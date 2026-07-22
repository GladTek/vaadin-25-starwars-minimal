package com.gladtek.vaadin.services;

import tools.jackson.databind.ObjectMapper;
import com.gladtek.vaadin.models.Planet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.vaadin.flow.signals.shared.SharedValueSignal;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanetService {

    private record PlanetData(String name, String climate, String terrain, String population) {
    }

    private final List<Planet> planets;
    private final SharedValueSignal<Long> totalPopulationSignal;
    private final SharedValueSignal<Integer> totalPopulationTrendSignal = new SharedValueSignal<>(0);

    public PlanetService(ObjectMapper objectMapper) {
        this.planets = loadPlanets(objectMapper);
        this.totalPopulationSignal = new SharedValueSignal<>(calculateBaseTotalPopulation());
    }

    private static List<Planet> loadPlanets(ObjectMapper objectMapper) {
        try (InputStream in = new ClassPathResource("data/planets.json").getInputStream()) {
            List<PlanetData> data = objectMapper.readValue(in,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, PlanetData.class));
            return data.stream()
                    .map(d -> new Planet(d.name(), d.climate(), d.terrain(),
                            new SharedValueSignal<>(d.population()), new SharedValueSignal<>(0)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load planets.json", e);
        }
    }

    private long calculateBaseTotalPopulation() {
        return planets.stream().mapToLong(p -> parsePopulation(p.populationSignal().peek())).sum();
    }

    public SharedValueSignal<Long> getTotalPopulationSignal() {
        return totalPopulationSignal;
    }

    public SharedValueSignal<Integer> getTotalPopulationTrendSignal() {
        return totalPopulationTrendSignal;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    @Scheduled(fixedRate = 5000)
    public void simulatePopulationGrowth() {
        long currentTotal = 0;
        for (Planet p : planets) {
            String popStr = p.populationSignal().peek();
            if (!"Unknown".equalsIgnoreCase(popStr)) {
                try {
                    long pop = Long.parseLong(popStr);
                    // Add random variance between -1000 and 1000
                    long diff = (long) (Math.random() * 2000) - 1000;
                    if (pop + diff < 0) diff = -pop; // Prevent negative populations
                    long newPop = pop + diff;
                    
                    p.populationSignal().set(String.valueOf(newPop));
                    if (diff > 0) p.trendSignal().set(1);
                    else if (diff < 0) p.trendSignal().set(-1);
                    else p.trendSignal().set(0);
                    
                    currentTotal += newPop;
                } catch (NumberFormatException ignored) {}
            }
        }
        long previousTotal = totalPopulationSignal.peek();
        totalPopulationSignal.set(currentTotal);
        if (currentTotal > previousTotal) totalPopulationTrendSignal.set(1);
        else if (currentTotal < previousTotal) totalPopulationTrendSignal.set(-1);
        else totalPopulationTrendSignal.set(0);
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
        return totalPopulationSignal.peek();
    }

    public List<Planet> getTop5Populated() {
        return getPlanets().stream()
                .sorted((p1, p2) -> Long.compare(parsePopulation(p2.populationSignal().peek()), parsePopulation(p1.populationSignal().peek())))
                .limit(5)
                .collect(Collectors.toList());
    }

    public Planet getFeaturedPlanet() {
        // Return Tatooine as featured, or get the first one
        return getPlanets().stream()
                .filter(p -> "Tatooine".equalsIgnoreCase(p.name()))
                .findFirst()
                .orElse(getPlanets().get(0));
    }
}
