FROM openjdk:8u111-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/node-web*.jar /app/node-web.jar

EXPOSE 8084
ENTRYPOINT exec java -jar /app/node-web.jar --spring.profiles.active=docker