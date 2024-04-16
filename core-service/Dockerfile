FROM openjdk:17-jdk-alpine
RUN apk add --no-cache tzdata
ARG JAR_FILE=target/*.jar
COPY ./target/core-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]