FROM maven:3.8.2-openjdk-8 as builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:8
WORKDIR /app
COPY --from=builder /app/target/test-0.0.1-SNAPSHOT.jar /app/test-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/test-0.0.1-SNAPSHOT.jar"]