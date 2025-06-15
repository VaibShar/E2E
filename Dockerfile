# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy everything to /app
COPY . .

# Make gradlew executable
RUN chmod +x gradlew

# Build the project
RUN ./gradlew build --no-daemon

# Expose Spring Boot port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "build/libs/*.jar"]
