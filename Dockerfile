FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/tenpo-0.0.1.jar app.jar
CMD ["java", "-jar", "app.jar"]