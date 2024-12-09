# Base image olarak OpenJDK kullanılır
FROM openjdk:17-jdk-slim

# Uygulama JAR dosyasının kopyalanacağı çalışma dizinini ayarla
WORKDIR /app

# Maven/Gradle build sırasında oluşturulan JAR dosyasını konteynere kopyala
COPY target/courierTracking-0.0.1-SNAPSHOT.jar app.jar

# Spring Boot uygulamasını başlat
ENTRYPOINT ["java", "-jar", "app.jar"]

# Uygulamanın çalışacağı port
EXPOSE 8080
