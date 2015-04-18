package utd.claimsProcessing.claimsInjector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import utd.claimsProcessing.util.FileUtils;

/**
 * This is a standalone message producer application. Its purpose is to 
 * read a claim's XML from a file and push a JMS Message containing the claim
 * on the Claim Processing Application's input queue. The path to the XML file
 * can be specified as a command line argument. If no argument is provided, the
 * application will prompt the user for the file name from stdin. 
 * 
 * There are two files:
 * claimsInjector.properties: This is the property file containing the ActiveMQ
 * URL and queue name that the claim will be placed onto. This file is found
 * on the application's classpath. 
 * 
 * <claim file>.xml : This is the name of the file containing the XML document
 * describing the claim to be processed. This file name is either provided as
 * a command line argument, or entered by the user. The file name must be either
 * the full path, or relative to the application's working directory. 
 *
 */
public class ClaimsInjector
{
	// Note this application uses the default ActiveMQ credentials. 
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	private String queueName;

	public static void main(String args[])
	{
		try {
			String filename = null;
			if (args.length == 1) {
				filename = args[0];
			}
			else {
				System.out.print("Enter claim filename: ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				filename = br.readLine();
			}
			
			if(!filename.endsWith(".xml")) {
				filename = filename + ".xml";
			}
			
			ClaimsInjector claimsInjector = new ClaimsInjector();
			claimsInjector.execute(filename);
		}
		catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Execute the application. 
	 */
	private void execute(String claimeFilename) throws Exception
	{
		// Read the app's properties. 
		readProperties("claimsInjector");
		showParameters();

		Connection connection = null;
		try {
			// Read the claim XML document. 
			String claimXML = readClaim(claimeFilename);
			System.out.println("Sending Claim:");
			System.out.println(claimXML);

			// Create the MQ connection.
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the MQ session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);

			// Create the MQ producer.
			MessageProducer producer = session.createProducer(queue);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create the JMS message from the XML and place message on the queue.
			TextMessage message = session.createTextMessage(claimXML);
			producer.send(message);
		}
		finally {
			if(connection != null) {
				connection.close();
			}
		}
		System.out.println("Finished Sending Claim");
	}

	/**
	 * Read the claim XML from the file at the given path. The
	 * path must be either a full path, or relative to the application's
	 * working directory. 
	 */
	private String readClaim(String fileName) throws IOException
	{
		File file = new File(fileName);
		String claimXML = FileUtils.readXML(file);
		return claimXML;
	}

	public void showParameters()
	{
		System.out.println("Connecting to URL: " + url);
		System.out.println("Using Queue: " + queueName);
	}

	/** 
	 * Read a .properties file from the application's classpath. 
	 */
	private void readProperties(String fileName)
	{
		ResourceBundle rb = ResourceBundle.getBundle(fileName);
		url = rb.getString("url");
		queueName = rb.getString("queue");
	}

}
