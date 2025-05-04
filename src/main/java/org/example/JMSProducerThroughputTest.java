package org.example;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;

public class JMSProducerThroughputTest {

    public static void main(String[] args) throws Exception {
        int numMessages = 1000000;         // total messages to send
        int messageSize = 1024;           // 1 KB payload
        int targetThroughput = 10000;     // target messages/sec
        String filePath = "data/message.txt";

        // Calculate interval between sends
        long intervalMs = 1000 / targetThroughput;
        long sleepMs = (long) (intervalMs - 0.2 * intervalMs);

        // Setup JMS (update with your actual JNDI config)
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        Context context = new InitialContext(env);

        QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        QueueConnection connection = factory.createQueueConnection();
        connection.start();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) context.lookup("dynamicQueues/test-queue");
        QueueSender sender = session.createSender(queue);

        String text = new String(Files.readAllBytes(Paths.get(filePath)));
        TextMessage message = session.createTextMessage(text);

        long start = System.currentTimeMillis();
        for (int i = 0; i < numMessages; i++) {
            sender.send(message);
            Thread.sleep(sleepMs);
        }
        long elapsed = System.currentTimeMillis() - start;
        double actualThroughput = (numMessages * 1000.0) / elapsed;

        System.out.println("Sent " + numMessages + " messages in " + elapsed + " ms");
        System.out.printf("Actual throughput: %.2f messages/sec\n", actualThroughput);

        sender.close();
        session.close();
        connection.close();
        context.close();
    }
}
