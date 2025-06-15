# Use Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Make gradlew executable
RUN chmod +x gradlew

# Build the Spring Boot application
RUN ./gradlew build --no-daemon

# Expose the app port
EXPOSE 8080

# Start the Spring Boot app (use exact JAR name)
CMD ["java", "-jar", "build/libs/BillGenerator-0.0.1-SNAPSHOT.jar"]
