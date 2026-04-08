package com.gladtek.vaadin.views.home;

import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.signals.Signal;

import java.text.NumberFormat;
import java.util.Locale;

@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout implements HasDynamicTitle {

    private final UserSession userSession;
    private final PlanetService planetService;

    private H2 planetKpiTitle;
    private Span planetKpiValue;
    private H2 populationKpiTitle;
    private Span populationKpiValue;

    private H3 top5Title;
    private Grid<Planet> top5Grid;

    private H3 featuredTitle;
    private H2 featuredName;
    private Span featuredClimateLabel;
    private Span featuredTerrainLabel;
    private Span featuredClimateValue;
    private Span featuredTerrainValue;

    public DashboardView(UserSession userSession, PlanetService planetService) {
        this.userSession = userSession;
        this.planetService = planetService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        createKpiRow();
        createContentRow();

        // Reactive Bindings
        setupReactiveBindings();

        // Ensure title and grid are ready on arrival
        addAttachListener(e -> {
            updatePageTitle(userSession.getLocaleSignal().peek());
            top5Grid.getDataProvider().refreshAll();
        });
    }

    private void createKpiRow() {
        FlexLayout kpiRow = new FlexLayout();
        kpiRow.setWidthFull();
        kpiRow.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        kpiRow.getStyle().set("gap", "20px");

        planetKpiTitle = new H2();
        planetKpiTitle.getStyle().set("margin", "0").set("font-size", "1.2rem");
        planetKpiValue = new Span(String.valueOf(planetService.getPlanets().size()));
        planetKpiValue.getStyle().set("font-size", "2.5rem").set("font-weight", "bold");

        VerticalLayout planetCard = new VerticalLayout(planetKpiTitle, planetKpiValue);
        styleCard(planetCard);
        planetCard.getStyle().set("flex-grow", "1");
        planetCard.getStyle().set("min-width", "300px");
        planetCard.getStyle().set("width", "40%");

        populationKpiTitle = new H2();
        populationKpiTitle.getStyle().set("margin", "0").set("font-size", "1.2rem");

        populationKpiValue = new Span();
        populationKpiValue.getStyle().set("font-size", "2.5rem").set("font-weight", "bold");

        VerticalLayout popCard = new VerticalLayout(populationKpiTitle, populationKpiValue);
        styleCard(popCard);
        popCard.getStyle().set("flex-grow", "1");
        popCard.getStyle().set("min-width", "300px");
        popCard.getStyle().set("width", "40%");

        kpiRow.add(planetCard, popCard);
        add(kpiRow);
    }

    private void createContentRow() {
        FlexLayout contentRow = new FlexLayout();
        contentRow.setSizeFull();
        contentRow.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contentRow.getStyle().set("gap", "20px");

        VerticalLayout top5Layout = new VerticalLayout();
        styleCard(top5Layout);
        top5Layout.setPadding(false);

        top5Layout.add(createTop5Section());
        top5Layout.getStyle().set("flex-grow", "2");

        VerticalLayout featuredLayout = new VerticalLayout();
        styleCard(featuredLayout);
        featuredLayout.add(createFeaturedSection());
        featuredLayout.getStyle().set("flex-grow", "1");

        contentRow.add(top5Layout, featuredLayout);
        add(contentRow);
    }

    private VerticalLayout createTop5Section() {
        VerticalLayout layout = new VerticalLayout();

        top5Title = new H3();
        top5Grid = new Grid<>(Planet.class, false);
        top5Grid.setItems(planetService.getTop5Populated());
        top5Grid.setAllRowsVisible(true);

        top5Grid.addColumn(p -> getTranslation(userSession.getLocaleSignal().peek(), "planet.name." + p.name().toLowerCase().replace(" ", "_")))
                .setHeader("Name")
                .setKey("name");

        top5Grid.addComponentColumn(p -> {
            com.vaadin.flow.component.html.Span span = new com.vaadin.flow.component.html.Span();
            span.bindText(Signal.computed(() -> {
                String popStr = p.populationSignal().get();
                Locale l = userSession.getLocaleSignal().get();
                if ("unknown".equalsIgnoreCase(popStr)) {
                    return getTranslation(l, "planet.term.unknown");
                }
                long pop = planetService.parsePopulation(popStr);
                Locale formatLocale = LanguageHelper.getFormattingLocale(l);
                return NumberFormat.getInstance(formatLocale).format(pop);
            }));
            
            Signal.effect(span, () -> {
                int trend = p.trendSignal().get();
                if (trend > 0) span.getStyle().set("color", "green");
                else if (trend < 0) span.getStyle().set("color", "red");
                else span.getStyle().remove("color");
            });

            return span;
        }).setHeader("Population").setKey("population");

        layout.add(top5Title, top5Grid);
        return layout;
    }

    private VerticalLayout createFeaturedSection() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);

        featuredTitle = new H3();
        featuredName = new H2();

        HorizontalLayout climateRow = new HorizontalLayout();
        featuredClimateLabel = new Span();
        featuredClimateLabel.getStyle().set("font-weight", "bold");
        featuredClimateValue = new Span();
        climateRow.add(featuredClimateLabel, new Span(": "), featuredClimateValue);

        HorizontalLayout terrainRow = new HorizontalLayout();
        featuredTerrainLabel = new Span();
        featuredTerrainLabel.getStyle().set("font-weight", "bold");
        featuredTerrainValue = new Span();
        terrainRow.add(featuredTerrainLabel, new Span(": "), featuredTerrainValue);

        layout.add(featuredTitle, featuredName, climateRow, terrainRow);
        return layout;
    }

    private void setupReactiveBindings() {
        // KPI Titles
        planetKpiTitle.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "dashboard.kpi.planets")));
        populationKpiTitle.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "dashboard.kpi.population")));
        
        // KPI Values
        populationKpiValue.bindText(Signal.computed(() -> {
            Locale l = userSession.getLocaleSignal().get();
            long totalPop = planetService.getTotalPopulationSignal().get();
            Locale formatLocale = LanguageHelper.getFormattingLocale(l);
            return NumberFormat.getInstance(formatLocale).format(totalPop);
        }));

        Signal.effect(this, () -> {
            int trend = planetService.getTotalPopulationTrendSignal().get();
            if (trend > 0) populationKpiValue.getStyle().set("color", "green");
            else if (trend < 0) populationKpiValue.getStyle().set("color", "red");
            else populationKpiValue.getStyle().set("color", "black");
        });

        // Top 5 Section
        top5Title.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "dashboard.top5.title")));

        // Featured Section Labels
        featuredTitle.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "dashboard.featured.title")));
        featuredClimateLabel.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "dashboard.featured.climate")));
        featuredTerrainLabel.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "dashboard.featured.terrain")));

        // Featured Section Values
        Planet featured = planetService.getFeaturedPlanet();
        featuredName.bindText(userSession.getLocaleSignal().map(l -> 
            getTranslation(l, "planet.name." + featured.name().toLowerCase().replace(" ", "_"))
        ));
        featuredClimateValue.bindText(userSession.getLocaleSignal().map(l -> translateList(featured.climate(), l)));
        featuredTerrainValue.bindText(userSession.getLocaleSignal().map(l -> translateList(featured.terrain(), l)));

        // Signal Effect for Grid Headers, Data refresh and Page Title
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            // Update Grid Headers
            top5Grid.getColumnByKey("name").setHeader(getTranslation(l, "planets.col.name"));
            top5Grid.getColumnByKey("population").setHeader(getTranslation(l, "planets.col.population"));
            
            // Refresh Grid Data (for cell renderers)
            top5Grid.getDataProvider().refreshAll();
            
            // Update Browser Page Title
            updatePageTitle(l);
        });
    }

    private void updatePageTitle(Locale l) {
        getUI().ifPresent(ui -> ui.getPage().setTitle(getTranslation(l, "nav.dashboard") + " - " + getTranslation(l, "app.title")));
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

    private void styleCard(VerticalLayout card) {
        card.setSpacing(true);
        card.getStyle().set("background-color", "var(--lumo-base-color)");
        card.getStyle().set("border-radius", "var(--lumo-size-m)");
        card.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
        card.setPadding(true);
    }

    @Override
    public String getPageTitle() {
        Locale l = userSession.getLocaleSignal().peek();
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "nav.dashboard") + " - " + getTranslation(l, "app.title");
    }
}
