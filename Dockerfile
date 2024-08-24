# Use an official OpenJDK 20 runtime as a parent image
FROM openjdk:20-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file first
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Run the Maven build for dependencies
RUN ./mvnw dependency:resolve

# Copy the rest of the project files
COPY src ./src

# Build the project using Maven
RUN ./mvnw clean install

# Run the Spring Boot app
CMD ["java", "-jar", "target/barter-0.0.1-SNAPSHOT.war"]
