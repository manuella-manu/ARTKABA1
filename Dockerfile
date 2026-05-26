# Étape 1 : Compilation avec Maven et Java 17
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l'application
FROM openjdk:17-jdk-slim
COPY --from=build /target/artkaba-0.0.1-SNAPSHOT.jar artkaba.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "artkaba.jar"]