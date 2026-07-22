package com.gladtek.vaadin.views.people;

import com.gladtek.vaadin.components.GridPaginator;
import com.gladtek.vaadin.components.PaginatedGridController;
import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.models.Person;
import com.gladtek.vaadin.services.PersonService;
import com.gladtek.vaadin.services.PlanetService;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.masterdetaillayout.MasterDetailLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;

import java.util.List;
import java.util.Locale;

@Route(value = "people", layout = MainLayout.class)
public class PeopleView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver {

    private final UserSession userSession;
    private final PersonService personService;
    private final PersonDetail personDetail;
    private final H2 title;
    private final TextField searchField;
    private final Grid<Person> grid;
    private final GridPaginator paginator;
    private final PaginatedGridController<Person> gridController;

    public PeopleView(UserSession userSession, PersonService personService, PlanetService planetService) {
        this.userSession = userSession;
        this.personService = personService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);

        title = new H2();

        searchField = new TextField();
        searchField.setClearButtonVisible(true);
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setWidth("300px");
        searchField.getStyle().set("margin-bottom", "1rem");

        grid = new Grid<>(Person.class);
        grid.removeAllColumns();

        grid.addColumn(new com.vaadin.flow.data.renderer.ComponentRenderer<>(person -> {
                    Avatar avatar = new Avatar(translateName(person, userSession.getLocaleSignal().peek()));
                    avatar.setImage(person.imageUrl());
                    return avatar;
                }))
                .setHeader("")
                .setKey("avatar")
                .setSortable(false)
                .setAutoWidth(true)
                .setFlexGrow(0);

        grid.addColumn(person -> translateName(person, userSession.getLocaleSignal().peek()))
                .setHeader("Name")
                .setKey("name")
                .setComparator((p1, p2) -> translateName(p1, userSession.getLocaleSignal().peek())
                        .compareToIgnoreCase(translateName(p2, userSession.getLocaleSignal().peek())));

        grid.addColumn(person -> translateGender(person, userSession.getLocaleSignal().peek()))
                .setHeader("Gender")
                .setKey("gender")
                .setSortable(false);

        grid.addColumn(Person::birthYear)
                .setHeader("Birth Year")
                .setKey("birthYear")
                .setComparator((p1, p2) -> p1.birthYear().compareToIgnoreCase(p2.birthYear()));

        grid.addColumn(Person::homeworld)
                .setHeader("Homeworld")
                .setKey("homeworld")
                .setComparator((p1, p2) -> p1.homeworld().compareToIgnoreCase(p2.homeworld()));

        grid.setColumnOrder(
                grid.getColumnByKey("avatar"),
                grid.getColumnByKey("name"),
                grid.getColumnByKey("gender"),
                grid.getColumnByKey("birthYear"),
                grid.getColumnByKey("homeworld")
        );

        paginator = new GridPaginator(10, List.of(5, 10, 25, 50));
        gridController = new PaginatedGridController<>(grid, paginator, personService.getPeople());

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

        personDetail = new PersonDetail(userSession, planetService);

        add(title, searchField, contentLayout, paginator);

        grid.asSingleSelect().addValueChangeListener(event -> {
            Person selected = event.getValue();
            if (selected != null) {
                personDetail.setPerson(selected);
                contentLayout.setDetail(personDetail);
            } else {
                contentLayout.setDetail(null);
            }
        });

        personDetail.addCloseListener(event -> grid.deselectAll());
        contentLayout.addBackdropClickListener(event -> grid.deselectAll());
        contentLayout.addDetailEscapePressListener(event -> grid.deselectAll());

        setupReactiveBindings();

        addAttachListener(e -> updatePageTitle(userSession.getLocaleSignal().peek()));
    }

    private void setupReactiveBindings() {
        title.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "people.title")));

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();

            searchField.setLabel(getTranslation(l, "people.search.label"));
            searchField.setPlaceholder(getTranslation(l, "people.search.placeholder"));

            paginator.setRtl(LanguageHelper.isRtl(l));

            grid.getColumnByKey("name").setHeader(getTranslation(l, "people.col.name"));
            grid.getColumnByKey("gender").setHeader(getTranslation(l, "people.col.gender"));
            grid.getColumnByKey("birthYear").setHeader(getTranslation(l, "people.col.birth_year"));
            grid.getColumnByKey("homeworld").setHeader(getTranslation(l, "people.col.homeworld"));

            applyFilter();

            updatePageTitle(l);
        });
    }

    private void applyFilter() {
        Locale l = userSession.getLocaleSignal().peek();
        String filter = searchField.getValue();
        if (filter == null || filter.isBlank()) {
            gridController.setFilter(person -> true);
        } else {
            String needle = filter.trim().toLowerCase(l);
            gridController.setFilter(person ->
                    translateName(person, l).toLowerCase(l).contains(needle)
                    || person.homeworld().toLowerCase(l).contains(needle));
        }
    }

    private void updatePageTitle(Locale l) {
        getUI().ifPresent(ui -> ui.getPage().setTitle(getTranslation(l, "nav.people") + " - " + getTranslation(l, "app.title")));
    }

    private String translateName(Person person, Locale locale) {
        return getTranslation(locale, "person.name." + person.name().toLowerCase().replace(" ", "_").replace("-", "_"));
    }

    private String translateGender(Person person, Locale locale) {
        return getTranslation(locale, "person.gender." + person.gender().toLowerCase());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        List<String> personParam = event.getLocation().getQueryParameters().getParameters().get("person");
        if (personParam != null && !personParam.isEmpty()) {
            String name = personParam.get(0);
            personService.getPeople().stream()
                    .filter(person -> person.name().equalsIgnoreCase(name))
                    .findFirst()
                    .ifPresent(gridController::selectItem);
        }
    }

    @Override
    public String getPageTitle() {
        Locale l = userSession.getLocaleSignal().peek();
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "nav.people") + " - " + getTranslation(l, "app.title");
    }
}
