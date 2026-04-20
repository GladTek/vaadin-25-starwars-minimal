package com.gladtek.vaadin.views.showcase;

import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.services.UserSession;
import com.gladtek.vaadin.views.showcase.components.*;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

@Route(value = "components", layout = MainLayout.class)
public class ComponentsView extends VerticalLayout implements HasDynamicTitle {

    private final UserSession userSession;
    private final FlexLayout contentContainer = new FlexLayout();
    private final H2 header;
    private final Span description;
    private final TabSheet tabSheet;

    public ComponentsView(UserSession userSession) {
        this.userSession = userSession;
        
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        header = new H2();
        description = new Span();
        add(header, description);

        // Configure responsive container
        contentContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contentContainer.getStyle().set("gap", "32px");
        contentContainer.setSizeFull();

        // Create contents using new component classes inject session
        ExperimentalSection experimentalSection = new ExperimentalSection(userSession);
        InputSection inputSection = new InputSection(userSession);
        ButtonSection buttonSection = new ButtonSection(userSession);
        SelectionSection selectionSection = new SelectionSection(userSession);
        DisplaySection displaySection = new DisplaySection(userSession);
        CardSection cardSection = new CardSection(userSession);
        DialogSection dialogSection = new DialogSection(userSession);

        // Style sections
        styleSection(experimentalSection);
        styleSection(inputSection);
        styleSection(buttonSection);
        styleSection(selectionSection);
        styleSection(displaySection);
        styleSection(cardSection);
        styleSection(dialogSection);

        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        // Add content, label will be assigned via Signal
        Tab expTab = tabSheet.add("", experimentalSection);
        Tab inTab = tabSheet.add("", inputSection);
        Tab btnTab = tabSheet.add("", buttonSection);
        Tab selTab = tabSheet.add("", selectionSection);
        Tab dispTab = tabSheet.add("", displaySection);
        Tab cardTab = tabSheet.add("", cardSection);
        Tab diagTab = tabSheet.add("", dialogSection);

        add(tabSheet);

        // Reactive bindings
        header.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "components.header")));
        description.bindText(userSession.getLocaleSignal().map(l -> getTranslation(l, "components.description")));

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            // Tab labels
            expTab.setLabel(getTranslation(l, "components.tab.experimental"));
            inTab.setLabel(getTranslation(l, "components.tab.inputs"));
            btnTab.setLabel(getTranslation(l, "components.tab.buttons"));
            selTab.setLabel(getTranslation(l, "components.tab.selection"));
            dispTab.setLabel(getTranslation(l, "components.tab.display"));
            cardTab.setLabel(getTranslation(l, "components.tab.card"));
            diagTab.setLabel(getTranslation(l, "components.tab.dialogs"));
            
            // Browser Page Title update
            getUI().ifPresent(ui -> ui.getPage().setTitle(getPageTitle(l)));
        });
    }

    private void styleSection(VerticalLayout section) {
        section.setWidthFull();
        section.getStyle().set("box-shadow", "0 1px 4px 0 rgba(0, 0, 0, 0.1)");
        section.getStyle().set("border-radius", "8px");
        section.getStyle().set("padding", "20px");
    }

    private String getPageTitle(Locale l) {
        if (l == null) l = Locale.ENGLISH;
        return getTranslation(l, "components.title");
    }

    @Override
    public String getPageTitle() {
        return getPageTitle(userSession.getLocaleSignal().peek());
    }
}
