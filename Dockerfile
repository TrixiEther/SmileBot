FROM openjdk:11
MAINTAINER trixiether@gmail.com
COPY build/libs/\*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

