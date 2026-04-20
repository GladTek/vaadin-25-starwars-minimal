package com.gladtek.vaadin.services;

import com.vaadin.flow.signals.Signal;
import com.vaadin.flow.signals.shared.SharedListSignal;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Application-scoped registry tracking all active alliances in a SharedListSignal.
 * Refactored for Pure Java, Session-based identity with tab reference counting.
 */
@Component
public class AllianceRegistry {

    private final ProfileNameService profileNameService;
    private final SharedListSignal<UserAlliance> activeAlliances = new SharedListSignal<>(UserAlliance.class);
    // Track unique UI IDs per session to prevent leak on navigation
    private final Map<String, Set<Integer>> sessionUIs = new ConcurrentHashMap<>();

    public AllianceRegistry(ProfileNameService profileNameService) {
        this.profileNameService = profileNameService;
    }

    /**
     * Get the reactive signal containing the list of active user alliances. 
     */
    public SharedListSignal<UserAlliance> getActiveAlliances() {
        return activeAlliances;
    }

    /**
     * Derived signal to count active 'Lords' (Dark side).
     */
    public Signal<Long> getLordCount() {
        return Signal.computed(() -> 
            activeAlliances.get().stream()
                .filter(uS -> "dark".equalsIgnoreCase(uS.get().side()))
                .count());
    }

    /**
     * Derived signal to count active 'Light' users.
     */
    public Signal<Long> getLightCount() {
        return Signal.computed(() -> 
            activeAlliances.get().stream()
                .filter(uS -> "light".equalsIgnoreCase(uS.get().side()))
                .count());
    }

    /**
     * Registers or updates a user session. Uses reference counting for tabs.
     */
    public String registerOrUpdate(String sessionId, int uiId, String side, String existingProfileName) {
        // Track unique UI ID for this session
        sessionUIs.computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet()).add(uiId);

        return activeAlliances.peek().stream()
            .filter(uS -> sessionId.equals(uS.peek().sessionId()))
            .findFirst()
            .map(uS -> {
                // If side changed, generate new name unless overridden
                if (!side.equalsIgnoreCase(uS.peek().side())) {
                    String name = (existingProfileName != null) ? existingProfileName : profileNameService.generateRandomName(side);
                    uS.set(new UserAlliance(sessionId, side, name, true, "Entering..."));
                    return name;
                }
                return uS.peek().profileName();
            })
            .orElseGet(() -> {
                String name = (existingProfileName != null) ? existingProfileName : profileNameService.generateRandomName(side);
                activeAlliances.insertLast(new UserAlliance(sessionId, side, name, true, "Entering..."));
                return name;
            });
    }

    public String registerOrUpdate(String sessionId, int uiId, String side) {
        return registerOrUpdate(sessionId, uiId, side, null);
    }

    /**
     * Updates the current view location of a user (Real-time presence).
     */
    public void updateCurrentPage(String sessionId, String currentPage) {
        activeAlliances.peek().stream()
            .filter(uS -> sessionId.equals(uS.peek().sessionId()))
            .findFirst()
            .ifPresent(uS -> {
                UserAlliance current = uS.peek();
                uS.set(new UserAlliance(current.sessionId(), current.side(), current.profileName(), true, currentPage));
            });
    }

    /**
     * Get a reactive signal for the profile name of the current session.
     */
    public Signal<String> getMyProfileName(String sessionId) {
        return Signal.computed(() -> 
            activeAlliances.get().stream()
                .filter(uS -> sessionId.equals(uS.get().sessionId()))
                .findFirst()
                .map(uS -> uS.get().profileName())
                .orElse("Citizen of the Galaxy")
        );
    }

    /**
     * Removes a tab reference. Only removes the session from the registry 
     * when the very last tab is closed.
     */
    public void unregister(String sessionId, int uiId) {
        Set<Integer> uis = sessionUIs.get(sessionId);
        if (uis != null) {
            uis.remove(uiId);
            if (uis.isEmpty()) {
                sessionUIs.remove(sessionId);
                // Last tab closed, perform final cleanup
                activeAlliances.peek().stream()
                    .filter(uS -> sessionId.equals(uS.peek().sessionId()))
                    .findFirst()
                    .ifPresent(activeAlliances::remove);
            }
        }
    }
}
