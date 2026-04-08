package com.gladtek.vaadin.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.Locale;

public class LanguageSwitcher extends Select<Locale> {

    public LanguageSwitcher(UserSession userSession) {
        super();

        Locale arabic = Locale.forLanguageTag("ar");

        setItems(Locale.ENGLISH, arabic, Locale.FRENCH, Locale.GERMAN);
        setRenderer(new ComponentRenderer<>(locale -> {
            Span span = new Span(getLabel(locale, arabic));
            return span;
        }));


        Locale current = UI.getCurrent().getLocale();
        if (current == null) {
            current = Locale.ENGLISH;
        }
        setValue(current);

        addValueChangeListener(event -> {
            Locale selectedLocale = event.getValue();
            if (selectedLocale != null) {
                // Update modern Signal - UI direction is now handled reactively by a Signal.effect
                userSession.getLocaleSignal().set(selectedLocale);
                
                // Update legacy framework locale
                UI.getCurrent().setLocale(selectedLocale);
                UI.getCurrent().getSession().setLocale(selectedLocale);
            }
        });

        setWidth("120px");
    }

    private String getLabel(Locale locale, Locale arabic) {
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
    }
}
