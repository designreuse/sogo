FROM openjdk:8u111-jdk-alpine
MAINTAINER tyj "tanyuanji@yihexinda.com"
RUN mkdir /app
WORKDIR /app
COPY target/config-service*.jar /app/config-service.jar
VOLUME /app/config

EXPOSE 8889
ENTRYPOINT exec java -jar /app/config-service.jar --spring.profiles.active=native,docker