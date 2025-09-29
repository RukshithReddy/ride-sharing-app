# --- Stage 1: Build the application with Maven ---
FROM maven:3.9-eclipse-temurin-19 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Build the project and create the JAR file
RUN mvn clean install -DskipTests

# --- Stage 2: Create the final, lightweight image ---
FROM openjdk:19-slim

WORKDIR /app

# Copy only the JAR file from the 'build' stage
COPY --from=build /app/target/rapo-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]