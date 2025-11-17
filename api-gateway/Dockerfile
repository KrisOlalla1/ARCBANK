# Etapa 1: Construir el .jar con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Crear la imagen final ligera
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# El gateway escucha en el puerto 8085 según su application.yml
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
