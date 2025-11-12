# # Multi-stage build for Spring Boot application
#
# # Stage 1: Build the application
# FROM gradle:8.5-jdk21 AS build
#
# WORKDIR /app
#
# # Copy gradle files first for better layer caching
# COPY gradle gradle
# COPY gradlew gradlew.bat build.gradle settings.gradle ./
#
# # Download dependencies (this layer will be cached if dependencies don't change)
# RUN gradle dependencies --no-daemon
#
# # Copy source code
# COPY src src
#
# # Build the application (skip tests for faster builds, run them in CI/CD)
# RUN gradle bootJar --no-daemon -x test
#
# # Stage 2: Create the runtime image
# FROM eclipse-temurin:21-jre-alpine
#
# WORKDIR /app
#
# # Create a non-root user for security
# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring
#
# # Copy the JAR from build stage
# COPY --from=build /app/build/libs/*.jar app.jar
#
# # Expose the application port
# EXPOSE 8080
#
# # Health check
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
#
# # Run the application
# ENTRYPOINT ["java", "-jar", "app.jar"]


# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy gradle files first for better layer caching
COPY gradle gradle
COPY gradlew gradlew.bat build.gradle settings.gradle ./

# Download dependencies (this layer will be cached if dependencies don't change)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src src

# Build the application (skip tests for faster builds, run them in CI/CD)
RUN gradle bootJar --no-daemon -x test

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

