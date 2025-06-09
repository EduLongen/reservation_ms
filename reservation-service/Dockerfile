FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/reservation-service*.jar /app/reservation-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "reservation-service.jar"] 