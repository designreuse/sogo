FROM openjdk:8u111-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/discovery-service*.jar /app/discovery-service.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/discovery-service.jar", "--spring.profiles.active=docker"]
EXPOSE 8761