# Build Stage
FROM gradle:8.7-jdk21 AS build

WORKDIR /home/gradle/app

# Copy Gradle wrapper and build files
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

# Copy the config directory
COPY config ./config

# Download dependencies (without testing)
RUN ./gradlew --no-daemon build -x test || true

# Copy the source code
COPY src ./src

# Build the application as a Spring Boot fat JAR
RUN ./gradlew --no-daemon clean build -x test -x checkstyleTest

# Extract layers using Spring Boot's layertools
RUN java -Djarmode=tools -jar build/libs/*SNAPSHOT.jar extract --layers --launcher

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install shadow package to get groupadd and useradd utilities
RUN apk add --no-cache shadow && \
    groupadd -g 1000 appgroup && \
    useradd -u 1000 -g appgroup -s /bin/sh -d /app appuser && \
    chown -R appuser:appgroup /app

# Copy Spring Boot layers from the build stage to the runtime image
COPY --from=build /home/gradle/app/*SNAPSHOT/dependencies/ ./
COPY --from=build /home/gradle/app/*SNAPSHOT/snapshot-dependencies/ ./
COPY --from=build /home/gradle/app/*SNAPSHOT/spring-boot-loader/ ./
COPY --from=build /home/gradle/app/*SNAPSHOT/application/ ./

# Switch to non-root user
USER appuser

EXPOSE 8080

# Run the application using Spring Boot's JarLauncher
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]