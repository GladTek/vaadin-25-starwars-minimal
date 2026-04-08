package com.gladtek.vaadin;


import com.gladtek.vaadin.services.AnalyticsService;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.Inline;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.VaadinService;

@Push
@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet("styles.css")
@PWA(name = "Star Wars Demo", shortName = "Star Wars")

public class Application implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        // Analytics Initialization: Check flag before global script injection
        AnalyticsService analyticsService = VaadinService.getCurrent().getInstantiator().getOrCreate(AnalyticsService.class);
        
        if (analyticsService.isEnabled()) {
            StringBuilder sb = new StringBuilder("<script");
            sb.append(" src=\"").append(analyticsService.getScriptUrl()).append("\"");
            sb.append(" data-site-id=\"").append(analyticsService.getSiteId()).append("\"");
            
            if (analyticsService.isWebVitalsEnabled()) sb.append(" data-web-vitals=\"true\"");
            
            sb.append(" async defer></script>");
            settings.addInlineWithContents(sb.toString(), Inline.Wrapping.AUTOMATIC);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
