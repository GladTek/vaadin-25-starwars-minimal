package com.gladtek.vaadin.views.showcase.components;

import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.slider.RangeSlider;
import com.vaadin.flow.component.slider.RangeSliderValue;
import com.vaadin.flow.component.slider.Slider;

public class ExperimentalSection extends VerticalLayout {

    public ExperimentalSection() {
        setPadding(false);
        setSpacing(true);

        H3 badgesTitle = new H3(getTranslation("components.badge.title"));
        HorizontalLayout badgesContainer = new HorizontalLayout();
        Badge pending = new Badge(getTranslation("components.status.pending"));

        Badge confirmed = new Badge(getTranslation("components.status.confirmed"));
        confirmed.addThemeVariants(BadgeVariant.SUCCESS);

        Badge warning = new Badge(getTranslation("components.status.warning"));
        warning.addThemeVariants(BadgeVariant.WARNING);

        Badge denied = new Badge(getTranslation("components.status.denied"));
        denied.addThemeVariants(BadgeVariant.ERROR);
        badgesContainer.add(pending, confirmed, warning, denied);

        H3 slidersTitle = new H3(getTranslation("components.sliders.title"));
        Slider slider = new Slider(getTranslation("components.slider.volume"));
        slider.setValue(50.0);
        
        RangeSlider rangeSlider = new RangeSlider(getTranslation("components.slider.price_range"), 0, 1000);
        rangeSlider.setValue(new RangeSliderValue(200, 800));
        rangeSlider.getElement().setAttribute("role", "group");
        rangeSlider.getElement().setAttribute("aria-label", getTranslation("components.slider.price_range_aria"));
        add(rangeSlider);

        Slider sliderMinMax = new Slider(getTranslation("components.slider.temperature"), 0, 100);
        sliderMinMax.setValue(50.0);
        sliderMinMax.setMinMaxVisible(true);
        add(sliderMinMax);

        add(badgesTitle, badgesContainer,slidersTitle,slider,rangeSlider,sliderMinMax);
    }
}
