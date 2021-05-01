package publisher;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import pointtopoint.JmsBroker;

import java.util.ArrayList;
import java.util.List;

public class Publish2SubscibeStarter {
	private static final String jmsAddress = "tcp://localhost:61616";
	private static final String brokerName = "customerBroker";
	private static final String topicName = "myTopic";
	private static List<Subscriber> subscribers = new ArrayList<>();
	
	public static void main(String[] args) {
		try {
			new JmsBroker(jmsAddress, brokerName);
			
			for (int i = 0 ; i<5 ; i++){
				Subscriber subscriber = new Subscriber(jmsAddress,topicName);
				subscriber.setName("Subscriber "+ i);
				subscribers.add(subscriber);
			}
			
			subscribers.forEach(Thread::start);
			
			new Publisher(jmsAddress,topicName);
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
