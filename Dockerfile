# Koristi službenu JDK sliku kao osnovu
FROM openjdk:17 AS build

# Postavi radni direktorij
WORKDIR /app

# Uključivanje gradle wrappera
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Daje pravo izvršenja
RUN chmod +x gradlew

# Izgradnja aplikacije
RUN ./gradlew build --no-daemon


# Kopiraj build.gradle i settings.gradle
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Preuzmi ovisnosti (cache)
RUN ./gradlew build --no-daemon --stacktrace --info

# Kopiraj izvorni kod
COPY src ./src

# Izgradi aplikaciju
RUN ./gradlew bootJar --no-daemon --stacktrace

# Stvori konačni Docker image
FROM openjdk:17

# Postavi radni direktorij
WORKDIR /app

# Kopiraj JAR datoteku iz build faze
COPY --from=build /app/build/libs/*.jar app.jar

# Pokreni aplikaciju
ENTRYPOINT ["java", "-jar", "app.jar"]
