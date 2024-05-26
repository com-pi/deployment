FROM openjdk:17
COPY api-gateway.jar /usr/src/myapp/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${active_profile}", "-jar", "/usr/src/myapp/app.jar"]
EXPOSE 8000
WORKDIR /usr/src/myapp