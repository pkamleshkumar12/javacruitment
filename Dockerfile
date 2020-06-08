FROM openjdk:14-alpine
ADD build/libs/javacruitment-0.0.1.jar javacruitment-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "javacruitment-0.0.1.jar"]

