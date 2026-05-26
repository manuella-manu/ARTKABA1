# Étape 1 : Compilation avec Maven et Java 17 (Temurin)
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application avec l'image moderne Eclipse Temurin
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/artkaba-0.0.1-SNAPSHOT.jar artkaba.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "artkaba.jar"]