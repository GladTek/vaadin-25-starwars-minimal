package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.utils.DatePickerI18nUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.signals.Signal;

import java.time.LocalDate;
import java.util.Locale;

public class InputSection extends VerticalLayout {

    private final TextField textField;
    private final TextArea textArea;
    private final PasswordField passwordField;
    private final EmailField emailField;
    private final DatePicker datePicker;
    private final TimePicker timePicker;

    public InputSection(UserSession userSession) {
        setPadding(false);
        setSpacing(true);
        setWidthFull();

        FlexLayout layout = new FlexLayout();
        layout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        layout.getStyle().set("gap", "20px");
        layout.setWidthFull();

        textField = new TextField();
        styleField(textField);

        textArea = new TextArea();
        styleField(textArea);

        passwordField = new PasswordField();
        styleField(passwordField);

        emailField = new EmailField();
        styleField(emailField);

        datePicker = new DatePicker(LocalDate.now());
        styleField(datePicker);

        timePicker = new TimePicker();
        styleField(timePicker);

        layout.add(textField, textArea, passwordField, emailField, datePicker, timePicker);
        add(layout);

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            textField.setLabel(getTranslation(l, "components.input.textfield"));
            textArea.setLabel(getTranslation(l, "components.input.textarea"));
            passwordField.setLabel(getTranslation(l, "components.input.password"));
            emailField.setLabel(getTranslation(l, "components.input.email"));
            
            datePicker.setLabel(getTranslation(l, "components.input.date"));
            DatePicker.DatePickerI18n i18n = DatePickerI18nUtil.getI18n(l);
            i18n.setToday(getTranslation(l, "components.datepicker.today"));
            i18n.setCancel(getTranslation(l, "components.datepicker.cancel"));
            datePicker.setI18n(i18n);

            timePicker.setLabel(getTranslation(l, "components.input.time"));
            timePicker.setLocale(l);
        });
    }

    private void styleField(Component field) {
        field.getStyle().set("flex-grow", "1");
    }
}
