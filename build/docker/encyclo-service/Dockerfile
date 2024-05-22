FROM openjdk:17
COPY auth-service.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
ENTRYPOINT ["java", "-Dspring.profiles.active=${active_profile}", "-jar", "/usr/src/myapp/app.jar"]
