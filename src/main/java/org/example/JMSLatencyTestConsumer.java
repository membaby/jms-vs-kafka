package org.example;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.*;

public class JMSLatencyTestConsumer {
    public static void main(String[] args) {
        // JMS Setup
        String brokerURL = "tcp://localhost:61616";
        String queueName = "performanceTestQueue";

        // Set up the connection factory and connection
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create session and queue
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);

            // Create a message consumer
            MessageConsumer consumer = session.createConsumer(queue);

            // Start measuring response time
            System.out.println("Ready to receive messages...");

            List<Long> latencies = new ArrayList<>();
            int received = 0;
            while (received < 10000) {
                TextMessage msg = (TextMessage) consumer.receive(5000);
                if (msg == null) break;

                long sentTime = Long.parseLong(msg.getText());
                long latency = System.nanoTime() - sentTime;
                System.out.println("Received message with latency: " + latency / 1_000_000 + " ms");
                latencies.add(latency);
                received++;
            }

            Collections.sort(latencies);
            long median = latencies.get(latencies.size() / 2);

            System.out.println("Median latency: " + median + " ms");

            // Clean up and close resources
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}