FROM openjdk:17
COPY discovery-eureka.jar /usr/src/myapp/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${active_profile}", "-jar", "/usr/src/myapp/app.jar"]
EXPOSE 8761
WORKDIR /usr/src/myapp