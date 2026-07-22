package com.gladtek.vaadin.services;

import com.gladtek.vaadin.models.Person;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class PersonService {

    private final List<Person> people;

    public PersonService(ObjectMapper objectMapper) {
        this.people = loadPeople(objectMapper);
    }

    private static List<Person> loadPeople(ObjectMapper objectMapper) {
        try (InputStream in = new ClassPathResource("data/people.json").getInputStream()) {
            return objectMapper.readValue(in,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Person.class));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load people.json", e);
        }
    }

    public List<Person> getPeople() {
        return people;
    }

    public Person getFeaturedPerson() {
        return people.get(0);
    }
}
