package org.example;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;


public class JMSLatencyTestProducer {
    public static void main(String[] args) {
        // Connection details
        String brokerURL = "tcp://localhost:61616";
        String queueName = "performanceTestQueue";
        try {            
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);

            for (int i = 0; i < 10000; i++) {
                long timestamp = System.nanoTime();
                TextMessage msg = session.createTextMessage(String.valueOf(timestamp));
                producer.send(msg);
            }            

            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
