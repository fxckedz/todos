FROM maven:4.0.0-rc-4-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml ./

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY --from=build /app/${JAR_FILE} app.jar

RUN adduser -D todouser

RUN chown todouser:todouser /app

USER todouser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]