package com.gladtek.vaadin.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.ColorScheme;


public class SchemeToggle extends Button {

    public SchemeToggle(UserSession userSession) {
        super();

        addThemeVariants(ButtonVariant.TERTIARY);
        
        String initialTheme = userSession.getSelectedSide();
        if ("dark".equalsIgnoreCase(initialTheme)) {
            setIcon(VaadinIcon.SUN_O.create());
            setAriaLabel(getTranslation("nav.theme.light"));
            UI.getCurrent().getPage().setColorScheme(ColorScheme.Value.DARK);
        } else {
            setIcon(VaadinIcon.MOON.create());
            setAriaLabel(getTranslation("nav.theme.dark"));
            UI.getCurrent().getPage().setColorScheme(ColorScheme.Value.LIGHT);
        }

        addClickListener(e -> {
            var page = UI.getCurrent().getPage();
            if (e.getSource().getIcon().getElement().getAttribute("icon").equals("vaadin:sun-o")) {
                page.setColorScheme(ColorScheme.Value.LIGHT);
                e.getSource().setIcon(VaadinIcon.MOON.create());
                setAriaLabel(getTranslation("nav.theme.dark"));
                userSession.setSelectedSide("light");
            } else {
                page.setColorScheme(ColorScheme.Value.DARK);
                e.getSource().setIcon(VaadinIcon.SUN_O.create());
                setAriaLabel(getTranslation("nav.theme.light"));
                userSession.setSelectedSide("dark");
            }
        });
    }
}
