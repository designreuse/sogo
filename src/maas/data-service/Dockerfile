FROM openjdk:8u111-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/data-service*.war /app/data-service.war

EXPOSE 8085
ENTRYPOINT exec java -jar /app/data-service.war --spring.profiles.active=docker