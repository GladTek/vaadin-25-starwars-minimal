package com.gladtek.vaadin.models;

import com.vaadin.flow.signals.shared.SharedValueSignal;

public record Planet(String name, String climate, String terrain, SharedValueSignal<String> populationSignal, SharedValueSignal<Integer> trendSignal) {
}
