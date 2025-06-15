# Use a Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy Gradle files and source code
COPY . .

# Build the project
RUN ./gradlew build --no-daemon

# Expose default Spring Boot port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "build/libs/*.jar"]
