package com.gladtek.vaadin.layout;

import com.gladtek.vaadin.components.AlliancePresence;
import com.gladtek.vaadin.components.LanguageSwitcher;
import com.gladtek.vaadin.components.SchemeToggle;
import com.gladtek.vaadin.security.LogoutView;
import com.gladtek.vaadin.services.AllianceRegistry;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.gladtek.vaadin.views.home.DashboardView;
import com.gladtek.vaadin.views.home.SplitScreenView;
import com.gladtek.vaadin.views.planets.PlanetsView;
import com.gladtek.vaadin.views.showcase.ComponentsView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private final UserSession userSession;
    private final AllianceRegistry allianceRegistry;
    private H1 logo;
    private SideNavItem dashboardItem;
    private SideNavItem planetsItem;
    private SideNavItem componentsItem;
    private SideNavItem logoutItem;
    private Span profileName;
    private SideNav nav;
    private SideNav footerNav;

    public MainLayout(UserSession userSession, AllianceRegistry allianceRegistry) {
        this.userSession = userSession;
        this.allianceRegistry = allianceRegistry;
        
        createHeader();
        createDrawer();
        
        // Universal Reactive Bindings
        setupReactiveBindings();
    }

    private void createHeader() {
        logo = new H1();
        logo.getStyle().set("white-space", "nowrap")
             .set("font-size", "var(--lumo-font-size-xl)")
             .set("margin", "0");

        SchemeToggle schemeToggle = new SchemeToggle(userSession);
        LanguageSwitcher languageSwitcher = new LanguageSwitcher(userSession);

        profileName = new Span();
        profileName.addClassName("profile-name");
        profileName.getStyle().set("font-weight", "bold")
                     .set("white-space", "nowrap")
                     .set("margin-left", "1rem")
                     .set("font-size", "0.95rem")
                     .set("flex-shrink", "0");
        
        AlliancePresence alliancePresence = new AlliancePresence(allianceRegistry);
        alliancePresence.getStyle().set("margin-right", "1rem");

        HorizontalLayout rightSide = new HorizontalLayout();
        rightSide.getStyle().set("margin-left", "auto");
        rightSide.setAlignItems(FlexComponent.Alignment.CENTER);
        rightSide.setPadding(false);
        rightSide.setSpacing(true);

        rightSide.add(alliancePresence, profileName, schemeToggle, languageSwitcher);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%");
        headerLayout.setPadding(true);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.add(new DrawerToggle(), logo, rightSide);

        Header header = new Header(headerLayout);
        header.setWidthFull();
        addToNavbar(header);
    }

    private void createDrawer() {
        nav = new SideNav();
        footerNav = new SideNav();
        
        dashboardItem = new SideNavItem("", DashboardView.class, VaadinIcon.DASHBOARD.create());
        planetsItem = new SideNavItem("", PlanetsView.class, VaadinIcon.GLOBE.create());
        componentsItem = new SideNavItem("", ComponentsView.class, VaadinIcon.CUBES.create());

        nav.addItem(dashboardItem);
        nav.addItem(planetsItem);
        nav.addItem(componentsItem);

        logoutItem = new SideNavItem("", LogoutView.class, VaadinIcon.SIGN_OUT.create());
        footerNav.addItem(logoutItem);
        footerNav.setWidthFull();

        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller, footerNav);
    }

    private void setupReactiveBindings() {
        // Bind Logo
        logo.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "app.title")));
        
        // Signal Effect for Global Labels & Direction
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            // SideNav Labels
            nav.setLabel(getTranslation(l, "nav.main"));
            footerNav.setLabel(getTranslation(l, "nav.footer"));
            
            // SideNav Items
            dashboardItem.setLabel(getTranslation(l, "nav.dashboard"));
            planetsItem.setLabel(getTranslation(l, "nav.planets"));
            componentsItem.setLabel(getTranslation(l, "nav.components"));
            logoutItem.setLabel(getTranslation(l, "nav.logout"));
            
            // Global UI Direction
            getUI().ifPresent(ui -> {
                Direction dir = LanguageHelper.isRtl(l) ? Direction.RIGHT_TO_LEFT : Direction.LEFT_TO_RIGHT;
                ui.setDirection(dir);
            });
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        String sessionId = ui.getSession().getSession().getId();
        profileName.bindText(allianceRegistry.getMyProfileName(sessionId));
    }

    @Override
    public void setContent(com.vaadin.flow.component.Component content) {
        if (content instanceof Main) {
            super.setContent(content);
        } else {
            Main main = new Main(content);
            main.setSizeFull();
            super.setContent(main);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String side = userSession.getSelectedSide();
        UI ui = event.getUI();
        
        if (side == null) {
            String path = event.getLocation().getPathWithQueryParameters();
            if (!path.isEmpty() && !path.equals(RouteConfiguration.forSessionScope().getUrl(SplitScreenView.class))) {
                userSession.setIntendedRoute(path);
            }
            event.rerouteTo(SplitScreenView.class);
        } else {
            String sessionId = ui.getSession().getSession().getId();
            allianceRegistry.registerOrUpdate(sessionId, side, userSession.getProfileName());
            
            String pageName = getPageTitleFor(event.getNavigationTarget(), userSession.getLocaleSignal().peek());
            allianceRegistry.updateCurrentPage(sessionId, pageName);
        }
    }

    private String getPageTitleFor(Class<?> viewClass, Locale locale) {
        if (viewClass == null) return "Galaxy Map";
        if (viewClass.equals(DashboardView.class)) return getTranslation(locale, "nav.dashboard");
        if (viewClass.equals(PlanetsView.class)) return getTranslation(locale, "nav.planets");
        if (viewClass.equals(ComponentsView.class)) return getTranslation(locale, "nav.components");
        return viewClass.getSimpleName();
    }

    @Override
    protected void onDetach(com.vaadin.flow.component.DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        UI ui = detachEvent.getUI();
        if (ui != null) {
            String sessionId = ui.getSession().getSession().getId();
            allianceRegistry.unregister(sessionId);
        }
    }
}
