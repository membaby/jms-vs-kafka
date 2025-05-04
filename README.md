# JMS vs Kafka Performance Lab

This repository contains source code and testing scripts used to benchmark and compare **Java Message Service (JMS)** with **Apache Kafka** as part of a distributed data-intensive applications (DDIA) lab.

## 📄 Lab Objectives
- Compare **response time**, **median latency**, and **maximum throughput** between JMS and Kafka.
- Evaluate **usability** (setup complexity and API verbosity).
- Research **integration support** with external tools and ecosystems.

---

## 🧪 Experiments Included

### ✅ JMS (ActiveMQ)
- `JMSThroughputTest.java`: Measures producer-side message throughput.
- `JMSConsumerThroughputTest.java`: Measures consumer throughput.
- `JMSLatencyTest.java`: Measures end-to-end latency by embedding timestamps.

### ✅ Kafka
> **All Kafka code contributions were authored by [Sama Zayed](https://github.com/samatarekzayed).**

- `KafkaThroughputTest.java`: Producer-side Java-based throughput benchmark.
- `KafkaLatencyTest.java`: Measures message latency using timestamped payloads.
- Kafka CLI Tools:
  - `kafka-producer-perf-test.sh`
  - `kafka-consumer-perf-test.sh`

---

## 📊 Results Summary
| Metric        | JMS                | Kafka                        |
|---------------|---------------------|-------------------------------|
| Producer Throughput | ~305 msg/sec         | ~241,094 msg/sec              |
| Consumer Throughput | ~15,090 msg/sec      | ~193,583 msg/sec (concurrent) |
| Median Latency      | ~0.196 ms            | ~917 ms (with default config) |

Full results and interpretation are available in the `report.pdf` or the `summary.txt` file.

---

## ⚙️ Setup Instructions

### JMS (ActiveMQ)
1. Install Java 17
2. Install ActiveMQ (e.g., via `brew install activemq`)
3. Add dependencies in `pom.xml`
4. Run the producer and consumer using Maven:
   ```bash
   mvn compile exec:java -Dexec.mainClass="org.example.JMSThroughputTest"
   ```

### Kafka (KRaft mode)
1. Download and extract Kafka
2. Initialize and start the broker using KRaft mode:
   ```bash
   export KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"
   bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties
   bin/kafka-server-start.sh config/kraft/server.properties
   ```
3. Create a topic:
   ```bash
   bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
   ```
4. Run the test classes or CLI tools.

---

## 📚 References
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [ActiveMQ Documentation](https://activemq.apache.org/)
- Kafka performance tool source: `producer-performance` and `consumer-performance` utilities

---

## 🧠 Author & License
Created by [Mahmoud Embaby](https://github.com/mahembaby) for academic benchmarking purposes.

Distributed under the MIT License.
