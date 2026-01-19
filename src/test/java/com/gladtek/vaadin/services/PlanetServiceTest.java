package com.gladtek.vaadin.services;

import com.gladtek.vaadin.models.Planet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanetServiceTest {

    private PlanetService planetService;

    @BeforeEach
    void setUp() {
        planetService = new PlanetService();
    }

    @Test
    void testGetPlanets_NotEmpty() {
        List<Planet> planets = planetService.getPlanets();
        assertNotNull(planets);
        assertFalse(planets.isEmpty());
    }

    @Test
    void testGetPlanets_ContainsTatooine() {
        List<Planet> planets = planetService.getPlanets();
        assertTrue(planets.stream()
                .anyMatch(p -> p.name().equals("Tatooine")));
    }

    @Test
    void testParsePopulation_ValidNumber() {
        long population = planetService.parsePopulation("2000000000");
        assertEquals(2000000000L, population);
    }

    @Test
    void testParsePopulation_WithCommas() {
        long population = planetService.parsePopulation("2,000,000,000");
        assertEquals(2000000000L, population);
    }

    @Test
    void testParsePopulation_Unknown() {
        long population = planetService.parsePopulation("unknown");
        assertEquals(0L, population);
    }

    @Test
    void testParsePopulation_Null() {
        long population = planetService.parsePopulation(null);
        assertEquals(0L, population);
    }

    @Test
    void testParsePopulation_Empty() {
        long population = planetService.parsePopulation("");
        assertEquals(0L, population);
    }

    @Test
    void testGetTotalPopulation_Positive() {
        long total = planetService.getTotalPopulation();
        assertTrue(total > 0);
    }

    @Test
    void testGetTop5Populated_ReturnsExactly5() {
        List<Planet> top5 = planetService.getTop5Populated();
        assertEquals(5, top5.size());
    }

    @Test
    void testGetTop5Populated_SortedDescending() {
        List<Planet> top5 = planetService.getTop5Populated();
        for (int i = 0; i < top5.size() - 1; i++) {
            long current = planetService.parsePopulation(top5.get(i).population());
            long next = planetService.parsePopulation(top5.get(i + 1).population());
            assertTrue(current >= next, "Top 5 should be sorted in descending order");
        }
    }

    @Test
    void testGetFeaturedPlanet_NotNull() {
        Planet featured = planetService.getFeaturedPlanet();
        assertNotNull(featured);
    }

    @Test
    void testGetFeaturedPlanet_IsTatooine() {
        Planet featured = planetService.getFeaturedPlanet();
        assertEquals("Tatooine", featured.name());
    }
}
