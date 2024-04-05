FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
RUN apk add --no-cache tzdata
COPY ./target/notification-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]