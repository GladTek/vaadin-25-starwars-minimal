package com.gladtek.vaadin.views.showcase.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.ThemeVariant;

public class ButtonSection extends VerticalLayout {

    public ButtonSection() {
        setPadding(false);
        setSpacing(true);

        FlexLayout row1 = new FlexLayout();
        row1.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        row1.getStyle().set("gap", "10px");
        row1.add(new Button(getTranslation("components.button.default")));
        row1.add(createVariantButton(getTranslation("components.button.primary"), ButtonVariant.AURA_PRIMARY));
        row1.add(createVariantButton(getTranslation("components.button.tertiary"), ButtonVariant.AURA_TERTIARY));
        row1.add(createVariantButton(getTranslation("components.button.danger"), ButtonVariant.AURA_DANGER));
        add(row1);
        add(createSpacer());

        FlexLayout row2 = new FlexLayout();
        row2.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        row2.getStyle().set("gap", "10px");
        row2.add(createIconButton(getTranslation("components.button.default"), VaadinIcon.PLUS.create(), false));
        row2.add(createIconButton(getTranslation("components.button.primary"), VaadinIcon.PLUS.create(), false, ButtonVariant.AURA_PRIMARY));
        row2.add(createIconButton(getTranslation("components.button.tertiary"), VaadinIcon.PLUS.create(), false, ButtonVariant.AURA_TERTIARY));
        row2.add(createIconButton(getTranslation("components.button.danger"), VaadinIcon.TRASH.create(), false, ButtonVariant.AURA_DANGER));
        add(row2);
        add(createSpacer());

        FlexLayout row3 = new FlexLayout();
        row3.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        row3.getStyle().set("gap", "10px");
        row3.add(createIconButton(getTranslation("components.button.default"), VaadinIcon.ARROW_RIGHT.create(), true));
        row3.add(createIconButton(getTranslation("components.button.primary"), VaadinIcon.ARROW_RIGHT.create(), true, ButtonVariant.AURA_PRIMARY));
        row3.add(createIconButton(getTranslation("components.button.tertiary"), VaadinIcon.ARROW_RIGHT.create(), true, ButtonVariant.AURA_TERTIARY));
        row3.add(createIconButton(getTranslation("components.button.danger"), VaadinIcon.TRASH.create(), true, ButtonVariant.AURA_DANGER));
        add(row3);
    }

    private Button createIconButton(String text, com.vaadin.flow.component.Component icon, boolean iconAfterText) {
        Button button = new Button(text, icon);
        button.setIconAfterText(iconAfterText);
        return button;
    }

    private Button createIconButton(String text, com.vaadin.flow.component.Component icon, boolean iconAfterText, ThemeVariant variant) {
        Button button = createIconButton(text, icon, iconAfterText);
        button.addThemeName(variant.getVariantName());
        return button;
    }

    private Button createVariantButton(String text, ThemeVariant variant) {
        Button button = new Button(text, e -> {
        });
        button.addThemeName(variant.getVariantName());
        return button;
    }
    private Component createSpacer() {
        Div spacer = new Div();
        spacer.setHeight("20px");
        return spacer;
    }
}
