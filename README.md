# Vaadin 25 Star Wars Demo Application

![GitHub Release](https://img.shields.io/github/v/release/GladTek/vaadin-25-starwars-minimal?color=blue&label=version)
![License](https://img.shields.io/github/license/GladTek/vaadin-25-starwars-minimal?color=green)
![Java Version](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Vaadin Version](https://img.shields.io/badge/Vaadin-25.2.1-blueviolet?logo=vaadin)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-brightgreen?logo=springboot&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-Built_with-646CFF?logo=vite&logoColor=white)
![Docker Pulls](https://img.shields.io/docker/pulls/achaabni/vaadin-starwars?color=blue&logo=docker)

A modern small demo application built with Vaadin 25 and Spring Boot, demonstrating advanced UI concepts, dynamic scheming dark/light mode, and full internationalization within a Star Wars-themed context.



### 🌐 Live Demo
Check out the live application here: [https://starwars.gladtek.com/](https://starwars.gladtek.com/)

> **Note**: This demo is hosted on Render's free tier. If the application has been inactive for a while, it may take a minute to wake up. Please be patient! ⏳
## How to Run

### Prerequisites
*   JDK 21
*   Maven 3.x

### Run locally

```bash
mvn clean spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

## 1. Functional Features

### Entry & Navigation
*   **Split Screen Entry**: A unique landing page requiring users to choose their path: "Light Side" or "Dark Side".
*   **User Identity**: In this demo, a user is defined not by a login, but uniquely by their choice of side (Dark/Light).
*   **Deep Linking**: Smart redirection ensures that if you access a specific link (e.g., `/planets`) before logging in, you are automatically taken there after selecting your side.
*   **Persistent Navigation**: A responsive side drawer navigation menu available throughout the application.

### Core Modules
*   **Dashboard**: A responsive dashboard overview with key performance indicators (KPIs).
*   **Planets**:
    *   Interactive data grid displaying Star Wars planets.
    *   **Master-Detail View**: Select a planet to see full details in a split-view layout.
    *   **Real-time Reactivity**: Leverages **Vaadin Signals** for live population updates across the dashboard and planet screens.
    *   **Localized Data**: Planet names, climates, and terrains are fully translated.
*   **Components Showcase**: A comprehensive gallery of customized Vaadin components:
    *   **Inputs**: Text fields, date pickers (with localized "Today"/"Cancel" buttons), time pickers.
    *   **Cards**: Standard cards and advanced variants with media images, badges, and footer actions.
    *   **Display**: Grids, Tabs, Accordions, and Dialogs.
    *   **Experimental**: Showcases upcoming Vaadin features like **Badges**, **Sliders**, and **RangeSliders** (enabled via feature flags).

### Internationalization (i18n)
*   **Multi-language Support**: Full support for **English**, **French**, **German**, and **Arabic**.
*   **RTL Support**: Automatic Right-to-Left (RTL) layout adjustment when switching to Arabic.
*   **Localized Components**: All UI elements, including complex components like the `DatePicker` (months, weekdays, buttons), are dynamically localized.

### Theming & UX
*   **Side-based Styling**: The application's visual density and color palette adapt based on the user's initial "Light" or "Dark" side selection.
*   **Dynamic Trend Indicators**: Population numbers dynamically shift to **Green** (increase) or **Red** (decrease) in real-time.
*   **404 Page**: A custom, localized "Page Not Found" experience that guides users back to safety.

---

## 2. Technical Overview

### Technology Stack
*   **Framework**: Vaadin Flow 25.2.1
*   **Backend**: Spring Boot 4.1.0 (Java 21)
*   **Build Tool**: Maven

### Architecture & Patterns
*   **Layout Architecture**:
    *   `MainLayout`: Implements `AppLayout` for the global shell, handling navigation and global theme/locale state.
    *   `SplitScreenView`: The entry point managing the initial user decision.
*   **Session Management**:
    *   `UserSession` (`@VaadinSessionScope`): Manages user state (Selected Side, Active Theme, Intended Route) across the session lifecycle.
*   **Security & Routing**:
    *   **Anonymous Access**: Views are annotated with `@AnonymousAllowed` (demo mode).
    *   **Route Guards**: `MainLayout` implements `BeforeEnterObserver` to intercept unauthenticated users (no side selected) and redirect them to the entry page, effectively protecting inner views while preserving deep links via `UserSession.intendedRoute`.
    *   `NotFoundView`: Implements `HasErrorParameter<NotFoundException>` to catch and display 404 errors.
*   **Signal State Management**:
    *   **Full-Stack Reactivity**: Implements the new Vaadin Signals paradigm for efficient, real-time state synchronization between the server and client.
    *   **Shared Signals**: Utilizes `SharedValueSignal` in `PlanetService` to ensure stable, session-isolated state sharing across multiple concurrent users.
*   **Simulation Engine**:
    *   A Spring-managed `@Scheduled` task in `PlanetService` simulates planetary population growth every 5 seconds, pushing updates to the UI via signals.

### Key Components & Utilities
*   **`DatePickerI18nUtil`**: A specialized utility that generates `DatePicker.DatePickerI18n` configurations dynamically from the active `Locale`, handling specific formats (e.g., `ar-TN` for Arabic) and loading translation resources.
*   **`LanguageHelper`**: Centralized logic for determining layout direction (`Direction.RIGHT_TO_LEFT` vs `LEFT_TO_RIGHT`) based on the language.
*   **`ComponentsView`**: A modular "Kitchen Sink" view demonstrating component customization. It is refactored into distinct sections for better maintainability:
    *   `InputSection`: Text fields, DatePickers, etc.
    *   `ButtonSection`: Button variants, responsive icon button rows, custom spacers, and styles replacing Lumo variables.
    *   `SelectionSection`: ComboBoxes, Selects, Radios.
    *   `DisplaySection`: Grids, Tabs, Accordions.
    *   `CardSection`: Standard and advanced Cards.
    *   `DialogSection`: Dialogs and Notifications.
    *   `ExperimentalSection`: Demonstrates experimental components (Slider, RangeSlider) and new Badge variants.
*   **Monitoring & Analytics**:
    *   **Rybbit Analytics**: Integrated support for monitoring user engagement and performance vitals, configurable via environment variables in production.

### Localization Structure
*   Resources stored in `src/main/resources/vaadin-i18n/`.
*   Standard properties files: `translations.properties` (fallback), `_en`, `_fr`, `_de`, `_ar`.
*   Keys follow a structured naming convention (e.g., `components.datepicker.today`, `error.404.title`).

## 3. Containerization & Deployment

### Optimized Docker Build
The project includes a sample `Dockerfile`:
*   **Multi-Stage Build**: Uses a robust `maven` image for building and a lightweight `alpine` JRE for running.
*   **Layer Caching**: `pom.xml` is copied and dependencies downloaded *before* source code, allowing Docker to cache the heavy dependency layer.
*   **Buildkit Caching**: Leverages `--mount=type=cache` to speed up Maven builds.

### Official Docker Image
A pre-built Docker image is maintained on Docker Hub:
*   **Docker Image**: `achaabni/vaadin-starwars`
*   **Docker Hub Page**: [achaabni/vaadin-starwars](https://hub.docker.com/r/achaabni/vaadin-starwars)
*   **Run Locally**:
    ```bash
    docker run -p 8080:8080 achaabni/vaadin-starwars:latest
    ```

### CI/CD Workflow & Forking Guide

The repository includes a GitHub Actions workflow (`.github/workflows/docker-publish.yml`) that automates building and pushing the Docker image. If you fork this project and want to build/push your own Docker images, follow these steps:

#### 1. Configure GitHub Secrets
Go to your forked repository's settings under **Settings > Secrets and variables > Actions** and add the following repository secrets:
*   `DOCKER_USERNAME`: Your Docker Hub username.
*   `DOCKER_PASSWORD`: Your Docker Hub personal access token or password.

#### 2. Configure `version.json`
The workflow is driven by the `version.json` file in the root of the project:
```json
{
  "tag": "1.1.0",
  "update_latest": true,
  "push": true,
  "image_name": "vaadin-starwars"
}
```
*   `tag`: The version string applied to the built Docker image (e.g. `1.1.0`).
*   `update_latest`: If set to `true`, the workflow will also tag the built image as `latest`.
*   `push`: Set to `true` to authenticate and push the image to Docker Hub under your username (i.e. `your_docker_username/vaadin-starwars:1.1.0`). Set to `false` if you only want the workflow to run a test build without pushing.
*   `image_name`: The name of the Docker repository/image to create.

#### 3. Triggering the Workflow
*   The workflow triggers automatically on every push to the `main` branch.
*   It can also be run manually via the **Actions** tab using the `workflow_dispatch` event.

