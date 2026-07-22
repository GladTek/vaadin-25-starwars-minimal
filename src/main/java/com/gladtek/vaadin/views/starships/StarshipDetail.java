package com.gladtek.vaadin.views.starships;

import com.gladtek.vaadin.models.Starship;
import com.gladtek.vaadin.services.PersonService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.views.people.PeopleView;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.local.ValueSignal;

import java.util.Locale;
import java.util.Map;

public class StarshipDetail extends VerticalLayout {
    private final UserSession userSession;
    private final PersonService personService;
    private final FormLayout formLayout;
    private final TextField name;
    private final TextField starshipClass;
    private final TextField manufacturer;
    private final TextField crew;
    private final Button viewPilotButton;
    private final Button closeButton;

    private final ValueSignal<Starship> selectedStarship = new ValueSignal<>(null);

    public StarshipDetail(UserSession userSession, PersonService personService) {
        this.userSession = userSession;
        this.personService = personService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        formLayout = new FormLayout();

        name = new TextField();
        name.setReadOnly(true);
        name.setWidthFull();

        starshipClass = new TextField();
        starshipClass.setReadOnly(true);
        starshipClass.setWidthFull();

        manufacturer = new TextField();
        manufacturer.setReadOnly(true);
        manufacturer.setWidthFull();

        crew = new TextField();
        crew.setReadOnly(true);
        crew.setWidthFull();

        formLayout.add(name, starshipClass, manufacturer, crew);

        viewPilotButton = new Button();
        viewPilotButton.addThemeVariants(ButtonVariant.TERTIARY);
        viewPilotButton.addClickListener(event -> {
            Starship starship = selectedStarship.peek();
            if (starship != null && starship.pilot() != null && !starship.pilot().isBlank()) {
                UI.getCurrent().navigate(PeopleView.class,
                        QueryParameters.simple(Map.of("person", starship.pilot())));
            }
        });

        closeButton = new Button();
        closeButton.addClickListener(
                event -> fireEvent(new CloseEvent(this, false)));
        add(formLayout, viewPilotButton, closeButton);

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            name.setLabel(getTranslation(l, "starship.detail.name"));
            starshipClass.setLabel(getTranslation(l, "starship.detail.class"));
            manufacturer.setLabel(getTranslation(l, "starship.detail.manufacturer"));
            crew.setLabel(getTranslation(l, "starship.detail.crew"));
            closeButton.setText(getTranslation(l, "starship.detail.close"));

            Starship starship = selectedStarship.get();
            if (starship != null) {
                name.setValue(getTranslation(l, "starship.name." + starship.name().toLowerCase().replace(" ", "_")));
                starshipClass.setValue(getTranslation(l, "starship.class." + starship.starshipClass().toLowerCase().replace(" ", "_")));
                manufacturer.setValue(starship.manufacturer());
                crew.setValue(starship.crew());

                boolean hasPilot = starship.pilot() != null && !starship.pilot().isBlank()
                        && personService.getPeople().stream()
                                .anyMatch(person -> person.name().equalsIgnoreCase(starship.pilot()));
                viewPilotButton.setVisible(hasPilot);
                viewPilotButton.setText(getTranslation(l, "starship.detail.view_pilot"));
            }
        });
    }

    public void setStarship(Starship starship) {
        selectedStarship.set(starship);
        setVisible(starship != null);
    }

    public Registration addCloseListener(
            ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public static class CloseEvent extends ComponentEvent<StarshipDetail> {
        public CloseEvent(StarshipDetail source, boolean fromClient) {
            super(source, fromClient);
        }
    }
}
