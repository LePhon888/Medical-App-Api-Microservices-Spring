FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/*.jar spring-boot-server.jar
ENTRYPOINT ["java","-jar","/spring-boot-server.jar"]