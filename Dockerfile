# Use an official lightweight Java 19 runtime as a parent image
FROM openjdk:19-slim

# Set the working directory in the container to /app
WORKDIR /app

# Copy the compiled JAR file from your project's target folder into the container
COPY target/rapo-0.0.1-SNAPSHOT.jar app.jar

# Tell Docker to run this command when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]