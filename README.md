# Kafka Basics
This is meant to teach the Kafka basics and get started with the guide.

### What is Kafka?
_Kafka is an open-source, distributed event stream platform that collects, stores, and processes real-time data streams at scale._

Let's break down the Kafka definition.
- **Open-source**: A software/code that is free to use and for possible modification and redistribution.
- **Distributed**: Kafka is a distributed system that runs on more than one system, inter-connected by the computer networks.
- **Event stream**: A continuous flow of events. An 'event' can be anything, for example, a click of a link on a website, a Temperature change etc.
- **Collect**: Collect data from source, in Kafka it's called **_Producer_**.

> [!NOTE]
> Kafka was originally developed by [LinkedIn](https://www.linkedin.com/) and made open source in 2011.

## Kafka Architecture
<p align="center">
  <img src="./images/kafka-architecture.png?raw=true" alt="Kafka Architecture"/>
</p>

- [Kafka Topic](#kafka-topic)
- [Kafka Partition](#kafka-partition)
- [Kafka Broker](#kafka-broker)
- [Kafka Producer](#kafka-producer)
- [Kafka Consumer](#kafka-consumer)
- [Kafka Consumer Group](#kafka-consumer-group)
- [Kafka Offsets](#kafka-offsets)

### Kafka Topic
- A _Topic_ in Kafka is like a table in RDMS which stores similar things.
- The messages or events are immutable in Kafka's topic.
- We can think of Kafka topics as log files.

### Kafka Partition
- A topic is divided into partitions to provide redundancy and scalability.
- Each partition can be hosted on a different server, meaning a single topic can be scaled horizontally across multiple servers to provide performance.
- Partitions can be replicated, such that different servers will store a copy of the same partition in case one server.
- The producer will decide target partition to place any message, depending on:
  - Partition ID, if it's specified within the message
  - **key % num** partitions, if no partition ID is mentioned
  - Round robin if neither partition ID nor message key is available in the message means only the value is available

### Kafka Broker
A Kafka broker is a machine running a Kafka process, in short -
- A computer instance or container running a Kafka process.
- It manages partitions
- Handle read-write requests

**Kafka Cluster**: a set of Kafka brokers in a network known as a Kafka cluster.

### Kafka Producer

### Kafka Consumer

### Kafka Consumer Group

### Kafka Offsets


## Kafka Local Setup
Download the Kafka from Official [Apache Kafka](https://kafka.apache.org/downloads)
  ```
  $ tar -xzf kafka_2.13-3.7.1.tgz
  $ cd kafka_2.13-3.7.1
  ```
> [!Note]
> Kafka broker can be started in 2 ways:
- Using Zookeeper: Zookeeper manages and stores meta-data information of the Kafka broker/cluster.
- Using Kraft: In this mode, Kafka itself manages Kafka brokers/clusters information.

### Using Zookeeper
To start the zookeeper service, run the following command in the terminal

```Bash
# Start the ZooKeeper service
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```
Once the zookeeper service starts successfully then, run the following command in the terminal to start the Kafka service/broker
```Bash
# Start the Kafka broker service
$ bin/kafka-server-start.sh config/server.properties
```
> [!Note]
> Kafka broker runs on the port 9092 by default. Look for this log message
```
 Registered broker 0 at path /brokers/ids/0 with addresses: PLAINTEXT://localhost:9092
```
### Using Kraft
Kafka can be run using KRaft mode using local scripts and downloaded files.
There are 3 steps
```Bash
# 1. Generate a cluster UUID 
$ KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

# 2. Format Log Directories
$ bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties

#3. Start the Kafka Server
$ bin/kafka-server-start.sh config/kraft/server.properties
```
> [!Note]
> Make sure to stop the zookeeper and Kafka service that you started previously using zookeeper 

### Create a topic
Let's create a kafka topic called **sports-news** which stores all the news related to sports.
```bash
# In another terminal

# Create a topic
$ bin/kafka-topics.sh --create --topic sports-news --bootstrap-server localhost:9092

# Optionally, run the kafka-topics.sh command to display the topic's usage information.
$ bin/kafka-topics.sh --describe --topic sports-news --bootstrap-server localhost:9092

# Output will be:
Topic: sports-news	TopicId: hsXYFr4IQzOrLkmL2cRVFQ	PartitionCount: 1	ReplicationFactor: 1	Configs:
	Topic: sports-news	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
```

### Write some sports events into the topic
Run the console producer client to write a few events into your topic. By default, each line you enter will result in a separate event being written to the topic.
```bash
$ bin/kafka-console-producer.sh --topic sports-news --bootstrap-server localhost:9092
>India into the Cricket World Cup final
>India won the Cricket World Cup 2024
>Roger Federer won the singles Australian Open
```

### Consume a Message
Open another terminal session and run the console consumer client to read the events you just created
```bash
$ bin/kafka-console-consumer.sh --topic sports-news --from-beginning --bootstrap-server localhost:9092
India into the cricket World Cup final 
India won the Cricket World Cup 2024
Roger Federer won the singles Australian Open

# The above messages are being read from the topic.
# Where:  --from-beginning  flag tells the consumer to read all the messages from the beginning
```

## Kafka Producer/Consumer using spring-boot
With console Producer/Consumer, we learnt how to create a topic and read/write events from the topic.
Let's make our hands dirty and try to demonstrate Kafka's producer and consumer example using spring-boot.
<div align = "center">
<img width="720" alt="image" src="https://github.com/user-attachments/assets/c7fa7c0b-68a9-40ef-a78e-07fa3151c6d0">
</div>

### Producer App
Create a spring-boot web application using [spring initializer](https://start.spring.io/) and make sure to add spring-Kafka dependency.
```coffeescript
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
</dependency>

<!-- By default 'spring-kafka-test' dependency will be added -->
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka-test</artifactId>
  <scope>test</scope>
</dependency>
```
Edit the Configuration of the application.yml file to add Kafka broker info like bootstrap-server, topic name etc
```yaml
# we can declare the properties related to this application
myapp:
  topic: sports-news

spring:
  application:
    name: kafka-producer

  kafka:
    producer:
      bootstrap-servers:
        - localhost:9092 # can specify the multiple broker address
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```
Kafka event is key-value.
The **key-serializer** and **value-serializer** are used to specify which type of serialization is used for key and value respectively.
sports-news : Kafka topic name that we created before.

**Producer Code**
The `sendMessage()` takes a message from the client, here we use a simple rest controller to send the message for demonstration purposes. Inside the `sendMessage()` method we invoke the kafkaTemplate's `send()` method to post a message to the kafka topic. The response from the `send()` is a `CompletableFuture` so invoking the `whenComplete()` callback to read the response.
```java
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${myapp.topic}")
    private String topicName;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> status = kafkaTemplate.send(topicName, message);
        status.whenComplete((result, throwable) -> {
            if (throwable == null) {
                System.out.println("Sent message [" + message + "] " +
                        "with offset [" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message to kafka topic [" + topicName + "] " +
                        "due to " + throwable.getMessage());
            }
        });
    }
}
```
Sample rest controller were we post some messages

```java
import com.raj.producer.service.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/producer")
public class KafkaProducerController {


    private final KafkaMessagePublisher kafkaMessagePublisher;

    @PostMapping
    public ResponseEntity<String> producerMsg(@RequestParam("message") String message) {
        kafkaMessagePublisher.sendMessage(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
```
### Consumer App
Create a spring-boot web application using [spring initializer](https://start.spring.io/) and make sure to add spring-Kafka dependency.

Configure the application.yml file of the consumer app read the events from the Kafka topic.
```yaml
server:
  port: 8081

spring:
  application:
    name: kafka-consumer

  kafka:
    consumer:
      bootstrap-servers:
        - localhost:9092
      group-id: sp-group-1 # This tells that in which consumer group, the consumer belongs to.
```
Sample java code
```java
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {


    @KafkaListener(topics = "sports-news", groupId = "sp-group-1")
    public void consume(String message) {

       log.info("Message consume : {}",message);
    }
}
```

