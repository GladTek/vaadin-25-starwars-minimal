package com.gladtek.vaadin;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet("styles.css")
@PWA(name = "Star Wars Demo", shortName = "Star Wars")
@Meta(name = "og:title", content = "Vaadin 25 Star Wars Demo")
@Meta(name = "og:description", content = "A modern small demo application built with Vaadin 25 and Spring Boot, demonstrating advanced UI concepts, dynamic scheming dark/light mode, and full internationalization within a Star Wars-themed context.")
@Meta(name = "og:image", content = "https://starwars.gladtek.com/icons/icon.png")
@Meta(name = "og:url", content = "https://starwars.gladtek.com")
@Meta(name = "og:type", content = "website")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
