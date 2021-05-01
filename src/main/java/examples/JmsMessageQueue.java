package examples;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.net.URI;
import java.net.URISyntaxException;

public class JmsMessageQueue {
	private static final String jmsAddress = "tcp://localhost:61616";
	private static final String brokerName = "customerBroker";
	private static final String queueName = "customerQueue";
	private static final Logger logger = Logger.getLogger(JmsMessageQueue.class);
	
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
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue("customerQueue");
			String payload = "Important Task";
			Message msg = session.createTextMessage(payload);
			MessageProducer producer = session.createProducer(queue);
			logger.info("Sending text '" + payload + "'");
			producer.send(msg);
			
			// Consumer
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage textMsg = (TextMessage) consumer.receive();
			logger.info(textMsg);
			logger.info("Received: " + textMsg.getText());
			session.close();
		} finally {
			if (connection != null) {
				connection.close();
			}
			broker.stop();
		}
	}
	
}
