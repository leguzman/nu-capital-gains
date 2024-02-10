# Build stage
FROM gradle:8.6.0-jdk21-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon 

# Package stage
FROM eclipse-temurin:21-jdk-alpine 
COPY --from=build /home/gradle/src/app/build/libs/app.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]