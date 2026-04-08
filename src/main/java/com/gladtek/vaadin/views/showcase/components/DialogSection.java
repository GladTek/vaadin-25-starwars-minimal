package com.gladtek.vaadin.views.showcase.components;

import com.gladtek.vaadin.services.UserSession;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.signals.Signal;

import java.util.Locale;

public class DialogSection extends VerticalLayout {

    private final UserSession userSession;
    private final Button dialogBtn;
    private final Button defaultNotifyBtn;
    private final Button successNotifyBtn;
    private final Button errorNotifyBtn;
    private final Button mentionNotifyBtn;
    private final H3 dialogsTitle;
    private final H3 notificationsTitle;

    public DialogSection(UserSession userSession) {
        this.userSession = userSession;
        setPadding(false);
        setSpacing(true);

        dialogBtn = new Button();
        dialogBtn.addClickListener(e -> {
            Dialog dialog = new Dialog();
            
            // To support live translations while dialog is open, we could use Signal.effect inside here,
            // but for simplicity we'll just evaluate the current locale when opening.
            Locale l = userSession.getLocaleSignal().peek();
            dialog.setHeaderTitle(getTranslation(l, "components.dialog.title"));
            
            Span contentSpan = new Span();
            contentSpan.bindText(userSession.getLocaleSignal().map(loc -> getTranslation(loc, "components.dialog.content")));
            dialog.add(contentSpan);
            
            Button closeButton = new Button();
            closeButton.bindText(userSession.getLocaleSignal().map(loc -> getTranslation(loc, "components.dialog.close")));
            closeButton.addClickListener(event -> dialog.close());
            dialog.getFooter().add(closeButton);
            
            dialog.open();
        });

        defaultNotifyBtn = new Button();
        defaultNotifyBtn.addClickListener(e -> {
            Notification.show(getTranslation(userSession.getLocaleSignal().peek(), "components.notification.message"));
        });

        successNotifyBtn = new Button();
        successNotifyBtn.addClickListener(e -> createSubmitSuccess().open());

        errorNotifyBtn = new Button();
        errorNotifyBtn.addClickListener(e -> createReportError().open());

        mentionNotifyBtn = new Button();
        mentionNotifyBtn.addClickListener(e -> createMentionNotification().open());

        HorizontalLayout notificationsButtons = new HorizontalLayout(defaultNotifyBtn, successNotifyBtn, errorNotifyBtn, mentionNotifyBtn);

        dialogsTitle = new H3();
        notificationsTitle = new H3();
        add(dialogsTitle, dialogBtn, notificationsTitle, notificationsButtons);

        // Reactive bindings
        Signal.effect(this, () -> {
            Locale l = userSession.getLocaleSignal().get();
            dialogBtn.setText(getTranslation(l, "components.dialog.open"));
            defaultNotifyBtn.setText(getTranslation(l, "components.notification.default"));
            successNotifyBtn.setText(getTranslation(l, "components.notification.success"));
            errorNotifyBtn.setText(getTranslation(l, "components.notification.error"));
            mentionNotifyBtn.setText(getTranslation(l, "components.notification.mention"));
            dialogsTitle.setText(getTranslation(l, "components.dialogs.title"));
            notificationsTitle.setText(getTranslation(l, "components.notification.title"));
        });
    }

    public Notification createSubmitSuccess() {
        Locale l = userSession.getLocaleSignal().peek();
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.SUCCESS);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();

        Button viewBtn = new Button(getTranslation(l, "components.notification.action.view"));

        HorizontalLayout layout = new HorizontalLayout(icon,
                new Text(getTranslation(l, "components.notification.success.text")));
        layout.addToEnd(viewBtn, createCloseBtn(notification, l));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMinWidth("350px");

        notification.add(layout);
        return notification;
    }

    public Notification createReportError() {
        Locale l = userSession.getLocaleSignal().peek();
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.ERROR);

        Icon icon = VaadinIcon.WARNING.create();
        Button retryBtn = new Button(getTranslation(l, "components.notification.action.retry"));

        HorizontalLayout layout = new HorizontalLayout(icon,
                new Text(getTranslation(l, "components.notification.error.text")));
        layout.addToEnd(retryBtn, createCloseBtn(notification, l));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMinWidth("350px");

        notification.add(layout);
        return notification;
    }

    public Notification createMentionNotification() {
        Locale l = userSession.getLocaleSignal().peek();
        Notification notification = new Notification();

        Avatar avatar = new Avatar(getTranslation(l, "components.notification.mention.user"));

        Span name = new Span(getTranslation(l, "components.notification.mention.user"));
        name.getStyle().set("font-weight", "500");

        Anchor projectLink = new Anchor("https://gladtek.com", getTranslation(l, "components.notification.mention.project"));
        projectLink.setTarget("_blank");

        Div info = new Div(name, new Text(" " + getTranslation(l, "components.notification.mention.text") + " "), projectLink);

        HorizontalLayout layout = new HorizontalLayout(avatar, info);
        layout.addToEnd(createCloseBtn(notification, l));
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMinWidth("350px");

        notification.add(layout);
        return notification;
    }

    public Button createCloseBtn(Notification notification, Locale l) {
        Button closeBtn = new Button(VaadinIcon.CLOSE_SMALL.create(), e -> notification.close());
        closeBtn.setAriaLabel(getTranslation(l, "components.dialog.close_aria"));
        return closeBtn;
    }
}
