package publisher;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MsgListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(MsgListener.class);
	private String name;
	
	public MsgListener(String  name) {
		this.name = name;
	}
	
	public void onMessage(Message m) {
		try {
			TextMessage msg = (TextMessage) m;
			
			logger.info("Following message is Received on " + name + ":" + msg.getText());
		} catch (JMSException e) {
			logger.error("Message Follow Exception", e);
		}
	}
}
