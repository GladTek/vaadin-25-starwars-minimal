package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.slider.RangeSlider;
import com.vaadin.flow.component.slider.RangeSliderValue;
import com.vaadin.flow.component.slider.Slider;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class ExperimentalSection extends VerticalLayout {

    private final UserSession userSession;
    private final H3 badgesTitle;
    private final Badge pending;
    private final Badge confirmed;
    private final Badge warning;
    private final Badge denied;

    private final H3 slidersTitle;
    private final Slider slider;
    private final RangeSlider rangeSlider;
    private final Slider sliderMinMax;

    public ExperimentalSection(UserSession userSession) {
        this.userSession = userSession;
        setPadding(false);
        setSpacing(true);

        badgesTitle = new H3();
        HorizontalLayout badgesContainer = new HorizontalLayout();
        
        pending = new Badge();
        confirmed = new Badge();
        confirmed.addThemeVariants(BadgeVariant.SUCCESS);
        warning = new Badge();
        warning.addThemeVariants(BadgeVariant.WARNING);
        denied = new Badge();
        denied.addThemeVariants(BadgeVariant.ERROR);
        badgesContainer.add(pending, confirmed, warning, denied);

        slidersTitle = new H3();
        slider = new Slider();
        slider.setValue(50.0);
        
        rangeSlider = new RangeSlider();
        rangeSlider.setMin(0);
        rangeSlider.setMax(1000);
        rangeSlider.setValue(new RangeSliderValue(200, 800));
        rangeSlider.getElement().setAttribute("role", "group");
        add(rangeSlider);

        sliderMinMax = new Slider();
        sliderMinMax.setMin(0);
        sliderMinMax.setMax(100);
        sliderMinMax.setValue(50.0);
        sliderMinMax.setMinMaxVisible(true);
        add(sliderMinMax);

        add(badgesTitle, badgesContainer, slidersTitle, slider, rangeSlider, sliderMinMax);

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            
            badgesTitle.setText(getTranslation(l, "components.badge.title"));
            pending.setText(getTranslation(l, "components.status.pending"));
            confirmed.setText(getTranslation(l, "components.status.confirmed"));
            warning.setText(getTranslation(l, "components.status.warning"));
            denied.setText(getTranslation(l, "components.status.denied"));

            slidersTitle.setText(getTranslation(l, "components.sliders.title"));
            slider.setLabel(getTranslation(l, "components.slider.volume"));
            
            rangeSlider.setLabel(getTranslation(l, "components.slider.price_range"));
            rangeSlider.getElement().setAttribute("aria-label", getTranslation(l, "components.slider.price_range_aria"));
            
            sliderMinMax.setLabel(getTranslation(l, "components.slider.temperature"));
        });
    }
}
