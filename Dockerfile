# Use official JDK image as base
FROM openjdk:17 AS build

# Set working directory
WORKDIR /app

# Copy Gradle files first to leverage caching
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle/

# Make gradlew executable
COPY gradlew .
RUN chmod +x gradlew

# Download dependencies (cache this layer)
RUN ./gradlew build --no-daemon --stacktrace

# Copy the rest of the application code
COPY src ./src

# Build the application
RUN ./gradlew bootJar --no-daemon --stacktrace

# Final image
FROM openjdk:17

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/build/libs/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
