package com.gladtek.vaadin.services;

import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
@VaadinSessionScope
public class UserSession implements Serializable {

    private String selectedSide;
    private String intendedRoute;
    private String profileName; // Persist profile name across UI refreshes

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
}
