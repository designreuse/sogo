FROM openjdk:8u111-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/auth-service*.war /app/auth-service.war

EXPOSE 8087
ENTRYPOINT exec java -jar /app/auth-service.war --spring.profiles.active=docker