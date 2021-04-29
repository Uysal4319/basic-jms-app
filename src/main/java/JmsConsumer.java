import examples.ConsumerMessageListener;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer  {
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private MessageConsumer consumer;
	private final int ackMode = Session.AUTO_ACKNOWLEDGE;
	private  int consumerCount;
	private String jmsAddress;
	private String queueName;
	
	
	Connection connection;
	
	public JmsConsumer(String jmsAddress , String queueName) {
		this.jmsAddress = jmsAddress;
		this.queueName= queueName;
		create();
	}
	
	public JmsConsumer(String jmsAddress , String queueName, int consumerCount) {
		this.jmsAddress = jmsAddress;
		this.queueName= queueName;
		this.consumerCount = consumerCount;
		create();
	}
	
	private void create()  {
		
		try {
			connectionFactory = new ActiveMQConnectionFactory(jmsAddress);
			
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, ackMode);
			Queue queue = session.createQueue(queueName);
				
			if(consumerCount==0){
				consumer = session.createConsumer(queue);
				consumer.setMessageListener(new ConsumerMessageListener("Consumer"));
			}else {
				for (int i = 0; i < consumerCount; i++) {
					
					consumer = session.createConsumer(queue);
					consumer.setMessageListener(new ConsumerMessageListener(
							"Consumer " + i));
				}
			}
			
			connection.start();
			
		} catch (JMSException e) {
			e.printStackTrace();
			try {
				connection.stop();
			} catch (JMSException jmsException) {
				jmsException.printStackTrace();
			}
		} finally {
		}
	}
	
	/**
	 * We used MessageListener , don't need Thread.
	 * **/
//	public void run() {
//		try {
//			while (true){
//				TextMessage textMsg = (TextMessage) consumer.receive();
//				System.out.println(textMsg);
//				System.out.println("Received: " + textMsg.getText());
//				Thread.sleep(1);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
