FROM gradle:8.6-jdk21 AS build

WORKDIR /app

# Dependencies cachen (deze laag wordt hergebruikt zolang build.gradle niet verandert)
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon || true

# Dan pas de rest kopiëren en builden
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]