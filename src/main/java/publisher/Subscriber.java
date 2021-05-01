package publisher;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;

public class Subscriber extends Thread {
	private static final Logger logger = Logger.getLogger(Subscriber.class);
	private static ActiveMQConnectionFactory connectionFactory;
	private static TopicSession session;
	private static MessageProducer producer;
	private static TopicConnection connection;
	
	public Subscriber(String jmsAddress, String topicName) {
		create(jmsAddress,topicName);
	}
	
	private void create(String jmsAddress, String topicName) {
		try {
			connectionFactory = new ActiveMQConnectionFactory(jmsAddress);
			connection = connectionFactory.createTopicConnection();
			connection.start();
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// 1) create topic
			Topic topic = session.createTopic(topicName);
			
			//2)create TopicSubscriber  
			TopicSubscriber receiver = session.createSubscriber(topic);
			
			//3) create listener object  
			MsgListener listener = new MsgListener(this.getName());
			
			//4) register the listener object with subscriber  
			receiver.setMessageListener(listener);
								
		} catch (Exception e) {
			logger.error("Subscribe Exception : ", e);
		}
	}
	public void run(){
		logger.info(this.getName()+ " is ready, waiting for messages...");
		try {
			while (true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
