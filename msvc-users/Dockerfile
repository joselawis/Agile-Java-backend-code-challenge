# Build stage
FROM amazoncorretto:21-alpine AS builder
WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:21-alpine
WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /app/target/msvc-users-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 9080

# Run the application
CMD ["java", "-jar", "app.jar"]