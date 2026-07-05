# ===== Build Stage =====
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy Maven files first (better layer caching)
COPY pom.xml .
COPY src src

# Install Maven and build the jar (skip tests for faster deploy)
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests && \
    apt-get clean

# ===== Run Stage =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render provides the PORT env variable automatically
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]