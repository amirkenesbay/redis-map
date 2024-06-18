FROM openjdk:17-jdk-slim

LABEL mentainer="amirkenesbay@gmail.com"

WORKDIR /app

COPY build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]