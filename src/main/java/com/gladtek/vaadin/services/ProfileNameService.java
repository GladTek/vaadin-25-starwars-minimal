package com.gladtek.vaadin.services;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

/**
 * Service to generate random Star Wars personas based on alliance.
 */
@Service
public class ProfileNameService {

    private static final List<String> DARK_SIDE_NAMES = List.of(
            "Darth Vader", "Darth Maul", "Kylo Ren", "Emperor Palpatine", 
            "Boba Fett", "Stormtrooper 7", "General Grevious", "Captain Phasma",
            "Sith Lord", "Death Star Commander"
    );

    private static final List<String> LIGHT_SIDE_NAMES = List.of(
            "Luke Skywalker", "Yoda", "Obi-Wan Kenobi", "Han Solo", 
            "Princess Leia", "Chewbacca", "C-3PO", "R2-D2",
            "Jedi Master", "Rebel Commander"
    );

    private final Random random = new Random();

    public String generateRandomName(String side) {
        if ("dark".equalsIgnoreCase(side)) {
            return DARK_SIDE_NAMES.get(random.nextInt(DARK_SIDE_NAMES.size()));
        } else if ("light".equalsIgnoreCase(side)) {
            return LIGHT_SIDE_NAMES.get(random.nextInt(LIGHT_SIDE_NAMES.size()));
        }
        return "Unknown Traveler";
    }
}
