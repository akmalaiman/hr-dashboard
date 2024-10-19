FROM maven:3.9.7-eclipse-temurin-21 AS build

COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM arm64v8/eclipse-temurin:21-jre-jammy

COPY --from=build /usr/src/app/target/hr-dashboard-api-0.0.1-SNAPSHOT.jar /usr/app/hr-dashboard-api-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/app/hr-dashboard-api-0.0.1-SNAPSHOT.jar"]