package com.gladtek.vaadin.views.starships;

import com.gladtek.vaadin.components.GridPaginator;
import com.gladtek.vaadin.components.PaginatedGridController;
import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Starship;
import com.gladtek.vaadin.services.PersonService;
import com.gladtek.vaadin.services.StarshipService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;

import java.util.List;
import java.util.Locale;

@Route(value = "starships", layout = MainLayout.class)
public class StarshipsView extends VerticalLayout implements HasDynamicTitle {

    private final UserSession userSession;
    private final StarshipDetail starshipDetail;
    private final H2 title;
    private final TextField searchField;
    private final Grid<Starship> grid;
    private final GridPaginator paginator;
    private final PaginatedGridController<Starship> gridController;

    public StarshipsView(UserSession userSession, StarshipService starshipService, PersonService personService) {
        this.userSession = userSession;

        setSizeFull();
        setPadding(true);
        setSpacing(false);

        title = new H2();

        searchField = new TextField();
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setWidth("300px");
        searchField.getStyle().set("margin-bottom", "1rem");

        grid = new Grid<>(Starship.class);
        grid.removeAllColumns();

        grid.addColumn(starship -> translateName(starship, userSession.getLocaleSignal().peek()))
                .setHeader("Name")
                .setKey("name")
                .setComparator((s1, s2) -> translateName(s1, userSession.getLocaleSignal().peek())
                        .compareToIgnoreCase(translateName(s2, userSession.getLocaleSignal().peek())));

        grid.addColumn(starship -> translateClass(starship, userSession.getLocaleSignal().peek()))
                .setHeader("Class")
                .setKey("class")
                .setComparator((s1, s2) -> translateClass(s1, userSession.getLocaleSignal().peek())
                        .compareToIgnoreCase(translateClass(s2, userSession.getLocaleSignal().peek())));

        grid.addColumn(Starship::manufacturer)
                .setHeader("Manufacturer")
                .setKey("manufacturer")
                .setComparator((s1, s2) -> s1.manufacturer().compareToIgnoreCase(s2.manufacturer()));

        grid.addColumn(Starship::crew)
                .setHeader("Crew")
                .setKey("crew")
                .setComparator((s1, s2) -> Long.compare(parseCrew(s1.crew()), parseCrew(s2.crew())));

        grid.setColumnOrder(
                grid.getColumnByKey("name"),
                grid.getColumnByKey("class"),
                grid.getColumnByKey("manufacturer"),
                grid.getColumnByKey("crew")
        );

        paginator = new GridPaginator(10, List.of(5, 10, 25, 50));
        gridController = new PaginatedGridController<>(grid, paginator, starshipService.getStarships());

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

        starshipDetail = new StarshipDetail(userSession, personService);

        add(title, searchField, contentLayout, paginator);

        grid.asSingleSelect().addValueChangeListener(event -> {
            Starship selected = event.getValue();
            if (selected != null) {
                starshipDetail.setStarship(selected);
                contentLayout.setDetail(starshipDetail);
            } else {
                contentLayout.setDetail(null);
            }
        });

        starshipDetail.addCloseListener(event -> grid.deselectAll());
        contentLayout.addBackdropClickListener(event -> grid.deselectAll());
        contentLayout.addDetailEscapePressListener(event -> grid.deselectAll());

        setupReactiveBindings();

        addAttachListener(e -> updatePageTitle(userSession.getLocaleSignal().peek()));
    }

    private void setupReactiveBindings() {
        title.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "starships.title")));

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();

            searchField.setLabel(getTranslation(l, "starships.search.label"));
            searchField.setPlaceholder(getTranslation(l, "starships.search.placeholder"));

            paginator.setRtl(LanguageHelper.isRtl(l));

            grid.getColumnByKey("name").setHeader(getTranslation(l, "starships.col.name"));
            grid.getColumnByKey("class").setHeader(getTranslation(l, "starships.col.class"));
            grid.getColumnByKey("manufacturer").setHeader(getTranslation(l, "starships.col.manufacturer"));
            grid.getColumnByKey("crew").setHeader(getTranslation(l, "starships.col.crew"));

            applyFilter();

            updatePageTitle(l);
        });
    }

    private void applyFilter() {
        Locale l = userSession.getLocaleSignal().peek();
        String filter = searchField.getValue();
        if (filter == null || filter.isBlank()) {
            gridController.setFilter(starship -> true);
        } else {
            String needle = filter.trim().toLowerCase(l);
            gridController.setFilter(starship ->
                    translateName(starship, l).toLowerCase(l).contains(needle)
                    || translateClass(starship, l).toLowerCase(l).contains(needle)
                    || starship.manufacturer().toLowerCase(l).contains(needle));
        }
    }

    private void updatePageTitle(Locale l) {
        getUI().ifPresent(ui -> ui.getPage().setTitle(getTranslation(l, "nav.starships") + " - " + getTranslation(l, "app.title")));
    }

    private String translateName(Starship starship, Locale locale) {
        return getTranslation(locale, "starship.name." + starship.name().toLowerCase().replace(" ", "_"));
    }

    private String translateClass(Starship starship, Locale locale) {
        return getTranslation(locale, "starship.class." + starship.starshipClass().toLowerCase().replace(" ", "_"));
    }

    private long parseCrew(String crew) {
        if (crew == null || crew.isBlank()) {
            return 0;
        }
        try {
            return Long.parseLong(crew.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getPageTitle() {
        Locale l = userSession.getLocaleSignal().peek();
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "nav.starships") + " - " + getTranslation(l, "app.title");
    }
}
