package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.utils.DatePickerI18nUtil;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.slider.IntegerRangeSlider;
import com.vaadin.flow.component.slider.IntegerRangeSliderValue;
import com.vaadin.flow.component.slider.IntegerSlider;
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

    private final H3 slidersTitle;
    private final IntegerSlider slider;
    private final IntegerRangeSlider rangeSlider;
    private final IntegerSlider sliderMinMax;

    public InputSection(UserSession userSession) {
        setPadding(false);
        setSpacing(true);
        setWidthFull();

        VerticalLayout fieldsColumn = new VerticalLayout();
        fieldsColumn.setPadding(false);
        fieldsColumn.setSpacing(true);
        fieldsColumn.setWidth("50%");

        textField = new TextField();
        textField.setWidthFull();

        textArea = new TextArea();
        textArea.setWidthFull();

        passwordField = new PasswordField();
        passwordField.setWidthFull();

        emailField = new EmailField();
        emailField.setWidthFull();

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setWidthFull();

        timePicker = new TimePicker();
        timePicker.setWidthFull();

        fieldsColumn.add(textField, textArea, passwordField, emailField, datePicker, timePicker);

        VerticalLayout slidersColumn = new VerticalLayout();
        slidersColumn.setPadding(false);
        slidersColumn.setSpacing(true);
        slidersColumn.setWidth("50%");

        slidersTitle = new H3();

        slider = new IntegerSlider();
        slider.setValue(50);

        rangeSlider = new IntegerRangeSlider();
        rangeSlider.setMin(0);
        rangeSlider.setMax(1000);
        rangeSlider.setValue(new IntegerRangeSliderValue(200, 800));
        rangeSlider.getElement().setAttribute("role", "group");

        sliderMinMax = new IntegerSlider();
        sliderMinMax.setMin(0);
        sliderMinMax.setMax(100);
        sliderMinMax.setValue(50);
        sliderMinMax.setMinMaxVisible(true);

        slidersColumn.add(slidersTitle, slider, rangeSlider, sliderMinMax);

        HorizontalLayout columns = new HorizontalLayout(fieldsColumn, slidersColumn);
        columns.setWidthFull();
        columns.setSpacing(false);
        columns.getStyle().set("gap", "24px");
        columns.setFlexGrow(1, fieldsColumn);
        columns.setFlexGrow(1, slidersColumn);

        add(columns);

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

            slidersTitle.setText(getTranslation(l, "components.sliders.title"));
            slider.setLabel(getTranslation(l, "components.slider.volume"));
            rangeSlider.setLabel(getTranslation(l, "components.slider.price_range"));
            rangeSlider.getElement().setAttribute("aria-label", getTranslation(l, "components.slider.price_range_aria"));
            sliderMinMax.setLabel(getTranslation(l, "components.slider.temperature"));
        });
    }
}
