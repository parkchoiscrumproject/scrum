FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-jar","/docker-springboot.jar"]s