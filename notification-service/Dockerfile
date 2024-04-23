FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
RUN apk add --no-cache tzdata
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]