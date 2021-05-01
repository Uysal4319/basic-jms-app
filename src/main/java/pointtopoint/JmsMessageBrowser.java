package pointtopoint;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.Enumeration;

public class JmsMessageBrowser extends Thread {
	private static final Logger logger = Logger.getLogger(JmsMessageBrowser.class);
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private QueueBrowser browser;
	private Queue queue;

	
	Connection connection;
	
	public JmsMessageBrowser(String jmsAddress, String queueName) {
		create(jmsAddress, queueName);
	}
	
	public JmsMessageBrowser(Session session,Queue queue) {
	this.session = session;	
	this.queue = queue;
	createBrowser();
	}
	

	
	private void create(String jmsAddress, String queueName) {
		
		try {
			connectionFactory = new ActiveMQConnectionFactory(jmsAddress);
			
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);
			browser = session.createBrowser(queue);
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	private void createBrowser() {
		
		try {
			browser = session.createBrowser(queue);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			logger.info("Browser Started.");
			while (true) {
				Enumeration e = browser.getEnumeration();
				while (e.hasMoreElements()) {
					TextMessage message = (TextMessage) e.nextElement();
					logger.info("Get [" + message.getText() + "]");
				}
				Thread.sleep(1);
			}
		} catch (Exception e) {
			logger.error("Thread exception : ", e);
		}
	}
	
}
