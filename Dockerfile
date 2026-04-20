FROM maven:3.9-eclipse-temurin-21-alpine AS build
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
COPY pom.xml $HOME
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline

COPY src $HOME/src
RUN --mount=type=cache,target=/root/.m2 \
     mvn clean package -DskipTests

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,jdk.unsupported,java.net.http,java.scripting,java.rmi,java.xml,java.prefs,jdk.zipfs \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM alpine:latest
ENV JAVA_HOME=/opt/java-runtime
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=build /javaruntime $JAVA_HOME
COPY --from=build /app/target/*.jar app.jar

# Rybbit Analytics Configuration (can be overridden at runtime)
ENV RYBBIT_ANALYTICS_ENABLED=false
ENV RYBBIT_ANALYTICS_SITE_ID=RYBBIT-DEMO-ID
ENV RYBBIT_ANALYTICS_SCRIPT_URL=https://analytics.example.com/api/script.js
ENV RYBBIT_ANALYTICS_ENABLE_WEB_VITALS=false

# App Metadata Configuration
ENV APP_META_OG_TITLE="Vaadin 25 Star Wars Demo"
ENV APP_META_OG_DESCRIPTION="A Star Wars-themed demo showcasing Vaadin 25 Signals, dynamic light/dark modes, and full internationalization (i18n) with RTL support."
ENV APP_META_OG_IMAGE="https://starwars.gladtek.com/icons/icon.png"
ENV APP_META_OG_URL="https://starwars.gladtek.com"
ENV APP_META_OG_TYPE="website"

ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]