package com.gladtek.vaadin.views.showcase;

import com.gladtek.vaadin.layout.MainLayout;
import com.gladtek.vaadin.views.showcase.components.*;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "components", layout = MainLayout.class)
@AnonymousAllowed
public class ComponentsView extends VerticalLayout implements HasDynamicTitle {

    private final FlexLayout contentContainer = new FlexLayout();

    public ComponentsView() {
        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(new H2(getTranslation("components.header")));
        add(new Span(getTranslation("components.description")));

        Tabs tabs = new Tabs();

        tabs.setWidthFull();

        Tab inputTab = new Tab(getTranslation("components.tab.inputs"));
        Tab buttonTab = new Tab(getTranslation("components.tab.buttons"));
        Tab selectionTab = new Tab(getTranslation("components.tab.selection"));
        Tab displayTab = new Tab(getTranslation("components.tab.display"));
        Tab cardTab = new Tab(getTranslation("components.tab.card"));
        Tab dialogTab = new Tab(getTranslation("components.tab.dialogs"));

        tabs.add(inputTab, buttonTab, selectionTab, displayTab, cardTab, dialogTab);

        // Configure responsive container
        contentContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contentContainer.getStyle().set("gap", "32px");
        contentContainer.setSizeFull();

        // Create contents using new component classes
        InputSection inputSection = new InputSection();
        ButtonSection buttonSection = new ButtonSection();
        SelectionSection selectionSection = new SelectionSection();
        DisplaySection displaySection = new DisplaySection();
        CardSection cardSection = new CardSection();
        DialogSection dialogSection = new DialogSection();

        // Style sections (keep some styling but ensure they fill space in single view)
        styleSection(inputSection);
        styleSection(buttonSection);
        styleSection(selectionSection);
        styleSection(displaySection);
        styleSection(cardSection);
        styleSection(dialogSection);

        // Add contents to container
        //contentContainer.add(inputSection, buttonSection, selectionSection, displaySection, cardSection, dialogSection);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add(getTranslation("components.tab.inputs"),inputSection);
        tabSheet.add(getTranslation("components.tab.buttons"), buttonSection);
        tabSheet.add(getTranslation("components.tab.selection"), selectionSection);
        tabSheet.add(getTranslation("components.tab.display"),displaySection);
        tabSheet.add(getTranslation("components.tab.card"), cardSection);
        tabSheet.add(getTranslation("components.tab.dialogs"), dialogSection);

        add(tabSheet);
    }

    private void styleSection(VerticalLayout section) {
        section.setWidthFull(); // Fill width in tab view
        section.getStyle().set("box-shadow", "0 1px 4px 0 rgba(0, 0, 0, 0.1)");
        section.getStyle().set("border-radius", "8px");
        section.getStyle().set("padding", "20px");
    }

    @Override
    public String getPageTitle() {
        return getTranslation("components.title");
    }
}
