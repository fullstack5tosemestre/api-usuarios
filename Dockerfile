FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/usuario.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

# Build image:
# docker build -t xdainz/api-usuario .
