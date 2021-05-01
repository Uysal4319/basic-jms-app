package examples;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.net.URI;
import java.net.URISyntaxException;

public class MultipleConsumerExample {
	private static final Logger logger = Logger.getLogger(MultipleConsumerExample.class);
	
	public static void main(String[] args) throws URISyntaxException, Exception {
		BrokerService broker = BrokerFactory.createBroker(new URI(
				"broker:(tcp://localhost:61616)"));
		broker.start();
		Connection connection = null;
		try {
			// Producer
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					"tcp://localhost:61616");
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("customerQueue");
			
			// Consumer
			for (int i = 0; i < 4; i++) {
				MessageConsumer consumer = session.createConsumer(queue);
				consumer.setMessageListener(new ConsumerMessageListener(
						"Consumer " + i));
			}
			connection.start();
			
			String basePayload = "Important Task";
			MessageProducer producer = session.createProducer(queue);
			for (int i = 0; i < 10; i++) {
				String payload = basePayload + i;
				Message msg = session.createTextMessage(payload);
				logger.info("Sending text '" + payload + "'");
				producer.send(msg);
			}
			
			Thread.sleep(1000);
			session.close();
		} finally {
			if (connection != null) {
				connection.close();
			}
			broker.stop();
		}
	}
	
}
