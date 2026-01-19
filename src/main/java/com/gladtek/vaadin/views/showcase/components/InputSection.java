package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.utils.DatePickerI18nUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.LocalDate;

public class InputSection extends VerticalLayout {

    public InputSection() {
        setPadding(false);
        setSpacing(true);
        setWidthFull();

        FlexLayout layout = new FlexLayout();
        layout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        layout.getStyle().set("gap", "20px");
        layout.setWidthFull();

        TextField textField = new TextField(getTranslation("components.input.textfield"));
        styleField(textField);

        TextArea textArea = new TextArea(getTranslation("components.input.textarea"));
        styleField(textArea);

        PasswordField passwordField = new PasswordField(getTranslation("components.input.password"));
        styleField(passwordField);

        EmailField emailField = new EmailField(getTranslation("components.input.email"));
        styleField(emailField);

        DatePicker datePicker = new DatePicker(getTranslation("components.input.date"), LocalDate.now());
        DatePicker.DatePickerI18n i18n = DatePickerI18nUtil.getI18n(UI.getCurrent().getLocale());
        i18n.setToday(getTranslation("components.datepicker.today"));
        i18n.setCancel(getTranslation("components.datepicker.cancel"));
        datePicker.setI18n(i18n);
        styleField(datePicker);

        TimePicker timePicker = new TimePicker(getTranslation("components.input.time"));
        styleField(timePicker);

        layout.add(textField, textArea, passwordField, emailField, datePicker, timePicker);
        add(layout);
    }

    private void styleField(com.vaadin.flow.component.Component field) {
        field.getStyle().set("flex-grow", "1");
    }
}
