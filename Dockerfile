# Build stage
FROM gradle:8.6-jdk21 AS build

WORKDIR /app

# Kopieer alles (gradlew, gradle-wrapper.jar, build.gradle, settings.gradle, src/)
COPY . .

# Build zonder tests
RUN ./gradlew build -x test

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Kopieer de JAR uit de Gradle build
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
