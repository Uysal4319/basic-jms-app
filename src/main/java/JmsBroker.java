import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

public class JmsBroker extends Thread {
	private BrokerService broker;
	private String connectorName;
	private Object lock;
	private static final Logger logger = Logger.getLogger(JmsBroker.class);
	
	public JmsBroker(String jmsAddress, String brokerName, boolean useJmx, int jmxPort) throws MessagingException {
		broker = new BrokerService();
		broker.setBrokerName(brokerName);
		broker.setUseJmx(useJmx);
		if (useJmx && jmxPort != -1) {
			broker.getManagementContext().setConnectorPort(jmxPort);
		}
		broker.setSchedulerSupport(false);
		broker.setUseLoggingForShutdownErrors(true);
		broker.setPersistent(false);
		connectorName = jmsAddress;
		
		try {
			broker.addConnector(jmsAddress);
			broker.start(true);
			
			while (!broker.isStarted()) {
				Thread.sleep(10);
				System.out.println("BROKER NOT STARTED");
			}
			
			if (logger.isInfoEnabled()) {
				logger.info("JMS BROKER STARTED");
			}
			System.out.println("JMS BROKER STARTED");
		} catch (InterruptedException e) {
			throw new MessagingException(e.getMessage());
		} catch (Exception e) {
			throw new MessagingException(e.getMessage());
		}
	}
	
	public void run() {
		try {
			lock = new Object();
			synchronized (lock) {
				lock.wait();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
}
