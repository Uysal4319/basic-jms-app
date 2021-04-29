import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class JmsMessageBrowser extends Thread {
	
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private final int ackMode = Session.AUTO_ACKNOWLEDGE;
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
			session = connection.createSession(false, ackMode);
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
			System.out.println("Browser Started.");
			while (true) {
				Enumeration e = browser.getEnumeration();
				while (e.hasMoreElements()) {
					TextMessage message = (TextMessage) e.nextElement();
					System.out.println("Get [" + message.getText() + "]");
				}
				Thread.sleep(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
