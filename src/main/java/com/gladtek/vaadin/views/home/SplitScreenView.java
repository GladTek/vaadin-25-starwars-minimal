package com.gladtek.vaadin.views.home;

import com.gladtek.vaadin.components.LanguageSwitcher;
import com.gladtek.vaadin.services.AllianceRegistry;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

@Route("")
public class SplitScreenView extends HorizontalLayout implements HasDynamicTitle {

    private final H1 darkTitle = new H1();
    private final Paragraph darkDesc = new Paragraph();
    private final H1 lightTitle = new H1();
    private final Paragraph lightDesc = new Paragraph();
    private final AllianceRegistry allianceRegistry;
    private final UserSession userSession;

    public SplitScreenView(UserSession userSession, AllianceRegistry allianceRegistry) {
        this.userSession = userSession;
        this.allianceRegistry = allianceRegistry;
        setSizeFull();
        setSpacing(false);
        setPadding(false);

        Main mainContent = new Main();
        mainContent.setSizeFull();
        mainContent.getStyle().set("display", "flex");
        
        HorizontalLayout splitLayout = new HorizontalLayout();
        splitLayout.setSizeFull();
        splitLayout.setSpacing(false);
        splitLayout.setPadding(false);

        LanguageSwitcher languageSwitcher = new LanguageSwitcher(userSession);
        languageSwitcher.getStyle().set("position", "absolute");
        languageSwitcher.getStyle().set("top", "20px");
        languageSwitcher.getStyle().set("right", "20px");
        languageSwitcher.getStyle().set("z-index", "10");
        add(languageSwitcher);

        VerticalLayout darkSide = createSide(
                "dark",
                "#1a1a1a",
                "white",
                "images/dark-side.png",
                "Lego Darth Vader",
                darkTitle,
                darkDesc
        );

        VerticalLayout lightSide = createSide(
                "light",
                "#ffffff",
                "#1a1a1a",
                "images/light-side.png",
                "Lego Luke Skywalker",
                lightTitle,
                lightDesc
        );

        splitLayout.add(darkSide, lightSide);
        splitLayout.setFlexGrow(1, darkSide);
        splitLayout.setFlexGrow(1, lightSide);

        mainContent.add(splitLayout);
        add(mainContent);

        // Signal-based bindings
        darkTitle.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "split.dark.title")));
        darkDesc.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "split.dark.desc")));
        lightTitle.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "split.light.title")));
        lightDesc.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "split.light.desc")));
        
        // Signal-based page title AND UI direction
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            UI.getCurrent().getPage().setTitle(getTranslation(l, "app.title"));
            
            // Reactive Direction Binding
            if (LanguageHelper.isRtl(l)) {
                UI.getCurrent().setDirection(Direction.RIGHT_TO_LEFT);
            } else {
                UI.getCurrent().setDirection(Direction.LEFT_TO_RIGHT);
            }
        });
    }

    private VerticalLayout createSide(String sideKey, String bgColor, String textColor, String imagePath, String imageAlt, H1 title, Paragraph desc) {
        VerticalLayout side = new VerticalLayout();
        side.addClassName("split-side");
        side.setSizeFull();
        side.getStyle().set("background-color", bgColor);
        side.getStyle().set("color", textColor);
        side.setJustifyContentMode(JustifyContentMode.CENTER);
        side.setAlignItems(Alignment.CENTER);

        side.addClickListener(e -> {
            if (!sideKey.equalsIgnoreCase(userSession.getSelectedSide())) {
                userSession.setProfileName(null);
            }
            userSession.setSelectedSide(sideKey);
            
            UI ui = UI.getCurrent();
            String sessionId = ui.getSession().getSession().getId();
            String persona = allianceRegistry.registerOrUpdate(sessionId, ui.getUIId(), sideKey, userSession.getProfileName());
            userSession.setProfileName(persona);
            
            getUI().ifPresent(activeUi -> {
                String intended = userSession.getIntendedRoute();
                if (intended != null && !intended.isEmpty()) {
                    userSession.setIntendedRoute(null);
                    activeUi.navigate(intended);
                } else {
                    activeUi.navigate(DashboardView.class);
                }
            });
        });

        Image image = new Image(imagePath, imageAlt);
        image.addClassName("lego-image");
        image.setWidth("300px");
        image.getStyle().set("border-radius", "10px");

        title.addClassName("split-text");
        title.getStyle().set("color", textColor);

        desc.addClassName("split-text");
        desc.getStyle().set("text-align", "center");
        desc.getStyle().set("max-width", "400px");

        side.add(image, title, desc);
        return side;
    }

    @Override
    public String getPageTitle() {
        return getTranslation(userSession.getLocaleSignal().peek(), "app.title");
    }
}
