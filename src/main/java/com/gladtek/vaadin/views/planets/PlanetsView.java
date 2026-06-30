package com.gladtek.vaadin.views.planets;

import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.signals.Signal;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "planets", layout = MainLayout.class)
public class PlanetsView extends VerticalLayout implements HasDynamicTitle {

    private final UserSession userSession;
    private final PlanetDetail planetDetail;
    private final H2 title;
    private final Grid<Planet> grid;

    public PlanetsView(UserSession userSession, PlanetService planetService) {
        this.userSession = userSession;

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

        grid.addComponentColumn(planet -> {
                    Span span = new Span();
                    span.bindText(Signal.computed(() -> {
                        String popStr = planet.populationSignal().get();
                        Locale l = userSession.getLocaleSignal().get();
                        if ("unknown".equalsIgnoreCase(popStr)) {
                            return getTranslation(l, "planet.term.unknown");
                        }
                        try {
                            long pop = Long.parseLong(popStr);
                            Locale formatLocale = LanguageHelper.getFormattingLocale(l);
                            return NumberFormat.getInstance(formatLocale).format(pop);
                        } catch (NumberFormatException e) {
                            return popStr;
                        }
                    }));
                    
                    Signal.effect(span, () -> {
                        int trend = planet.trendSignal().get();
                        if (trend > 0) span.getStyle().set("color", "green");
                        else if (trend < 0) span.getStyle().set("color", "red");
                        else span.getStyle().remove("color");
                    });
                    
                    return span;
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

        MasterDetailLayout contentLayout = new MasterDetailLayout();
        contentLayout.setSizeFull();
        contentLayout.setMasterSize("60%");
        contentLayout.setDetailSize("40%");
        contentLayout.setExpandMaster(true);
        contentLayout.setMaster(grid);

        planetDetail = new PlanetDetail(userSession);

        add(title, contentLayout);

        grid.asSingleSelect().addValueChangeListener(event -> {
            Planet selected = event.getValue();
            if (selected != null) {
                planetDetail.setPlanet(selected);
                contentLayout.setDetail(planetDetail);
            } else {
                contentLayout.setDetail(null);
            }
        });

        planetDetail.addCloseListener(event -> grid.deselectAll());
        contentLayout.addBackdropClickListener(event -> grid.deselectAll());
        contentLayout.addDetailEscapePressListener(event -> grid.deselectAll());

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
        return Stream.of(value.split(","))
                .map(String::trim)
                .map(part -> getTranslation(locale, "planet.term." + part.toLowerCase().replace(" ", "_")))
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getPageTitle() {
        Locale l = userSession.getLocaleSignal().peek();
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "nav.planets") + " - " + getTranslation(l, "app.title");
    }
}
