# Stage 1: Build the application
FROM gradle:latest AS builder
WORKDIR /app

# Copy gradle wrapper and project files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# Make gradlew executable and build the project (skip tests to speed up)
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# Stage 2: Run the application
# Sử dụng openjdk:latest để đảm bảo tương thích với Java 25 như cấu hình trong build.gradle
FROM openjdk:latest
WORKDIR /app

# Copy built JAR from the builder stage
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
