package com.gladtek.vaadin.views.showcase.components;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class DisplaySection extends VerticalLayout {

    public DisplaySection() {
        setPadding(false);
        setSpacing(true);

        // Accordion
        Accordion accordion = new Accordion();
        accordion.add(getTranslation("components.accordion.panel1"), new Span(getTranslation("components.accordion.content1")));
        accordion.add(getTranslation("components.accordion.panel2"), new Span(getTranslation("components.accordion.content2")));

        // Tabs
        Tabs tabs = new Tabs();
        tabs.add(new Tab(getTranslation("components.tabs.tab1")), new Tab(getTranslation("components.tabs.tab2")), new Tab(getTranslation("components.tabs.tab3")));

        Grid<Person> grid = new Grid<>(Person.class, false);
        grid.addColumn(Person::getName).setHeader(getTranslation("components.grid.name"));
        grid.addColumn(Person::getAge).setHeader(getTranslation("components.grid.age"));
        grid.setItems(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 35)
        );
        grid.setHeight("200px");

        add(tabs, accordion, grid);
    }

    public static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
