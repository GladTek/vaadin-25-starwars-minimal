package com.gladtek.vaadin.services;

import java.io.Serializable;

/**
 * Domain model for an active session's alliance, including their random persona, status, and current view location.
 */
public record UserAlliance(String sessionId, String side, String profileName, boolean isVisible, String currentPage) implements Serializable {
}
