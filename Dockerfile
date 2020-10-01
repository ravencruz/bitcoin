#
# Build stage
#
FROM gradle:6.6.1-jdk11 as build
LABEL maintainer="jsebas.ct@gmail.com"
WORKDIR /home/app

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

COPY src ./src
RUN gradle build
RUN ls ./build/libs

#
# run stage
#
FROM openjdk:11.0.8-jdk
WORKDIR /home/app
COPY --from=build /home/app/build/libs/coin-0.0.1-SNAPSHOT.jar ./coin.jar
RUN chmod 775 ./coin.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "coin.jar"]