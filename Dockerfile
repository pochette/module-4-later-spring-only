FROM amazoncorretto:21-alpine-jdk
LABEL authors="burda"
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]