package com.gladtek.vaadin.views.planets;

import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.signals.Signal;

import java.text.NumberFormat;
import java.util.Locale;

@Route(value = "planets", layout = MainLayout.class)
public class PlanetsView extends VerticalLayout implements HasDynamicTitle {

    private final UserSession userSession;
    private final PlanetService planetService;
    private final PlanetDetail planetDetail;
    private final H2 title;
    private final Grid<Planet> grid;

    public PlanetsView(UserSession userSession, PlanetService planetService) {
        this.userSession = userSession;
        this.planetService = planetService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        title = new H2();
        
        grid = new Grid<>(Planet.class);
        grid.setItems(planetService.getPlanets());
        grid.removeAllColumns();

        grid.addColumn(planet -> getTranslation(userSession.getLocaleSignal().peek(), "planet.name." + planet.name().toLowerCase().replace(" ", "_")))
                .setHeader("Name")
                .setKey("name");

        grid.addColumn(planet -> translateList(planet.climate(), userSession.getLocaleSignal().peek()))
                .setHeader("Climate")
                .setKey("climate");

        grid.addColumn(planet -> translateList(planet.terrain(), userSession.getLocaleSignal().peek()))
                .setHeader("Terrain")
                .setKey("terrain");

        grid.addColumn(planet -> {
                    if ("unknown".equalsIgnoreCase(planet.population())) {
                        return getTranslation(userSession.getLocaleSignal().peek(), "planet.term.unknown");
                    }
                    try {
                        long pop = Long.parseLong(planet.population());
                        Locale formatLocale = LanguageHelper.getFormattingLocale(userSession.getLocaleSignal().peek());
                        return NumberFormat.getInstance(formatLocale).format(pop);
                    } catch (NumberFormatException e) {
                        return planet.population();
                    }
                })
                .setHeader("Population")
                .setKey("population");

        grid.setColumnOrder(
                grid.getColumnByKey("name"),
                grid.getColumnByKey("climate"),
                grid.getColumnByKey("terrain"),
                grid.getColumnByKey("population")
        );

        grid.setSizeFull();

        FlexLayout contentLayout = new FlexLayout();
        contentLayout.setSizeFull();
        contentLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contentLayout.getStyle().set("gap", "20px");

        grid.getStyle().set("flex-grow", "2");
        grid.getStyle().set("flex-basis", "600px");
        grid.getStyle().set("min-width", "400px");

        planetDetail = new PlanetDetail(userSession);
        planetDetail.getStyle().set("flex-grow", "1");
        planetDetail.getStyle().set("flex-basis", "300px");
        planetDetail.getStyle().set("min-width", "300px");
        planetDetail.setVisible(false);

        contentLayout.add(grid, planetDetail);
        add(title, contentLayout);

        grid.asSingleSelect().addValueChangeListener(event -> {
            Planet selected = event.getValue();
            if (selected != null) {
                planetDetail.setPlanet(selected);
                planetDetail.setVisible(true);
            } else {
                planetDetail.setVisible(false);
            }
        });

        planetDetail.addCloseListener(event -> grid.deselectAll());

        // Reactive Bindings
        setupReactiveBindings();

        // Initial setup on attach
        addAttachListener(e -> {
            updatePageTitle(userSession.getLocaleSignal().peek());
            grid.getDataProvider().refreshAll();
        });
    }

    private void setupReactiveBindings() {
        // Bind View Title
        title.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "planets.title")));

        // Signal Effect for Grid Headers, Data refresh and Page Title
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            // Grid Headers
            grid.getColumnByKey("name").setHeader(getTranslation(l, "planets.col.name"));
            grid.getColumnByKey("climate").setHeader(getTranslation(l, "planets.col.climate"));
            grid.getColumnByKey("terrain").setHeader(getTranslation(l, "planets.col.terrain"));
            grid.getColumnByKey("population").setHeader(getTranslation(l, "planets.col.population"));
            
            // Refresh Grid Cells (cell renderers use peek() inside the effect's re-render cycle)
            grid.getDataProvider().refreshAll();
            
            // Update Browser Page Title
            updatePageTitle(l);
        });
    }

    private void updatePageTitle(Locale l) {
        getUI().ifPresent(ui -> ui.getPage().setTitle(getTranslation(l, "nav.planets") + " - " + getTranslation(l, "app.title")));
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

    @Override
    public String getPageTitle() {
        Locale l = userSession.getLocaleSignal().peek();
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "nav.planets") + " - " + getTranslation(l, "app.title");
    }
}
