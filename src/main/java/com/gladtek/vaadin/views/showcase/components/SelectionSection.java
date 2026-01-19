package com.gladtek.vaadin.views.showcase.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;

public class SelectionSection extends VerticalLayout {

    public SelectionSection() {
        setPadding(false);
        setSpacing(true);

        HorizontalLayout row1 = new HorizontalLayout();

        ComboBox<String> comboBox = new ComboBox<>(getTranslation("components.select.combobox"));
        comboBox.setItems(getTranslation("components.select.option1"), getTranslation("components.select.option2"), getTranslation("components.select.option3"));
        comboBox.setValue(getTranslation("components.select.option1"));

        Select<String> select = new Select<>();
        select.setLabel(getTranslation("components.select.select"));
        select.setItems(getTranslation("components.select.optionA"), getTranslation("components.select.optionB"), getTranslation("components.select.optionC"));
        select.setValue(getTranslation("components.select.optionA"));

        row1.add(comboBox, select);

        HorizontalLayout row2 = new HorizontalLayout();
        Checkbox checkbox = new Checkbox(getTranslation("components.select.checkbox"));
        checkbox.setValue(true);

        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel(getTranslation("components.select.radiogroup"));
        radioGroup.setItems(getTranslation("components.select.option1"), getTranslation("components.select.option2"), getTranslation("components.select.option3"));
        radioGroup.setValue(getTranslation("components.select.option1"));

        row2.add(checkbox, radioGroup);

        add(row1, row2);
    }
}
