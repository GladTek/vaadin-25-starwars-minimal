FROM maven:3.9-eclipse-temurin-21 AS build
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
COPY pom.xml $HOME
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline

COPY src $HOME/src
RUN --mount=type=cache,target=/root/.m2 \
     mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
