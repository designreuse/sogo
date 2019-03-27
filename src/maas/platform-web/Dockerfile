FROM openjdk:8u111-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/platform-web*.jar /app/platform-web.jar

EXPOSE 8083
ENTRYPOINT exec java -jar /app/platform-web.jar --spring.profiles.active=docker