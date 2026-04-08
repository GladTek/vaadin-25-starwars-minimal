package com.gladtek.vaadin.components;

import com.gladtek.vaadin.services.AllianceRegistry;
import com.gladtek.vaadin.services.UserAlliance;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.avatar.AvatarGroup.AvatarGroupItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.signals.Signal;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Independent component representing the current alliance presence using AvatarGroup
 * and the Lord/Light count. Refactored for Pure Java, Session-based identity.
 */
public class AlliancePresence extends HorizontalLayout {

    public AlliancePresence(AllianceRegistry allianceRegistry) {
        setAlignItems(Alignment.CENTER);
        setPadding(false);
        setSpacing(true);

        // Official AvatarGroup for overlapping items and overflow handling
        AvatarGroup avatarGroup = new AvatarGroup();
        avatarGroup.getStyle().set("margin-right", "0.5rem");
        avatarGroup.setMaxItemsVisible(5);
        
        // Reactive Effect: Map the shared list to AvatarGroupItem objects
        Signal.effect(avatarGroup, () -> {
            List<AvatarGroupItem> items = allianceRegistry.getActiveAlliances().get().stream()
                .map(uS -> {
                    UserAlliance u = uS.get();
                    String name = u.profileName();
                    // Tooltip shows the persona and their current page
                    String tooltip = String.format("%s - On: %s", name, u.currentPage());
                    
                    AvatarGroupItem item = new AvatarGroupItem(tooltip);
                    if (name != null && !name.isEmpty()) {
                        item.setAbbreviation(name.substring(0, 1).toUpperCase());
                    }
                    // Visual Correction (from screenshot): Index 6 = Red (Dark), Index 3 = Blue (Light)
                    item.setColorIndex("dark".equalsIgnoreCase(u.side()) ? 6 : 3);
                    return item;
                })
                .collect(Collectors.toList());
            
            avatarGroup.setItems(items);
        });

        // Alliance Statistics
        Span allianceStats = new Span();
        allianceStats.getStyle().set("white-space", "nowrap")
                          .set("font-size", "0.85rem")
                          .set("color", "var(--lumo-secondary-text-color)")
                          .set("flex-shrink", "0");
        
        // Reactively bind header text to the registry's computed counts
        allianceStats.bindText(Signal.computed(() -> 
            String.format("(D: %d / L: %d)", 
                allianceRegistry.getLordCount().get(), 
                allianceRegistry.getLightCount().get()))
        );

        add(avatarGroup, allianceStats);
    }
}
