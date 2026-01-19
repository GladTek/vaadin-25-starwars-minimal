package com.gladtek.vaadin.views.showcase.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DialogSection extends VerticalLayout {

    public DialogSection() {
        setPadding(false);
        setSpacing(true);

        Button dialogBtn = new Button(getTranslation("components.dialog.open"), e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle(getTranslation("components.dialog.title"));
            dialog.add(new Span(getTranslation("components.dialog.content")));
            Button closeButton = new Button(getTranslation("components.dialog.close"), event -> dialog.close());
            dialog.getFooter().add(closeButton);
            dialog.open();
        });

        Button notifyBtn = new Button(getTranslation("components.notification.show"), e -> {
            Notification.show(getTranslation("components.notification.message"));
        });

        HorizontalLayout buttons = new HorizontalLayout(dialogBtn, notifyBtn);
        add(buttons);
    }
}
