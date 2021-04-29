import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProducer {
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private MessageProducer producer;
	private final int ackMode = Session.AUTO_ACKNOWLEDGE;
	Connection connection;
	
	public JmsProducer(String jmsAddress, String queueName) {
		
		create(jmsAddress, queueName);
	}
	
	private void create(String jmsAddress, String queueName) {
		
		try {
			connectionFactory = new ActiveMQConnectionFactory(jmsAddress);
			
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, ackMode);
			Queue queue = session.createQueue(queueName);
			
			producer = session.createProducer(queue);
			
//			new JmsMessageBrowser(session,queue).start();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public MessageProducer getProducer() {
		return producer;
	}
	
	public Message setText(String msg) throws JMSException {
		try {
			
			return session.createTextMessage(msg);
			
		} catch (JMSException jmsException) {
			jmsException.printStackTrace();
		}
		return null;
	}
}
