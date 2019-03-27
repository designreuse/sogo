FROM openjdk:8u111-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/gateway-service*.jar /app/gateway-service.jar

EXPOSE 8000
ENTRYPOINT exec java -jar /app/gateway-service.jar --spring.profiles.active=docker