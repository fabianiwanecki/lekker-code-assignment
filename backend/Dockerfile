FROM maven:3.9.8-amazoncorretto-21 AS build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.3-alpine3.19
ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]