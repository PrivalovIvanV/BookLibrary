FROM openjdk:11

#WORKDIR /app
ADD ./target/libraryProject-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT java -jar app.jar