package com.gladtek.vaadin.services;

import com.vaadin.flow.signals.local.ValueSignal;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Locale;

@Service
@VaadinSessionScope
public class UserSession implements Serializable {

    private String selectedSide;
    private String intendedRoute;
    private String profileName;
    private final ValueSignal<Locale> localeSignal = new ValueSignal<>(Locale.ENGLISH);

    public String getSelectedSide() {
        return selectedSide;
    }

    public void setSelectedSide(String selectedSide) {
        this.selectedSide = selectedSide;
    }

    public String getIntendedRoute() {
        return intendedRoute;
    }

    public void setIntendedRoute(String intendedRoute) {
        this.intendedRoute = intendedRoute;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ValueSignal<Locale> getLocaleSignal() {
        return localeSignal;
    }
}
