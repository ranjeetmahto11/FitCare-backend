# Use Java 21
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Install Maven and build
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

# Run the app
EXPOSE 8081
CMD ["java", "-jar", \
     "target/fitcare-1.0.0.jar", \
     "--spring.profiles.active=prod"]