import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

import javax.jms.*;

public class JmsStarter {
	private static final String jmsAddress = "tcp://localhost:62000";
	private static final String brokerName = "customerBroker";
	private static final String queueName = "customerQueue";
	
	public static void main(String[] args) {
		JmsBroker broker = null;
		try {
			broker = new JmsBroker(jmsAddress, brokerName, true, 62001);	
			
			JmsProducer jmsProducer = new JmsProducer(jmsAddress,queueName);
			
			new JmsConsumer(jmsAddress,queueName);
			
			
//			new JmsMessageBrowser(jmsAddress,queueName).start();
			
			//Multiple Consumer
//			JmsConsumer jmsConsumers = new JmsConsumer(jmsAddress,queueName,4);
//			jmsConsumer.start();
					
			
			for (int i =0 ; i<10;i++){
				Thread.sleep(1);
				String payload = "Message - " +i ;
				Message msg = jmsProducer.setText(payload);
				System.out.println("Sending text '" + payload + "'");
				jmsProducer.getProducer().send(msg);
				
			}
			
			
		} catch (MessagingException | JMSException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			broker.stop();
		}
		
	}
}
