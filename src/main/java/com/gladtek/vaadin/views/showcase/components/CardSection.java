package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class CardSection extends VerticalLayout {

    private final UserSession userSession;

    private final Image mediaImage;
    private final Card simpleCard;
    private final Span simpleCardContent;
    
    private final Card imageCard;
    private final Span badge;
    private final Span imageCardContent;
    private final Button bookBtn;
    private final Button learnBtn;
    private final H3 advancedTitle;

    public CardSection(UserSession userSession) {
        this.userSession = userSession;
        setPadding(false);
        setSpacing(true);

        mediaImage = new Image("https://images.unsplash.com/photo-1519681393784-d120267933ba?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80", "");
        mediaImage.setWidth("100%");
        mediaImage.setHeight("150px");
        mediaImage.getStyle().set("object-fit", "cover");

        // Simple Card
        simpleCard = new Card();
        simpleCardContent = new Span();
        simpleCard.add(simpleCardContent);

        // Card with Image
        imageCard = new Card();
        imageCard.setWidth("300px");
        imageCard.setMedia(mediaImage);

        badge = new Span();
        badge.getElement().getThemeList().add("badge success");
        imageCard.setHeaderSuffix(badge);

        imageCardContent = new Span();
        imageCard.add(imageCardContent);

        bookBtn = new Button();
        bookBtn.addThemeVariants(ButtonVariant.PRIMARY);

        learnBtn = new Button();
        imageCard.addToFooter(bookBtn, learnBtn);

        advancedTitle = new H3();
        add(advancedTitle, simpleCard, imageCard);

        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();

            mediaImage.setAlt(getTranslation(l, "components.card.image.alt"));
            
            simpleCard.setTitle(getTranslation(l, "components.card.simple.title"));
            simpleCardContent.setText(getTranslation(l, "components.card.simple.content"));

            imageCard.setTitle(getTranslation(l, "components.card.travel.title"));
            imageCard.setSubtitle(getTranslation(l, "components.card.travel.subtitle"));
            
            badge.setText(getTranslation(l, "components.card.badge.new"));
            imageCardContent.setText(getTranslation(l, "components.card.travel.content"));
            
            bookBtn.setText(getTranslation(l, "components.card.action.book"));
            learnBtn.setText(getTranslation(l, "components.card.action.learn"));

            advancedTitle.setText(getTranslation(l, "components.card.advanced.title"));
        });
    }
}
