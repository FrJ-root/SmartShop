FROM maven:3.9.1-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean compile

FROM eclipse-temurin:17
WORKDIR /app

COPY --from=build /app/target/classes ./classes
ENTRYPOINT ["java", "-cp", "classes", "org.SmartShop.Main"]