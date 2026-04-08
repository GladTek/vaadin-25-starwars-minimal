package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class SelectionSection extends VerticalLayout {

    private final UserSession userSession;
    private final ComboBox<String> comboBox;
    private final Select<String> select;
    private final Checkbox checkbox;
    private final RadioButtonGroup<String> radioGroup;

    public SelectionSection(UserSession userSession) {
        this.userSession = userSession;
        setPadding(false);
        setSpacing(true);

        HorizontalLayout row1 = new HorizontalLayout();

        comboBox = new ComboBox<>();
        comboBox.setItems("components.select.option1", "components.select.option2", "components.select.option3");
        comboBox.setItemLabelGenerator(key -> getTranslation(userSession.getLocaleSignal().peek(), key));
        comboBox.setValue("components.select.option1");

        select = new Select<>();
        select.setItems("components.select.optionA", "components.select.optionB", "components.select.optionC");
        select.setRenderer(new ComponentRenderer<>(key -> {
            Span span = new Span(getTranslation(userSession.getLocaleSignal().peek(), key));
            return span;
        }));
        select.setValue("components.select.optionA");

        row1.add(comboBox, select);

        HorizontalLayout row2 = new HorizontalLayout();
        checkbox = new Checkbox();
        checkbox.setValue(true);

        radioGroup = new RadioButtonGroup<>();
        radioGroup.setItems("components.select.option1", "components.select.option2", "components.select.option3");
        radioGroup.setItemLabelGenerator(key -> getTranslation(userSession.getLocaleSignal().peek(), key));
        radioGroup.setValue("components.select.option1");

        row2.add(checkbox, radioGroup);

        add(row1, row2);

        // Reactive bindings
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            comboBox.setLabel(getTranslation(l, "components.select.combobox"));
            // Force redraw of items
            comboBox.getListDataView().refreshAll();

            select.setLabel(getTranslation(l, "components.select.select"));
            // Force redraw of renderer
            select.getListDataView().refreshAll();

            checkbox.setLabel(getTranslation(l, "components.select.checkbox"));

            radioGroup.setLabel(getTranslation(l, "components.select.radiogroup"));
            // Force redraw of items
            radioGroup.getListDataView().refreshAll();
        });
    }
}
