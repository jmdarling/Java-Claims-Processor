package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.RejectedClaimInfo;
import utd.claimsProcessing.smtp.SmtpService;

/**
 * A processor responsible for handling a claim which has been denied by the
 * workflow. The Provider originating the claim is notified of the denial
 * via email using the address provided by the Claim.
 */
public class DenyClaimsProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(DenyClaimsProcessor.class);

	private MessageProducer producer;

	public DenyClaimsProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{
		Queue queue = getSession().createQueue(QueueNames.saveFolder);
		producer = getSession().createProducer(queue);
	}

	public void onMessage(Message message)
	{
		logger.debug("DenyClaimsProcessor ReceivedMessage");

		try {
			Object object = ((ObjectMessage) message).getObject();
			ClaimFolder claimFolder = (ClaimFolder) object;

			sendDeniedNotification(claimFolder);
			
			Message claimMessage = getSession().createObjectMessage(claimFolder);
			producer.send(claimMessage);
		}
		catch (Exception ex) {
			logError("DenyClaimsProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}
	
	private void sendDeniedNotification(ClaimFolder claimFolder) throws Exception
	{
		String emailAddr = claimFolder.getClaim().getReplyTo();
		String claimID = claimFolder.getClaimID();
		if(StringUtils.isBlank(emailAddr)) {
			logger.info("Unable to send notification email. Folder ID: " + claimID);
		}
		else {
			String to = emailAddr;
			String subject = "Your claim has been denied. Claim: " + claimID;
			String body = buildBody(claimFolder);
			
			SmtpService smtpService = SmtpService.getSingleton();
			String domain = smtpService.getDomain();
			String from = "donotreply@" + domain;

			try {
				smtpService.sendMail(from, to, subject, body);
			}
			catch(EmailException ex) {
				logger.error("Unable to send email " + ex.getMessage(), ex);
			}
		}
	}

	private String buildBody(ClaimFolder claimFolder)
    {
		RejectedClaimInfo rejectionInfo = claimFolder.getRejectedClaimInfo();
		
		StringBuilder sb = new StringBuilder();
		sb.append("Your claim number " + claimFolder.getClaimID());
		sb.append(" has been denied.");
		sb.append("\nThe reason for the denial is " + rejectionInfo.getReason() );
		sb.append(" : " + rejectionInfo.getDescription());
	    return sb.toString();
    }
}
