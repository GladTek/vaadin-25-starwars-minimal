package com.gladtek.vaadin.views.showcase.components;

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

        Button defaultNotifyBtn = new Button(getTranslation("components.notification.default"), e -> {
            Notification.show(getTranslation("components.notification.message"));
        });

        Button successNotifyBtn = new Button(getTranslation("components.notification.success"), e -> {
            createSubmitSuccess().open();
        });

        Button errorNotifyBtn = new Button(getTranslation("components.notification.error"), e -> {
            createReportError().open();
        });

        Button mentionNotifyBtn = new Button(getTranslation("components.notification.mention"), e -> {
            createMentionNotification().open();
        });

        HorizontalLayout notificationsButtons = new HorizontalLayout(defaultNotifyBtn, successNotifyBtn, errorNotifyBtn, mentionNotifyBtn);

        H3 dialogsTitle = new H3(getTranslation("components.dialogs.title"));
        H3 notificationsTitle = new H3(getTranslation("components.notification.title"));
        add(dialogsTitle, dialogBtn, notificationsTitle, notificationsButtons);

    }

    public Notification createSubmitSuccess() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.SUCCESS);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();

        Button viewBtn = new Button(getTranslation("components.notification.action.view"));

        HorizontalLayout layout = new HorizontalLayout(icon,
                new Text(getTranslation("components.notification.success.text")));
        layout.addToEnd(viewBtn, createCloseBtn());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMinWidth("350px");

        notification.add(layout);

        return notification;
    }

    public Notification createReportError() {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.ERROR);

        Icon icon = VaadinIcon.WARNING.create();
        Button retryBtn = new Button(getTranslation("components.notification.action.retry"));

        HorizontalLayout layout = new HorizontalLayout(icon,
                new Text(getTranslation("components.notification.error.text")));
        layout.addToEnd(retryBtn, createCloseBtn());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMinWidth("350px");

        notification.add(layout);

        return notification;
    }

    public Notification createMentionNotification() {
        Notification notification = new Notification();

        Avatar avatar = new Avatar(getTranslation("components.notification.mention.user"));

        Span name = new Span(getTranslation("components.notification.mention.user"));
        name.getStyle().set("font-weight", "500");

        Div info = new Div(name, new Text(" " +getTranslation("components.notification.mention.text") + " "),
                new Anchor("#", getTranslation("components.notification.mention.project")));

        HorizontalLayout layout = new HorizontalLayout(avatar, info);
        layout.addToEnd(createCloseBtn());
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setMinWidth("350px");

        notification.add(layout);

        return notification;
    }

    public Button createCloseBtn() {
        Button closeBtn = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeBtn.setAriaLabel(getTranslation("components.dialog.close_aria"));
        return closeBtn;
    }
}
