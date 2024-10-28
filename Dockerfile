# Use the official OpenJDK as a build stage
FROM openjdk:17 AS build

# Set the working directory
WORKDIR /app

# Copy Gradle files first to take advantage of Docker caching
COPY build.gradle settings.gradle ./
COPY gradle gradle/

# Copy the rest of the application
COPY src ./src

# Make gradlew executable and build the application
COPY gradlew ./
RUN chmod +x gradlew && ./gradlew build --no-daemon

# Final stage for the runnable image
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
