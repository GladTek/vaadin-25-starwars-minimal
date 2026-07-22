package com.gladtek.vaadin.views.planets;

import com.gladtek.vaadin.components.GridPaginator;
import com.gladtek.vaadin.components.PaginatedGridController;
import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PersonService;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.signals.Signal;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "planets", layout = MainLayout.class)
public class PlanetsView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver {

    private final UserSession userSession;
    private final PlanetService planetService;
    private final PlanetDetail planetDetail;
    private final H2 title;
    private final TextField searchField;
    private final Grid<Planet> grid;
    private final GridPaginator paginator;
    private final PaginatedGridController<Planet> gridController;

    public PlanetsView(UserSession userSession, PlanetService planetService, PersonService personService) {
        this.userSession = userSession;
        this.planetService = planetService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);

        title = new H2();

        searchField = new TextField();
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setWidth("300px");
        searchField.getStyle().set("margin-bottom", "1rem");

        grid = new Grid<>(Planet.class);
        grid.removeAllColumns();

        grid.addColumn(planet -> getTranslation(userSession.getLocaleSignal().peek(), "planet.name." + planet.name().toLowerCase().replace(" ", "_")))
                .setHeader("Name")
                .setKey("name")
                .setComparator((p1, p2) -> translateName(p1, userSession.getLocaleSignal().peek())
                        .compareToIgnoreCase(translateName(p2, userSession.getLocaleSignal().peek())));

        grid.addColumn(planet -> translateList(planet.climate(), userSession.getLocaleSignal().peek()))
                .setHeader("Climate")
                .setKey("climate")
                .setComparator((p1, p2) -> translateList(p1.climate(), userSession.getLocaleSignal().peek())
                        .compareToIgnoreCase(translateList(p2.climate(), userSession.getLocaleSignal().peek())));

        grid.addColumn(planet -> translateList(planet.terrain(), userSession.getLocaleSignal().peek()))
                .setHeader("Terrain")
                .setKey("terrain")
                .setComparator((p1, p2) -> translateList(p1.terrain(), userSession.getLocaleSignal().peek())
                        .compareToIgnoreCase(translateList(p2.terrain(), userSession.getLocaleSignal().peek())));

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
                .setKey("population")
                .setComparator((p1, p2) -> Long.compare(
                        planetService.parsePopulation(p1.populationSignal().peek()),
                        planetService.parsePopulation(p2.populationSignal().peek())));

        grid.setColumnOrder(
                grid.getColumnByKey("name"),
                grid.getColumnByKey("climate"),
                grid.getColumnByKey("terrain"),
                grid.getColumnByKey("population")
        );

        paginator = new GridPaginator(10, List.of(5, 10, 25, 50));
        gridController = new PaginatedGridController<>(grid, paginator, planetService.getPlanets());

        searchField.addValueChangeListener(e -> {
            paginator.reset();
            applyFilter();
        });

        MasterDetailLayout contentLayout = new MasterDetailLayout();
        contentLayout.setSizeFull();
        contentLayout.setMasterSize("60%");
        contentLayout.setDetailSize("40%");
        contentLayout.setExpandMaster(true);
        contentLayout.setMaster(grid);

        planetDetail = new PlanetDetail(userSession, personService);

        add(title, searchField, contentLayout, paginator);

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
        addAttachListener(e -> updatePageTitle(userSession.getLocaleSignal().peek()));
    }

    private void setupReactiveBindings() {
        // Bind View Title
        title.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "planets.title")));

        // Signal Effect for Grid Headers, Data refresh and Page Title
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();

            searchField.setLabel(getTranslation(l, "planets.search.label"));
            searchField.setPlaceholder(getTranslation(l, "planets.search.placeholder"));

            paginator.setRtl(LanguageHelper.isRtl(l));

            grid.getColumnByKey("name").setHeader(getTranslation(l, "planets.col.name"));
            grid.getColumnByKey("climate").setHeader(getTranslation(l, "planets.col.climate"));
            grid.getColumnByKey("terrain").setHeader(getTranslation(l, "planets.col.terrain"));
            grid.getColumnByKey("population").setHeader(getTranslation(l, "planets.col.population"));

            applyFilter();
            updatePageTitle(l);
        });
    }

    private void applyFilter() {
        Locale l = userSession.getLocaleSignal().peek();
        String filter = searchField.getValue();
        if (filter == null || filter.isBlank()) {
            gridController.setFilter(planet -> true);
        } else {
            String needle = filter.trim().toLowerCase(l);
            gridController.setFilter(planet ->
                    translateName(planet, l).toLowerCase(l).contains(needle)
                    || translateList(planet.climate(), l).toLowerCase(l).contains(needle)
                    || translateList(planet.terrain(), l).toLowerCase(l).contains(needle));
        }
    }

    private void updatePageTitle(Locale l) {
        getUI().ifPresent(ui -> ui.getPage().setTitle(getTranslation(l, "nav.planets") + " - " + getTranslation(l, "app.title")));
    }

    private String translateName(Planet planet, Locale locale) {
        return getTranslation(locale, "planet.name." + planet.name().toLowerCase().replace(" ", "_"));
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
    public void beforeEnter(BeforeEnterEvent event) {
        List<String> planetParam = event.getLocation().getQueryParameters().getParameters().get("planet");
        if (planetParam != null && !planetParam.isEmpty()) {
            String name = planetParam.get(0);
            planetService.getPlanets().stream()
                    .filter(planet -> planet.name().equalsIgnoreCase(name))
                    .findFirst()
                    .ifPresent(gridController::selectItem);
        }
    }

    @Override
    public String getPageTitle() {
        Locale l = userSession.getLocaleSignal().peek();
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "nav.planets") + " - " + getTranslation(l, "app.title");
    }
}
