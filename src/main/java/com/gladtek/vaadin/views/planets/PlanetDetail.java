package com.gladtek.vaadin.views.planets;

import com.gladtek.vaadin.models.Person;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PersonService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.gladtek.vaadin.views.people.PeopleView;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.local.ValueSignal;
import com.vaadin.flow.signals.Signal;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlanetDetail extends VerticalLayout {
    private final UserSession userSession;
    private final PersonService personService;
    private final FormLayout formLayout;
    private final TextField name;
    private final TextField climate;
    private final TextField terrain;
    private final TextField population;
    private final H4 residentsTitle;
    private final FlexLayout residentsList;
    private final Button closeButton;

    private final ValueSignal<Planet> selectedPlanet = new ValueSignal<>(null);

    public PlanetDetail(UserSession userSession, PersonService personService) {
        this.userSession = userSession;
        this.personService = personService;

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

        residentsTitle = new H4();
        residentsList = new FlexLayout();
        residentsList.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        residentsList.getStyle().set("gap", "12px");
        residentsList.setWidthFull();

        closeButton = new Button();
        closeButton.addClickListener(
                event -> fireEvent(new CloseEvent(this, false)));
        add(formLayout, residentsTitle, residentsList, closeButton);

        // Signal Effect for labels, planet details, and residents (does NOT depend on
        // the population/trend signals, so background population ticks don't rebuild
        // the resident cards or reload their images).
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            name.setLabel(getTranslation(l, "planet.detail.name"));
            climate.setLabel(getTranslation(l, "planet.detail.climate"));
            terrain.setLabel(getTranslation(l, "planet.detail.terrain"));
            population.setLabel(getTranslation(l, "planet.detail.population"));
            closeButton.setText(getTranslation(l, "planet.detail.close"));

            Planet planet = selectedPlanet.get();
            if (planet != null) {
                name.setValue(getTranslation(l, "planet.name." + planet.name().toLowerCase().replace(" ", "_")));
                climate.setValue(translateList(planet.climate(), l));
                terrain.setValue(translateList(planet.terrain(), l));

                List<Person> residents = personService.getPeople().stream()
                        .filter(person -> person.homeworld().equalsIgnoreCase(planet.name()))
                        .toList();

                residentsTitle.setText(getTranslation(l, "planet.detail.residents"));
                residentsTitle.setVisible(!residents.isEmpty());
                residentsList.setVisible(!residents.isEmpty());
                residentsList.removeAll();
                for (Person resident : residents) {
                    String residentName = getTranslation(l,
                            "person.name." + resident.name().toLowerCase().replace(" ", "_").replace("-", "_"));

                    Card residentCard = new Card();
                    residentCard.setWidth("160px");
                    residentCard.setTitle(residentName);
                    residentCard.setSubtitle(getTranslation(l, "person.gender." + resident.gender().toLowerCase()));

                    Image residentImage = new Image(resident.imageUrl(), residentName);
                    residentImage.setWidthFull();
                    residentImage.setHeight("160px");
                    residentImage.getStyle().set("object-fit", "cover");
                    residentCard.setMedia(residentImage);

                    Button viewButton = new Button(getTranslation(l, "planet.detail.view_resident"));
                    viewButton.addThemeVariants(ButtonVariant.PRIMARY);
                    viewButton.addClickListener(event -> UI.getCurrent().navigate(PeopleView.class,
                            QueryParameters.simple(Map.of("person", resident.name()))));
                    residentCard.addToFooter(viewButton);

                    residentsList.add(residentCard);
                }
            }
        });

        // Separate Signal Effect for the live population ticker, so it only updates
        // the population field text/color and never touches the residents cards.
        Signal.effect(this, () -> {
            Planet planet = selectedPlanet.get();
            if (planet == null) {
                return;
            }
            String popStr = planet.populationSignal().get();
            int trend = planet.trendSignal().get();
            Locale l = userSession.getLocaleSignal().peek();

            population.getStyle().remove("--vaadin-input-field-value-color");
            if (trend > 0) population.getStyle().set("--vaadin-input-field-value-color", "green");
            else if (trend < 0) population.getStyle().set("--vaadin-input-field-value-color", "red");

            if ("unknown".equalsIgnoreCase(popStr)) {
                population.setValue(getTranslation(l, "planet.term.unknown"));
            } else {
                try {
                    long pop = Long.parseLong(popStr);
                    Locale formatLocale = LanguageHelper.getFormattingLocale(l);
                    population.setValue(NumberFormat.getInstance(formatLocale).format(pop));
                } catch (NumberFormatException e) {
                    population.setValue(popStr);
                }
            }
        });
    }

    public void setPlanet(Planet planet) {
        selectedPlanet.set(planet);
        if (planet != null) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
    
    private String translateList(String value, Locale locale) {
        if (value == null || value.isEmpty() || "unknown".equalsIgnoreCase(value)) {
            return getTranslation(locale, "planet.term.unknown");
        }
        return Stream.of(value.split(","))
                .map(String::trim)
                .map(part -> getTranslation(locale, "planet.term." + part.toLowerCase().replace(" ", "_")))
                .collect(Collectors.joining(", "));
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
