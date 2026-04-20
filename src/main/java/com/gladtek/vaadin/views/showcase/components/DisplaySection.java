package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class DisplaySection extends VerticalLayout {

    private final AccordionPanel panel1;
    private final Span panel1Content;
    private final AccordionPanel panel2;
    private final Span panel2Content;

    private final Tab tab1;
    private final Tab tab2;
    private final Tab tab3;

    private final Grid<Person> grid;

    private final Badge pending;
    private final Badge confirmed;
    private final Badge warning;
    private final Badge denied;

    public DisplaySection(UserSession userSession) {
        setPadding(false);
        setSpacing(true);

        // Accordion
        Accordion accordion = new Accordion();
        panel1Content = new Span();
        panel1 = accordion.add("", panel1Content);
        
        panel2Content = new Span();
        panel2 = accordion.add("", panel2Content);

        // Tabs
        tab1 = new Tab();
        tab2 = new Tab();
        tab3 = new Tab();
        Tabs tabs = new Tabs();
        tabs.add(tab1, tab2, tab3);

        grid = new Grid<>(Person.class, false);
        // Bind column headers and content via Signal
        grid.addColumn(person -> getTranslation(userSession.getLocaleSignal().peek(), person.getNameKey()))
            .setHeader("Name")
            .setKey("name");
        grid.addColumn(Person::getAge).setHeader("Age").setKey("age");

        grid.setItems(
                new Person("components.sample.name.alice", 30),
                new Person("components.sample.name.bob", 25),
                new Person("components.sample.name.charlie", 35));
        grid.setHeight("200px");

        HorizontalLayout badges = new HorizontalLayout();
        pending = new Badge();
        
        confirmed = new Badge();
        confirmed.addThemeVariants(BadgeVariant.SUCCESS);

        warning = new Badge();
        warning.addThemeVariants(BadgeVariant.WARNING);

        denied = new Badge();
        denied.addThemeVariants(BadgeVariant.ERROR);
        badges.add(pending, confirmed, warning, denied);

        add(tabs, accordion, grid, badges);

        // Reactive bindings
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            // Accordion
            panel1.setSummaryText(getTranslation(l, "components.accordion.panel1"));
            panel1Content.setText(getTranslation(l, "components.accordion.content1"));
            panel2.setSummaryText(getTranslation(l, "components.accordion.panel2"));
            panel2Content.setText(getTranslation(l, "components.accordion.content2"));
            
            // Tabs
            tab1.setLabel(getTranslation(l, "components.tabs.tab1"));
            tab2.setLabel(getTranslation(l, "components.tabs.tab2"));
            tab3.setLabel(getTranslation(l, "components.tabs.tab3"));
            
            // Grid
            grid.getColumnByKey("name").setHeader(getTranslation(l, "components.grid.name"));
            grid.getColumnByKey("age").setHeader(getTranslation(l, "components.grid.age"));
            grid.getDataProvider().refreshAll();
            
            // Badges
            pending.setText(getTranslation(l, "components.status.pending"));
            confirmed.setText(getTranslation(l, "components.status.confirmed"));
            warning.setText(getTranslation(l, "components.status.warning"));
            denied.setText(getTranslation(l, "components.status.denied"));
        });
    }

    public static class Person {
        private final String nameKey;
        private final int age;

        public Person(String nameKey, int age) {
            this.nameKey = nameKey;
            this.age = age;
        }

        public String getNameKey() {
            return nameKey;
        }

        public int getAge() {
            return age;
        }
    }
}
