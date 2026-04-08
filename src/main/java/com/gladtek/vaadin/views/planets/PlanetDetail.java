package com.gladtek.vaadin.views.planets;

import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.Signal;

import java.text.NumberFormat;
import java.util.Locale;

public class PlanetDetail extends VerticalLayout {
    private final UserSession userSession;
    private final FormLayout formLayout;
    private final TextField name;
    private final TextField climate;
    private final TextField terrain;
    private final TextField population;
    private final Button closeButton;
    
    private Planet currentPlanet;

    public PlanetDetail(UserSession userSession) {
        this.userSession = userSession;
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        formLayout = new FormLayout();

        name = new TextField();
        name.setReadOnly(true);
        name.setWidthFull();

        climate = new TextField();
        climate.setReadOnly(true);
        climate.setWidthFull();

        terrain = new TextField();
        terrain.setReadOnly(true);
        terrain.setWidthFull();

        population = new TextField();
        population.setReadOnly(true);
        population.setWidthFull();

        formLayout.add(name, climate, terrain, population);
        closeButton = new Button();
        closeButton.addClickListener(
                event -> fireEvent(new CloseEvent(this, false)));
        add(formLayout, closeButton);

        // Signal Effect for labels and values
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            name.setLabel(getTranslation(l, "planet.detail.name"));
            climate.setLabel(getTranslation(l, "planet.detail.climate"));
            terrain.setLabel(getTranslation(l, "planet.detail.terrain"));
            population.setLabel(getTranslation(l, "planet.detail.population"));
            closeButton.setText(getTranslation(l, "planet.detail.close"));
            
            if (currentPlanet != null) {
                updateValuesForLocale(l);
            }
        });
    }

    public void setPlanet(Planet planet) {
        this.currentPlanet = planet;
        if (planet != null) {
            updateValuesForLocale(userSession.getLocaleSignal().peek());
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
    
    private void updateValuesForLocale(Locale l) {
        name.setValue(getTranslation(l, "planet.name." + currentPlanet.name().toLowerCase().replace(" ", "_")));
        climate.setValue(translateList(currentPlanet.climate(), l));
        terrain.setValue(translateList(currentPlanet.terrain(), l));
        
        if ("unknown".equalsIgnoreCase(currentPlanet.population())) {
            population.setValue(getTranslation(l, "planet.term.unknown"));
        } else {
            try {
                long pop = Long.parseLong(currentPlanet.population());
                Locale formatLocale = LanguageHelper.getFormattingLocale(l);
                population.setValue(NumberFormat.getInstance(formatLocale).format(pop));
            } catch (NumberFormatException e) {
                population.setValue(currentPlanet.population());
            }
        }
    }
    
    private String translateList(String value, Locale locale) {
        if (value == null || value.isEmpty() || "unknown".equalsIgnoreCase(value)) {
            return getTranslation(locale, "planet.term.unknown");
        }
        return java.util.stream.Stream.of(value.split(","))
                .map(String::trim)
                .map(part -> getTranslation(locale, "planet.term." + part.toLowerCase().replace(" ", "_")))
                .collect(java.util.stream.Collectors.joining(", "));
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
