package utd.mqDemo;

import java.util.ResourceBundle;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This application demos a comsumer using ActiveMQ. The application opens a
 * connection to the queue subject and retrieves messages. The messages are
 * assumed to be simple text messages.
 */
public class Consumer implements MessageListener, ExceptionListener
{
	private Session session;
	private Queue queue;
	private String subject = "TOOL.DEFAULT";
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args)
	{
		Consumer consumer = new Consumer();
		consumer.run();
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
		try {
			readProperties("defaultConsumer");
			showParameters();

			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			Connection connection = connectionFactory.createConnection();
			connection.setExceptionListener(this);
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			queue = session.createQueue(subject);

			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(this);
		}
		catch (Exception e) {
			System.out.println("Error Caught: " + e);
			e.printStackTrace();
		}
	}

	public void onMessage(Message message)
	{
		try {
			if (message instanceof TextMessage) {
				String txtMsg = ((TextMessage)message).getText();
				System.out.println("Received: '" + txtMsg);
			}
			else {
				System.err.println("Unknown type of JMS message received");
			}
		}
		catch(JMSException ex) {
			System.err.println("JMS Ex caught " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public synchronized void onException(JMSException ex)
	{
		System.out.println("JMS Exception occured.  Shutting down client.");
	}

}
