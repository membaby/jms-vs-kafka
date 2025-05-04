package org.example;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumerRT {
    public static void main(String[] args) {
        // JMS Setup
        String brokerURL = "tcp://localhost:61616";
        String queueName = "performanceTestQueue";

        int messageCount = 1000;
        long[] responseTimes = new long[messageCount];

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

            // Measure response time for receiving a message

            for (int i = 0; i < messageCount; i++) {
                long start = System.currentTimeMillis();
                Message msg = consumer.receive(); // Wait for a message
                if (msg instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) msg;
                    String text = textMessage.getText();
                } else {
                    System.out.println("Received non-text message");
                }
                long responseTimeInMillis = System.currentTimeMillis() - start;
                responseTimes[i] = responseTimeInMillis;
            }

            // Clean up and close resources
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        // Calculate average response time
        long totalResponseTime = 0;
        for (long responseTime : responseTimes) {
            totalResponseTime += responseTime;
        }
        double averageResponseTime = (double) totalResponseTime / messageCount;
        System.out.println("Average response time: " + averageResponseTime + " ms");

        java.util.Arrays.sort(responseTimes);
        long medianMs = responseTimes[responseTimes.length/2];
        System.out.println("Median response time: " + medianMs + " ms");

    }
}