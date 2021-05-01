package publisher;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Publisher {
	private static final Logger logger = Logger.getLogger(Publisher.class);
	private static ActiveMQConnectionFactory connectionFactory;
	private static TopicSession session;
	private static MessageProducer producer;
	private static TopicConnection connection;
	
	public Publisher(String jmsAddress, String topicName) {
		create(jmsAddress, topicName);
	}
	
	private void create(String jmsAddress, String topicName) {
		try {
			connectionFactory = new ActiveMQConnectionFactory(jmsAddress);
			connection = connectionFactory.createTopicConnection();
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			
			//1)create TopicPublisher object          
			TopicPublisher publisher = session.createPublisher(topic);
			//2) create TextMessage object  
			TextMessage msg = session.createTextMessage();
			
			//3) write message  
			BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				logger.info("Enter Msg, end to terminate:");
				String s = b.readLine();
				if (s.equals("end"))
					break;
				msg.setText(s);
				//7) send message  
				publisher.publish(msg);
				logger.info("Message successfully sent.");
			}
			//4) connection close  
			connection.close();
		} catch (Exception e) {
			logger.info("Publisher Error : ",e);
		}
	}
}
