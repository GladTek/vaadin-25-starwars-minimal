package com.gladtek.vaadin.security;

import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.views.home.SplitScreenView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("logout")
public class LogoutView extends VerticalLayout implements BeforeEnterObserver {

    private final UserSession userSession;

    public LogoutView(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        userSession.setSelectedSide(null);
        event.forwardTo(SplitScreenView.class);
    }
}
