# Build stage
FROM mcr.microsoft.com/openjdk/jdk:17-mariner AS build
ENV JAR_FILE=app-lebe-backend-api-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY mvnw /app/
COPY .mvn /app/.mvn
COPY pom.xml /app
COPY src /app/src
RUN ls -la /app
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests

# Runtime stage
FROM mcr.microsoft.com/openjdk/jdk:17-mariner
COPY --from=build /app/target/app-lebe-backend-api-0.0.1-SNAPSHOT.jar /usr/src/myapp/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/usr/src/myapp/app.jar"]