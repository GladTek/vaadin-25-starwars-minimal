package com.gladtek.vaadin.services;

import com.gladtek.vaadin.models.Planet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.vaadin.flow.signals.shared.SharedValueSignal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanetService {

    private final List<Planet> planets = Arrays.asList(
            new Planet("Tatooine", "Arid", "Desert", new SharedValueSignal<>("200000"), new SharedValueSignal<>(0)),
            new Planet("Alderaan", "Temperate", "Grasslands, Mountains", new SharedValueSignal<>("2000000000"), new SharedValueSignal<>(0)),
            new Planet("Yavin IV", "Temperate, Tropical", "Jungle, Rainforests", new SharedValueSignal<>("1000"), new SharedValueSignal<>(0)),
            new Planet("Hoth", "Frozen", "Tundra, Ice Caves", new SharedValueSignal<>("Unknown"), new SharedValueSignal<>(0)),
            new Planet("Dagobah", "Murky", "Swamp, Jungles", new SharedValueSignal<>("Unknown"), new SharedValueSignal<>(0)),
            new Planet("Bespin", "Temperate", "Gas Giant", new SharedValueSignal<>("6000000"), new SharedValueSignal<>(0)),
            new Planet("Endor", "Temperate", "Forests, Mountains, Lakes", new SharedValueSignal<>("Unknown"), new SharedValueSignal<>(0)),
            new Planet("Naboo", "Temperate", "Grassy Hills, Swamps, Forests, Mountains", new SharedValueSignal<>("4500000000"), new SharedValueSignal<>(0)),
            new Planet("Coruscant", "Temperate", "Cityscape, Mountains", new SharedValueSignal<>("1000000000000"), new SharedValueSignal<>(0)),
            new Planet("Kamino", "Temperate", "Ocean", new SharedValueSignal<>("1000000000"), new SharedValueSignal<>(0))
    );

    private final SharedValueSignal<Long> totalPopulationSignal = new SharedValueSignal<>(calculateBaseTotalPopulation());
    private final SharedValueSignal<Integer> totalPopulationTrendSignal = new SharedValueSignal<>(0);

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
