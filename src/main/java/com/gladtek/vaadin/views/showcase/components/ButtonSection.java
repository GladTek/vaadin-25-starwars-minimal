package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class ButtonSection extends VerticalLayout {

    private final UserSession userSession;
    
    // Row 1
    private final Button r1Default;
    private final Button r1Primary;
    private final Button r1Tertiary;
    private final Button r1Danger;

    // Row 2
    private final Button r2Default;
    private final Button r2Primary;
    private final Button r2Tertiary;
    private final Button r2Danger;

    // Row 3
    private final Button r3Default;
    private final Button r3Primary;
    private final Button r3Tertiary;
    private final Button r3Danger;

    public ButtonSection(UserSession userSession) {
        this.userSession = userSession;
        setPadding(false);
        setSpacing(true);

        r1Default = new Button();
        r1Primary = createVariantButton(ButtonVariant.PRIMARY);
        r1Tertiary = createVariantButton(ButtonVariant.TERTIARY);
        r1Danger = createVariantButton(ButtonVariant.ERROR);

        FlexLayout row1 = new FlexLayout();
        row1.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        row1.getStyle().set("gap", "10px");
        row1.add(r1Default, r1Primary, r1Tertiary, r1Danger);
        add(row1);
        add(createSpacer());

        r2Default = createIconButton(VaadinIcon.PLUS.create(), false);
        r2Primary = createIconButton(VaadinIcon.PLUS.create(), false, ButtonVariant.PRIMARY);
        r2Tertiary = createIconButton(VaadinIcon.PLUS.create(), false, ButtonVariant.TERTIARY);
        r2Danger = createIconButton(VaadinIcon.TRASH.create(), false, ButtonVariant.ERROR);

        FlexLayout row2 = new FlexLayout();
        row2.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        row2.getStyle().set("gap", "10px");
        row2.add(r2Default, r2Primary, r2Tertiary, r2Danger);
        add(row2);
        add(createSpacer());

        r3Default = createIconButton(VaadinIcon.ARROW_RIGHT.create(), true);
        r3Primary = createIconButton(VaadinIcon.ARROW_RIGHT.create(), true, ButtonVariant.PRIMARY);
        r3Tertiary = createIconButton(VaadinIcon.ARROW_RIGHT.create(), true, ButtonVariant.TERTIARY);
        r3Danger = createIconButton(VaadinIcon.TRASH.create(), true, ButtonVariant.ERROR);

        FlexLayout row3 = new FlexLayout();
        row3.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        row3.getStyle().set("gap", "10px");
        row3.add(r3Default, r3Primary, r3Tertiary, r3Danger);
        add(row3);

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            r1Default.setText(getTranslation(l, "components.button.default"));
            r1Primary.setText(getTranslation(l, "components.button.primary"));
            r1Tertiary.setText(getTranslation(l, "components.button.tertiary"));
            r1Danger.setText(getTranslation(l, "components.button.danger"));

            r2Default.setText(getTranslation(l, "components.button.default"));
            r2Primary.setText(getTranslation(l, "components.button.primary"));
            r2Tertiary.setText(getTranslation(l, "components.button.tertiary"));
            r2Danger.setText(getTranslation(l, "components.button.danger"));

            r3Default.setText(getTranslation(l, "components.button.default"));
            r3Primary.setText(getTranslation(l, "components.button.primary"));
            r3Tertiary.setText(getTranslation(l, "components.button.tertiary"));
            r3Danger.setText(getTranslation(l, "components.button.danger"));
        });
    }

    private Button createIconButton(Component icon, boolean iconAfterText) {
        Button button = new Button();
        button.setIcon(icon);
        button.setIconAfterText(iconAfterText);
        return button;
    }

    private Button createIconButton(Component icon, boolean iconAfterText, ThemeVariant variant) {
        Button button = createIconButton(icon, iconAfterText);
        button.addThemeName(variant.getVariantName());
        return button;
    }

    private Button createVariantButton(ThemeVariant variant) {
        Button button = new Button();
        button.addThemeName(variant.getVariantName());
        return button;
    }
    
    private Component createSpacer() {
        Div spacer = new Div();
        spacer.setHeight("20px");
        return spacer;
    }
}
