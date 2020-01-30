ARG VERSION


FROM maven:3.5-jdk-8-alpine
COPY src /src
COPY pom.xml /pom.xml
COPY conf /conf
RUN mvn package -DskipTests


FROM openjdk:8
COPY --from=0 / /
ENTRYPOINT java -jar target/janusbench-0.0.1-jar-with-dependencies.jar
