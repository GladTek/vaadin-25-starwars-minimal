package com.gladtek.vaadin.views.errors;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpServletResponse;

@PageTitle("404 - Page Not Found")
@AnonymousAllowed
public class NotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException>, LocaleChangeObserver {

    private final H1 header;
    private final Paragraph description;
    private final Button backButton;

    public NotFoundView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        header = new H1();
        description = new Paragraph();
        backButton = new Button();
        backButton.addThemeVariants(ButtonVariant.AURA_PRIMARY);
        backButton.addClickListener(e -> UI.getCurrent().navigate(""));

        add(header, description, backButton);

        updateTexts();
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        updateTexts();
        return HttpServletResponse.SC_NOT_FOUND;
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        updateTexts();
    }

    private void updateTexts() {
        header.setText(getTranslation("error.404.title"));
        description.setText(getTranslation("error.404.description"));
        backButton.setText(getTranslation("error.404.back"));
    }
}
