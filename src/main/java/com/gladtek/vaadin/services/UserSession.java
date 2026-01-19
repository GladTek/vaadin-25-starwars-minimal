package com.gladtek.vaadin.services;


import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
public class UserSession {
    private String selectedSide;
    private String intendedRoute;

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
}
