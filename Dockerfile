FROM gradle:7.3.3-jdk11 as gradlebuilder-clean
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN ./gradlew bootJar -DskipTests


FROM azul/zulu-openjdk-alpine:11.0.13-jre
RUN mkdir /app
COPY --from=gradlebuilder-clean ./project/build/libs/bayztracker-0.0.1-SNAPSHOT.jar /app/bayztracker-0.0.1-SNAPSHOT.jar
WORKDIR /app
CMD ["java" ,"-jar" ,"bayztracker-0.0.1-SNAPSHOT.jar"]
