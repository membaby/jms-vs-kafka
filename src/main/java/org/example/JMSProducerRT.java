package org.example;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;


public class JMSProducerRT {
    public static void main(String[] args) {
        // Connection details
        String brokerURL = "tcp://localhost:61616";
        String queueName = "performanceTestQueue";
        String filePath = "data/message.txt";

        int messageCount = 1000;
        long[] responseTimes = new long[messageCount];

        try {
            String text = new String(Files.readAllBytes(Paths.get(filePath)));

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage(text);
            for (int i=0; i < messageCount; i++) {
                long start = System.currentTimeMillis();
                producer.send(message);
                long responseTimeInMillis = System.currentTimeMillis() - start;
                responseTimes[i] = responseTimeInMillis;
            }

            producer.close();
            session.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        long totalResponseTime = 0;
        for (long responseTime : responseTimes) {
            totalResponseTime += responseTime;
        }
        // Calculate average response time
        double averageResponseTime = (double) totalResponseTime / messageCount;
        System.out.println("Average response time: " + averageResponseTime + " ms");

        java.util.Arrays.sort(responseTimes);
        long medianMs = responseTimes[responseTimes.length/2];
        System.out.println("Median response time: " + medianMs + " ms");

    }
}
