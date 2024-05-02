FROM openjdk:17-jdk-alpine
RUN apk add --no-cache tzdata
ARG JAR_FILE=target/*.jar
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
ENV TZ=Asia/Bangkok