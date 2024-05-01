FROM openjdk:17
COPY board-service.jar /usr/src/myapp/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${active_profile}", "-jar", "/usr/src/myapp/app.jar"]
EXPOSE 8082
WORKDIR /usr/src/myapp