package org.example;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class JMSConsumerThroughputTest {

    public static void main(String[] args) throws Exception {
        int expectedMessages = 200000;   // Number of messages to receive

        // Setup JMS (adjust for your JMS provider)
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        env.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        Context context = new InitialContext(env);

        QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        QueueConnection connection = factory.createQueueConnection();
        connection.start();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = (Queue) context.lookup("dynamicQueues/test-queue");
        QueueReceiver receiver = session.createReceiver(queue);

        int received = 0;
        long start = System.currentTimeMillis();
        while (received < expectedMessages) {
            Message message = receiver.receive(5000);
            if (message == null) break;
            received++;
        }
        long end = System.currentTimeMillis();
        long elapsed = end - start;
        double throughput = (received * 1000.0) / elapsed;

        System.out.println("Received " + received + " messages in " + elapsed + " ms");
        System.out.printf("Actual consumer throughput: %.2f messages/sec\n", throughput);

        receiver.close();
        session.close();
        connection.close();
        context.close();
    }
}
