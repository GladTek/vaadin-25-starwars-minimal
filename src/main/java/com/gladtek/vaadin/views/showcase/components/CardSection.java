package com.gladtek.vaadin.views.showcase.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CardSection extends VerticalLayout {

    public CardSection() {
        setPadding(false);
        setSpacing(true);

        Image mediaImage = new Image("https://images.unsplash.com/photo-1519681393784-d120267933ba?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80", "Snowy Landscape");
        mediaImage.setWidth("100%");
        mediaImage.setHeight("150px");
        mediaImage.getStyle().set("object-fit", "cover");

        // Simple Card
        Card simpleCard = new Card();
        simpleCard.setTitle(getTranslation("components.card.simple.title"));
        simpleCard.add(new Span(getTranslation("components.card.simple.content")));

        // Card with Image
        Card imageCard = new Card();
        imageCard.setWidth("300px");

        // Media
        imageCard.setMedia(mediaImage);

        // Header (Prefix, Title, Subtitle, Suffix)
        imageCard.setTitle(getTranslation("components.card.travel.title"));
        imageCard.setSubtitle(getTranslation("components.card.travel.subtitle"));

        Span badge = new Span(getTranslation("components.card.badge.new"));
        badge.getElement().getThemeList().add("badge success"); //Won't have a default color on Aura
        imageCard.setHeaderSuffix(badge);

        // Content
        imageCard.add(new Span(getTranslation("components.card.travel.content")));

        // Footer (Actions)
        Button bookBtn = new Button(getTranslation("components.card.action.book"));
        bookBtn.addThemeVariants(ButtonVariant.AURA_PRIMARY);

        Button learnBtn = new Button(getTranslation("components.card.action.learn"));
        imageCard.addToFooter(bookBtn, learnBtn);


        add(new H3(getTranslation("components.card.advanced.title")), simpleCard, imageCard);
    }
}
