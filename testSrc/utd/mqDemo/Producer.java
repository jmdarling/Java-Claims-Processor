package utd.mqDemo;

import java.util.Date;
import java.util.ResourceBundle;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This application demos a producer using ActiveMQ. The application opens a
 * connection to the queue subject and sends messageCount messages. The messages
 * sent are simple text messages.
 */
public class Producer extends Thread
{
	private Queue queue;
	private int messageCount = 50;
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String subject = "TOOL.DEFAULT";
	
	public static void main(String args[])
	{
		Producer producer = new Producer();
		producer.run();
	}

	public void showParameters()
	{
		System.out.println("Connecting to URL: " + url);
		System.out.println("Listening to subject: " + subject);
	}

	private void readProperties(String fileName)
	{
		ResourceBundle rb = ResourceBundle.getBundle(fileName);
		url = rb.getString("url");
		subject = rb.getString("subject");
	}

	public void run()
	{
		readProperties("defaultProducer");
		showParameters();

		Connection connection = null;
		try {
			// Create the connection.
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = session.createQueue(subject);

			// Create the producer.
			MessageProducer producer = session.createProducer(queue);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Start sending messages
			sendLoop(session, producer);

			System.out.println("[" + this.getName() + "] Done.");
		}
		catch (Exception e) {
			System.out.println("[" + this.getName() + "] Caught: " + e);
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			}
			catch (Throwable ignore) {
			}
		}
	}

	protected void sendLoop(Session session, MessageProducer producer) throws Exception
	{

		for (int i = 0; i < messageCount || messageCount == 0; i++) {

			TextMessage message = session.createTextMessage(createMessageText(i));

			String msg = message.getText();
			System.out.println("[" + this.getName() + "] Sending message: '" + msg + "'");

			producer.send(message);
		}
	}

	private String createMessageText(int index)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Message: " + index + " sent at: " + new Date());
		return buffer.toString();
	}

}
