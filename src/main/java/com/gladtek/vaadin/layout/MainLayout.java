package com.gladtek.vaadin.layout;


import com.gladtek.vaadin.components.LanguageSwitcher;
import com.gladtek.vaadin.components.SchemeToggle;
import com.gladtek.vaadin.security.LogoutView;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.gladtek.vaadin.views.home.DashboardView;
import com.gladtek.vaadin.views.home.SplitScreenView;
import com.gladtek.vaadin.views.planets.PlanetsView;
import com.gladtek.vaadin.views.showcase.ComponentsView;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteConfiguration;

import java.util.Locale;


public class MainLayout extends AppLayout implements LocaleChangeObserver, BeforeEnterObserver {

    private final UserSession userSession;
    private final Locale currentLocale;
    private H1 logo;
    private SideNavItem dashboardItem;
    private SideNavItem planetsItem;
    private SideNavItem componentsItem;
    private SideNavItem logoutItem;

    public MainLayout(UserSession userSession) {

        final UI ui = UI.getCurrent();
        currentLocale = ui.getLocale();
        this.userSession = userSession;
        createHeader();
        createDrawer();
    }


    private void createHeader() {
        logo = new H1(getTranslation("app.title"));

        SchemeToggle schemeToggle = new SchemeToggle(userSession);

        LanguageSwitcher languageSwitcher = new LanguageSwitcher();

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, schemeToggle, languageSwitcher);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");

        addToNavbar(header);
    }

    private void createDrawer() {

        SideNav nav = new SideNav();
        dashboardItem = new SideNavItem(getTranslation("nav.dashboard"), DashboardView.class, VaadinIcon.DASHBOARD.create());
        planetsItem = new SideNavItem(getTranslation("nav.planets"), PlanetsView.class, VaadinIcon.GLOBE.create());
        componentsItem = new SideNavItem(getTranslation("nav.components"), ComponentsView.class, VaadinIcon.CUBES.create());

        nav.addItem(dashboardItem);
        nav.addItem(planetsItem);
        nav.addItem(componentsItem);

        SideNav footerNav = new SideNav();
        logoutItem = new SideNavItem(getTranslation("nav.logout"), LogoutView.class, VaadinIcon.SIGN_OUT.create());
        footerNav.addItem(logoutItem);
        footerNav.setWidthFull();

        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller, footerNav);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (userSession.getSelectedSide() == null) {
            String path = event.getLocation().getPathWithQueryParameters();
            // Avoid saving the redirect target itself or loop
            if (!path.isEmpty() && !path.equals(RouteConfiguration.forSessionScope().getUrl(SplitScreenView.class))) {
                userSession.setIntendedRoute(path);
            }
            event.rerouteTo(SplitScreenView.class);
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        if (LanguageHelper.isRtl(localeChangeEvent.getLocale())) {
            localeChangeEvent.getUI().setDirection(Direction.RIGHT_TO_LEFT);
        } else {
            localeChangeEvent.getUI().setDirection(Direction.LEFT_TO_RIGHT);
        }
        if (!currentLocale.equals(localeChangeEvent.getLocale())) {
            UI.getCurrent().getSession().setLocale(localeChangeEvent.getLocale());
            UI.getCurrent().getPage().reload();
        }

    }

}
