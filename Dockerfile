FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY . .
ARG SERVICE_MODULE=welcome_service
RUN mvn -B -DskipTests -pl ${SERVICE_MODULE} -am package \
    && (cp /workspace/${SERVICE_MODULE}/target/*.jar /tmp/app.jar 2>/dev/null \
        || cp /workspace/${SERVICE_MODULE}/target/*.war /tmp/app.jar)

FROM eclipse-temurin:17-jre
WORKDIR /app
ARG SERVICE_MODULE=welcome_service
COPY --from=build /tmp/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
