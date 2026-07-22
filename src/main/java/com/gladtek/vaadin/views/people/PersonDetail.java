package com.gladtek.vaadin.views.people;

import com.gladtek.vaadin.models.Person;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.views.planets.PlanetsView;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PersonDetail extends VerticalLayout {
    private final UserSession userSession;
    private final PlanetService planetService;
    private final Avatar avatar;
    private final FormLayout formLayout;
    private final TextField name;
    private final TextField gender;
    private final TextField birthYear;
    private final TextField homeworld;
    private final Button viewPlanetButton;
    private final Button closeButton;

    private final ValueSignal<Person> selectedPerson = new ValueSignal<>(null);

    public PersonDetail(UserSession userSession, PlanetService planetService) {
        this.userSession = userSession;
        this.planetService = planetService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        avatar = new Avatar();
        avatar.setWidth("96px");
        avatar.setHeight("96px");

        formLayout = new FormLayout();

        name = new TextField();
        name.setReadOnly(true);
        name.setWidthFull();

        gender = new TextField();
        gender.setReadOnly(true);
        gender.setWidthFull();

        birthYear = new TextField();
        birthYear.setReadOnly(true);
        birthYear.setWidthFull();

        homeworld = new TextField();
        homeworld.setReadOnly(true);
        homeworld.setWidthFull();

        formLayout.add(name, gender, birthYear, homeworld);

        viewPlanetButton = new Button();
        viewPlanetButton.addThemeVariants(ButtonVariant.TERTIARY);
        viewPlanetButton.addClickListener(event -> {
            Person person = selectedPerson.peek();
            if (person != null) {
                UI.getCurrent().navigate(PlanetsView.class,
                        QueryParameters.simple(Map.of("planet", person.homeworld())));
            }
        });

        closeButton = new Button();
        closeButton.addClickListener(
                event -> fireEvent(new CloseEvent(this, false)));
        add(avatar, formLayout, viewPlanetButton, closeButton);

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            name.setLabel(getTranslation(l, "person.detail.name"));
            gender.setLabel(getTranslation(l, "person.detail.gender"));
            birthYear.setLabel(getTranslation(l, "person.detail.birth_year"));
            homeworld.setLabel(getTranslation(l, "person.detail.homeworld"));
            closeButton.setText(getTranslation(l, "person.detail.close"));

            Person person = selectedPerson.get();
            if (person != null) {
                String translatedName = getTranslation(l, "person.name." + person.name().toLowerCase().replace(" ", "_").replace("-", "_"));
                avatar.setName(translatedName);
                avatar.setImage(person.imageUrl());
                name.setValue(translatedName);
                gender.setValue(getTranslation(l, "person.gender." + person.gender().toLowerCase()));
                birthYear.setValue(person.birthYear());
                homeworld.setValue(person.homeworld());

                boolean hasPlanet = planetService.getPlanets().stream()
                        .anyMatch(planet -> planet.name().equalsIgnoreCase(person.homeworld()));
                viewPlanetButton.setVisible(hasPlanet);
                viewPlanetButton.setText(getTranslation(l, "person.detail.view_planet"));
            }
        });
    }

    public void setPerson(Person person) {
        selectedPerson.set(person);
        setVisible(person != null);
    }

    public Registration addCloseListener(
            ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public static class CloseEvent extends ComponentEvent<PersonDetail> {
        public CloseEvent(PersonDetail source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}
