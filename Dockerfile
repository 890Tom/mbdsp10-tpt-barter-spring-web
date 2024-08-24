# Use an official OpenJDK runtime as a parent image
FROM openjdk:20-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the project
RUN ./mvnw clean install

# Run the Spring Boot app
CMD ["java", "-jar", "target/barter-0.0.1-SNAPSHOT.war"]
