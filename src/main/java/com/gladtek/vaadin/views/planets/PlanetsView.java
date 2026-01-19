package com.gladtek.vaadin.views.planets;


import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Planet;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "planets", layout = MainLayout.class)
public class PlanetsView extends VerticalLayout {

    private final PlanetDetail planetDetail;
    private final H2 title;
    private final Grid<Planet> grid;

    public PlanetsView(UserSession userSession, PlanetService planetService) {


        setSizeFull();
        setPadding(false);
        setSpacing(false);

        title = new H2(getTranslation("planets.title"));


        grid = new Grid<>(Planet.class);
        grid.setItems(planetService.getPlanets());

        grid.removeAllColumns();


        grid.addColumn(planet -> getTranslation("planet.name." + planet.name().toLowerCase().replace(" ", "_")))
                .setHeader(getTranslation("planets.col.name"))
                .setKey("name");


        grid.addColumn(planet -> translateList(planet.climate()))
                .setHeader(getTranslation("planets.col.climate"))
                .setKey("climate");


        grid.addColumn(planet -> translateList(planet.terrain()))
                .setHeader(getTranslation("planets.col.terrain"))
                .setKey("terrain");


        grid.addColumn(planet -> {
                    if ("unknown".equalsIgnoreCase(planet.population())) {
                        return getTranslation("planet.term.unknown");
                    }
                    return planet.population();
                })
                .setHeader(getTranslation("planets.col.population"))
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

        planetDetail = new PlanetDetail();
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


        planetDetail
                .addCloseListener(event -> grid.deselectAll());
    }

    private String translateList(String value) {
        if (value == null || value.isEmpty() || "unknown".equalsIgnoreCase(value)) {
            return value;
        }
        return java.util.stream.Stream.of(value.split(","))
                .map(String::trim)
                .map(part -> getTranslation("planet.term." + part.toLowerCase().replace(" ", "_")))
                .collect(java.util.stream.Collectors.joining(", "));
    }
}
