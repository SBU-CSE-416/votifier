#
# Build stage
#
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /home/app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:17-jre

RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates && rm -rf /var/lib/apt/lists/*

COPY --from=build /home/app/target/server-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/usr/local/lib/demo.jar"]
