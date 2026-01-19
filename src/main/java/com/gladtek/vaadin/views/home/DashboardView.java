package com.gladtek.vaadin.views.home;

import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;

import java.text.NumberFormat;
import java.util.Locale;

@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout implements LocaleChangeObserver {

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

        updateTexts();
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
        // Responsive sizing: Grow to fill, min-width to trigger wrap, width hint
        planetCard.getStyle().set("flex-grow", "1");
        planetCard.getStyle().set("min-width", "300px");
        planetCard.getStyle().set("width", "40%");

        populationKpiTitle = new H2();
        populationKpiTitle.getStyle().set("margin", "0").set("font-size", "1.2rem");

        populationKpiValue = new Span(NumberFormat.getInstance(Locale.ENGLISH).format(planetService.getTotalPopulation()));
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
        top5Layout.setPadding(false); // Grid handles padding

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

        top5Grid.addColumn(p -> getTranslation("planet.name." + p.name().toLowerCase().replace(" ", "_")))
                .setHeader("Name")
                .setKey("name");

        top5Grid.addColumn(p -> {
            long pop = planetService.parsePopulation(p.population());
            return NumberFormat.getInstance(Locale.ENGLISH).format(pop);
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

        Planet featured = planetService.getFeaturedPlanet();
        featuredName = new H2(getTranslation("planet.name." + featured.name().toLowerCase().replace(" ", "_")));

        HorizontalLayout climateRow = new HorizontalLayout();
        featuredClimateLabel = new Span();
        featuredClimateLabel.getStyle().set("font-weight", "bold");
        featuredClimateValue = new Span(translateList(featured.climate()));
        climateRow.add(featuredClimateLabel, new Span(": "), featuredClimateValue);

        HorizontalLayout terrainRow = new HorizontalLayout();
        featuredTerrainLabel = new Span();
        featuredTerrainLabel.getStyle().set("font-weight", "bold");
        featuredTerrainValue = new Span(translateList(featured.terrain()));
        terrainRow.add(featuredTerrainLabel, new Span(": "), featuredTerrainValue);

        layout.add(featuredTitle, featuredName, climateRow, terrainRow);
        return layout;
    }

    private void styleCard(VerticalLayout card) {
        card.setSpacing(true);
    }


    @Override
    public void localeChange(LocaleChangeEvent event) {
        updateTexts();
    }

    private void updateTexts() {
        planetKpiTitle.setText(getTranslation("dashboard.kpi.planets"));
        populationKpiTitle.setText(getTranslation("dashboard.kpi.population"));

        top5Title.setText(getTranslation("dashboard.top5.title"));
        top5Grid.getColumnByKey("name").setHeader(getTranslation("planets.col.name"));
        top5Grid.getColumnByKey("population").setHeader(getTranslation("planets.col.population"));
        top5Grid.getDataProvider().refreshAll();

        featuredTitle.setText(getTranslation("dashboard.featured.title"));
        featuredClimateLabel.setText(getTranslation("dashboard.featured.climate"));
        featuredTerrainLabel.setText(getTranslation("dashboard.featured.terrain"));

        Planet featured = planetService.getFeaturedPlanet();
        featuredName.setText(getTranslation("planet.name." + featured.name().toLowerCase().replace(" ", "_")));
        featuredClimateValue.setText(translateList(featured.climate()));
        featuredTerrainValue.setText(translateList(featured.terrain()));

        populationKpiValue.setText(NumberFormat.getInstance(Locale.ENGLISH).format(planetService.getTotalPopulation()));
    }

    private String translateList(String value) {
        if (value == null || value.isEmpty() || "unknown".equalsIgnoreCase(value)) {
            return getTranslation("planet.term.unknown");
        }
        return java.util.stream.Stream.of(value.split(","))
                .map(String::trim)
                .map(part -> getTranslation("planet.term." + part.toLowerCase().replace(" ", "_")))
                .collect(java.util.stream.Collectors.joining(", "));
    }
}
