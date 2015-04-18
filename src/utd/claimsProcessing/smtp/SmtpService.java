package utd.claimsProcessing.smtp;

import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 * This class provides a service used to send emails. The service uses the SMTP
 * protocol to connect to an outgoing SMTP service. The connection information
 * is maintained in a properties file 'smtpService.properties' that is expected
 * to be found on the application's classpath.
 */
public class SmtpService
{
	private static final Logger logger = Logger.getLogger(SmtpService.class);
	
	private static final String propFilename = "smtpService";

	private String hostname;
	private int portNumber ;
	private String userID;
	private String passwd;
	private String domain;

	private static SmtpService singleton;
	//public static final String defaultFromField = "donotreply@sandboxe1ef7eb404d84a0384b53376a99686a8.mailgun.org";

	public static SmtpService getSingleton() throws Exception
	{
		if (singleton == null) {
			singleton = new SmtpService();
			try {
				singleton.readProperties(propFilename);
			}
			catch (Exception ex) {
				String msg = "Unable to load SMTP properties from: " + propFilename;
				logger.error(msg);
				throw new Exception(msg, ex);
			}
		}
		return singleton;
	}

	private SmtpService()
	{
	}

	public SmtpService(String userID, String passwd)
	{
		this.userID = userID;
		this.passwd = passwd;
	}

	/**
	 * Send a simple text only email using the configured SMTP gateway server.
	 * This method expects the email's contents including the from, to, subject,
	 * and message body.
	 */
	public void sendMail(String from, String to, String messageSubject, String messageBody) throws EmailException
	{
		Email email = new SimpleEmail();
		email.setDebug(false);

		email.setHostName(hostname);
		email.setSmtpPort(portNumber);
		email.setTLS(true);

		email.setAuthenticator(new DefaultAuthenticator(userID, passwd));

		email.setFrom(from);
		email.addTo(to);
		email.setSubject(messageSubject);
		email.setMsg(messageBody);

		email.send();
	}

	/**
	 * Read the service's configuration parameters from a properties file
	 * expected to be found on the application's classpath.
	 */
	private void readProperties(String fileName)
	{
		ResourceBundle rb = ResourceBundle.getBundle(fileName);

		hostname = rb.getString("hostname");
		logger.info("SmtpService hostname: " + hostname);

		String portNumberStr = rb.getString("portNumber");
		portNumber = Integer.parseInt(portNumberStr);
		logger.info("SmtpService portNumber: " + portNumber);

		userID = rb.getString("userID");
		logger.info("SmtpService userID: " + userID);

		passwd = rb.getString("passwd");
		logger.info("SmtpService passwd: " + passwd);

		domain = rb.getString("domain");
		logger.info("SmtpService domain: " + domain);
	}

	public String getDomain()
    {
	    return domain;
    }

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}

	public void setPortNumber(int portNumber)
	{
		this.portNumber = portNumber;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

}
