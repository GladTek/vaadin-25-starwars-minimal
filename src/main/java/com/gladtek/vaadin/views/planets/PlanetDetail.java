package com.gladtek.vaadin.views.planets;

import com.gladtek.vaadin.models.Planet;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class PlanetDetail extends VerticalLayout {
    private final FormLayout formLayout;
    private final TextField name;
    private final TextField climate;
    private final TextField terrain;
    private final TextField population;
    private final Button closeButton;

    public PlanetDetail() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        formLayout = new FormLayout();

        name = new TextField(getTranslation("planet.detail.name"));
        name.setReadOnly(true);
        name.setWidthFull();

        climate = new TextField(getTranslation("planet.detail.climate"));
        climate.setReadOnly(true);
        climate.setWidthFull();

        terrain = new TextField(getTranslation("planet.detail.terrain"));
        terrain.setReadOnly(true);
        terrain.setWidthFull();

        population = new TextField(getTranslation("planet.detail.population"));
        population.setReadOnly(true);
        population.setWidthFull();

        formLayout.add(name, climate, terrain, population);
        closeButton = new Button(getTranslation("planet.detail.close"));
        closeButton.addClickListener(
                event -> fireEvent(new CloseEvent(this, false)));
        add(formLayout, closeButton);
    }

    public void setPlanet(Planet planet) {
        if (planet != null) {
            name.setValue(planet.name());
            climate.setValue(planet.climate());
            terrain.setValue(planet.terrain());
            population.setValue(planet.population());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public Registration addCloseListener(
            ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public static class CloseEvent extends ComponentEvent<PlanetDetail> {
        public CloseEvent(PlanetDetail source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}
