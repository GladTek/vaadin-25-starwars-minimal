package com.gladtek.vaadin.components;

import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.select.Select;

import java.util.Locale;

public class LanguageSwitcher extends Select<Locale> {

    public LanguageSwitcher() {
        super();

        Locale arabic = Locale.forLanguageTag("ar");

        setItems(Locale.ENGLISH, arabic, Locale.FRENCH, Locale.GERMAN);
        setItemLabelGenerator(locale -> {
            if (locale.equals(Locale.ENGLISH)) {
                return "English";
            } else if (locale.equals(arabic)) {
                return "العربية";
            } else if (locale.equals(Locale.FRENCH)) {
                return "Français";
            } else if (locale.equals(Locale.GERMAN)) {
                return "Deutsch";
            }
            return locale.getDisplayName();
        });


        Locale current = UI.getCurrent().getLocale();
        if (current == null) {
            current = Locale.ENGLISH;
        }
        setValue(current);

        addValueChangeListener(event -> {
            Locale selectedLocale = event.getValue();
            if (selectedLocale != null) {
                UI.getCurrent().setLocale(selectedLocale);
                if (LanguageHelper.isRtl(selectedLocale)) {
                    UI.getCurrent().setDirection(Direction.RIGHT_TO_LEFT);
                } else {
                    UI.getCurrent().setDirection(Direction.LEFT_TO_RIGHT);
                }
            }
        });

        setWidth("120px");
    }
}
