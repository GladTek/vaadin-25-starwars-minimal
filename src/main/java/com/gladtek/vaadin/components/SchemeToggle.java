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
        
        boolean isDark = "dark".equalsIgnoreCase(userSession.getSelectedSide());
        applyTheme(isDark, userSession);

        addClickListener(e -> {
            boolean currentDark = "dark".equalsIgnoreCase(userSession.getSelectedSide());
            applyTheme(!currentDark, userSession);
        });
    }

    private void applyTheme(boolean isDark, UserSession userSession) {
        var page = UI.getCurrent().getPage();
        if (isDark) {
            setIcon(VaadinIcon.SUN.create());
            setAriaLabel(getTranslation("nav.theme.light"));
            page.setColorScheme(ColorScheme.Value.DARK);
            userSession.setSelectedSide("dark");
        } else {
            setIcon(VaadinIcon.MOON.create());
            setAriaLabel(getTranslation("nav.theme.dark"));
            page.setColorScheme(ColorScheme.Value.LIGHT);
            userSession.setSelectedSide("light");
        }
    }
}
