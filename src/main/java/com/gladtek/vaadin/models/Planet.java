package com.gladtek.vaadin.models;

import com.vaadin.flow.signals.local.ValueSignal;

public record Planet(String name, String climate, String terrain, ValueSignal<String> populationSignal, ValueSignal<Integer> trendSignal) {
}
