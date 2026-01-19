package com.gladtek.vaadin.views.home;

import com.gladtek.vaadin.components.LanguageSwitcher;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.util.LanguageHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;

@Route("")
public class SplitScreenView extends HorizontalLayout implements LocaleChangeObserver {

    private final H1 darkTitle;
    private final Paragraph darkDesc;
    private final H1 lightTitle;
    private final Paragraph lightDesc;

    public SplitScreenView(UserSession userSession) {
        setSizeFull();
        setSpacing(false);
        setPadding(false);

        LanguageSwitcher languageSwitcher = new LanguageSwitcher();
        languageSwitcher.getStyle().set("position", "absolute");
        languageSwitcher.getStyle().set("top", "20px");
        languageSwitcher.getStyle().set("right", "20px");
        languageSwitcher.getStyle().set("z-index", "10");
        add(languageSwitcher);

        darkTitle = new H1();
        darkDesc = new Paragraph();
        VerticalLayout darkSide = createSide(
                "dark",
                "#1a1a1a",
                "white",
                "images/dark-side.png",
                "Lego Darth Vader",
                darkTitle,
                darkDesc,
                userSession
        );

        lightTitle = new H1();
        lightDesc = new Paragraph();
        VerticalLayout lightSide = createSide(
                "light",
                "#ffffff",
                "#1a1a1a",
                "images/light-side.png",
                "Lego Luke Skywalker",
                lightTitle,
                lightDesc,
                userSession
        );

        add(darkSide, lightSide);

        setFlexGrow(1, darkSide);
        setFlexGrow(1, lightSide);

        updateTexts();
    }

    private VerticalLayout createSide(String sideKey, String bgColor, String textColor, String imagePath, String imageAlt, H1 title, Paragraph desc, UserSession userSession) {
        VerticalLayout side = new VerticalLayout();
        side.addClassName("split-side");
        side.setSizeFull();
        side.getStyle().set("background-color", bgColor);
        side.getStyle().set("color", textColor);
        side.setJustifyContentMode(JustifyContentMode.CENTER);
        side.setAlignItems(Alignment.CENTER);

        side.addClickListener(e -> {
            userSession.setSelectedSide(sideKey);
            getUI().ifPresent(ui -> {
                String intended = userSession.getIntendedRoute();
                if (intended != null && !intended.isEmpty()) {
                    userSession.setIntendedRoute(null);
                    ui.navigate(intended);
                } else {
                    ui.navigate(DashboardView.class);
                }
            });
        });

        Image image = new Image(imagePath, imageAlt);
        image.addClassName("lego-image");
        image.setWidth("300px");
        image.getStyle().set("border-radius", "10px");

        title.addClassName("split-text");
        title.getStyle().set("color", textColor); // Ensure title inherits text color if explicitly set

        desc.addClassName("split-text");
        desc.getStyle().set("text-align", "center");
        desc.getStyle().set("max-width", "400px");

        side.add(image, title, desc);
        return side;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        /** This is the case of updating texts without reloading the page.
        Useful when we do not have a lot of references to update**/
        updateTexts();
        UI.getCurrent().getSession().setLocale(event.getLocale());

        if (LanguageHelper.isRtl(event.getLocale())) {
            getUI().ifPresent(ui -> ui.setDirection(com.vaadin.flow.component.Direction.RIGHT_TO_LEFT));
        } else {
            getUI().ifPresent(ui -> ui.setDirection(com.vaadin.flow.component.Direction.LEFT_TO_RIGHT));
        }
    }

    private void updateTexts() {
        darkTitle.setText(getTranslation("split.dark.title"));
        darkDesc.setText(getTranslation("split.dark.desc"));
        lightTitle.setText(getTranslation("split.light.title"));
        lightDesc.setText(getTranslation("split.light.desc"));
    }
}
