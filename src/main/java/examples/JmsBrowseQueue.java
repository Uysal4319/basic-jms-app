package examples;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

public class JmsBrowseQueue {
	private static final Logger logger = Logger.getLogger(JmsBrowseQueue.class);
	
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
			Queue queue = session.createQueue("browseQueue");
			
			String basePayload = "A";
			MessageProducer producer = session.createProducer(queue);
			for (int i = 0; i < 4; i++) {
				String payload = basePayload + i;
				Message msg = session.createTextMessage(payload);
				logger.info("Sending text '" + payload + "'");
				producer.send(msg);
			}
			
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			
			logger.info("Browse through the elements in queue");
			QueueBrowser browser = session.createBrowser(queue);
			Enumeration e = browser.getEnumeration();
			while (e.hasMoreElements()) {
				TextMessage message = (TextMessage) e.nextElement();
				logger.info("Get [" + message.getText() + "]");
			}
			logger.info("Done");
			browser.close();
			
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
