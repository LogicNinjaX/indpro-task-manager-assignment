FROM amazoncorretto:17
LABEL authors="NITISH KUMAR"

WORKDIR /app

COPY /target/task-manager-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]