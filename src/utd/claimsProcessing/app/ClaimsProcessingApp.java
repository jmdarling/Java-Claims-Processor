package utd.claimsProcessing.app;

import java.util.ResourceBundle;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import utd.claimsProcessing.messageProcessors.BuildClaimsFolderProcessor;
import utd.claimsProcessing.messageProcessors.DenyClaimsProcessor;
import utd.claimsProcessing.messageProcessors.MessageProcessor;
import utd.claimsProcessing.messageProcessors.PaymentProcessor;
import utd.claimsProcessing.messageProcessors.QueueNames;
import utd.claimsProcessing.messageProcessors.RejectedClaimsProcessor;
import utd.claimsProcessing.messageProcessors.RetrieveMemberProcessor;
import utd.claimsProcessing.messageProcessors.SaveFolderProcessor;

/**
 * The main for the claims processing application. 
 */
public class ClaimsProcessingApp implements ExceptionListener
{
	private static final Logger logger = Logger.getLogger(ClaimsProcessingApp.class);

	private ActiveMQConnectionFactory connectionFactory;
	private Connection connection;
	private Session session;

	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args)
	{
		ClaimsProcessingApp cpApp = new ClaimsProcessingApp();
		try {
			cpApp.readProperties("cpApp");
			cpApp.initialize();
			cpApp.buildProcessors();
		}
		catch (JMSException ex) {
			logger.error("JMS Exception: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Startup initialization. Establish the connection (session) with
	 * ActiveMQ message bus. 
	 */
	public void initialize() throws JMSException
	{
		connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		connection = connectionFactory.createConnection();
		connection.start();
		connection.setExceptionListener(this);

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	/**
	 * This method registers each of the application's message listeners. 
	 */
	public void buildProcessors() throws JMSException
	{
		installProcessor(new RejectedClaimsProcessor(session), QueueNames.rejectClaims);

		installProcessor(new BuildClaimsFolderProcessor(session), QueueNames.incomingClaims);
		installProcessor(new RetrieveMemberProcessor(session), QueueNames.retrieveMember);

		installProcessor(new PaymentProcessor(session), QueueNames.payClaim);
		installProcessor(new DenyClaimsProcessor(session), QueueNames.denyClaim);
		installProcessor(new SaveFolderProcessor(session), QueueNames.saveFolder);
	}

	/**
	 * Installs the given message listener with the given queue. The message listeners 
	 * will consume messages from their queue. 
	 */
	private void installProcessor(MessageProcessor processor, String consumerQueueName) throws JMSException
	{
		processor.initialize();

		Queue queue = session.createQueue(consumerQueueName);
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(processor);

		logger.debug(processor.getClass() + " initialized");
	}

	/**
	 * Read the properties from the given filename, ResourceBundle will
	 * automatically add the file extension ".properties" to the given
	 * filename. NOTE: The specified file must be on the application's 
	 * classpath. 
	 */
	private void readProperties(String fileName)
	{
		ResourceBundle rb = ResourceBundle.getBundle(fileName);
		url = rb.getString("url");
		logger.info("Connecting to URL: " + url);
	}

	/**
	 * A handler that is executed when an uncaught JMS exception
	 * occurs. A feature of ActiveMQ. 
	 */
	public void onException(JMSException ex)
	{
		String msg = "[" + this.getClass().getName() + "] JMS Exception occured.";
		System.err.println(msg);
		ex.printStackTrace();

		logger.error(msg, ex);
	}

	/**
	 * The passwd used to register with ActiveMQ
	 */
	public void setPassword(String pwd)
	{
		this.password = pwd;
	}

	/**
	 * The URL used to register with ActiveMQ
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	/**
	 * The user ID used to register with ActiveMQ
	 */
	public void setUser(String user)
	{
		this.user = user;
	}
}
