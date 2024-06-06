# Start with a base image containing Java runtime
FROM amazoncorretto:17-alpine-jdk

# Create a directory
WORKDIR /app

# Copy all the files from the current directory to the image
COPY . .

# Install Kafka
RUN apk add --no-cache wget tar bash
RUN wget https://archive.apache.org/dist/kafka/3.1.0/kafka_2.13-3.1.0.tgz
RUN tar -xzf kafka_2.13-3.1.0.tgz
RUN rm kafka_2.13-3.1.0.tgz
RUN mv kafka_2.13-3.1.0 kafka

# build the project avoiding tests
RUN ./gradlew clean build -x test

# Expose port 8080
EXPOSE 8080

# Run the jar file
CMD ["sh", "-c", "./kafka/bin/zookeeper-server-start.sh ./kafka/config/zookeeper.properties & ./kafka/bin/kafka-server-start.sh ./kafka/config/server.properties & java -jar ./build/libs/catalog-0.0.1-SNAPSHOT.jar"]