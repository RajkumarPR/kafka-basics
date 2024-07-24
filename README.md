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

### Kafka Architecture
<p align="center">
  <img src="./images/kafka-architecture.png?raw=true" alt="Kafka Architecture"/>
</p>

[Kafka Topic](#kafka-topic)  
[Kafka Partition](#kafka-partition)
[Kafka Broker](#kafka-broker)
[Kafka Producer](#kafka-producer)
[Kafka Consumer](#kafka-consumer)
[Kafka Consumer Group](#kafka-consumer-group)
[Kafka Offsets](#kafka-offsets)

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
A Kafka broker is a machine running a Kafka process, a set of Kafka brokers in a network known as a Kafka cluster. In short -
- A computer instance or container running a Kafka process.
- It manages partitions
- Handle read-write requests

### Kafka Producer

### Kafka Consumer

### Kafka Consumer Group

### Kafka Offsets





