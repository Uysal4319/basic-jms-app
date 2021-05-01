package examples;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener {
	private String consumerName;
	private static final Logger logger = Logger.getLogger(JmsBrowseQueue.class);
	
	public ConsumerMessageListener(String consumerName) {
		this.consumerName = consumerName;
	}
	
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			logger.info(consumerName + " received " + textMessage.getText());
		} catch (JMSException e) {
			logger.error("consume error" , e);
		}
	}
	
}
