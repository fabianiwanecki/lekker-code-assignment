FROM maven:3.9.8-amazoncorretto-17 AS build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17.0.11-alpine
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]