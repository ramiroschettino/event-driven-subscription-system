FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
# We run this context from the root folder of the project in docker-compose
COPY pom.xml .
COPY membership-api/pom.xml membership-api/
COPY payment-api/pom.xml payment-api/
RUN mvn dependency:go-offline -B -DskipTests

# Copy source and build
COPY membership-api/src membership-api/src
RUN mvn -pl membership-api clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/membership-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
