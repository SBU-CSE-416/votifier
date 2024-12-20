#
# Build stage
#
FROM maven:3.9.9-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:17-jre
COPY --from=build /home/app/target/server-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]