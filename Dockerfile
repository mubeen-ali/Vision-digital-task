FROM openjdk:8
EXPOSE 8080
ADD target/mubeen-assessment.jar mubeen-assessment.jar
RUN mkdir -p /opt/conf
COPY app-configs.properties /opt/conf/app-configs.properties
ENTRYPOINT ["java","-jar","mubeen-assessment.jar",  "--spring.config.location=file:/opt/conf/app-configs.properties"]