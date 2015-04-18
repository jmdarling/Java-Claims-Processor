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

import utd.claimsProcessing.domain.Claim;
import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.RejectedClaimInfo;
import utd.claimsProcessing.smtp.SmtpService;
import utd.claimsProcessing.util.ClaimReader;

/**
 * A processor responsible for handling a claim which has been rejected by the
 * workflow. The Provider originating the claim is notified of the rejection
 * via email using the address provided by the Claim.
 */
public class RejectedClaimsProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(RejectedClaimsProcessor.class);
	
	private boolean sendEmail = true;
	
	private MessageProducer producer;

	public RejectedClaimsProcessor(Session session)
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
		logger.debug("RejectedClaimsProcessor ReceivedMessage");

		try {
			Object object = ((ObjectMessage) message).getObject();
			ClaimFolder claimFolder = (ClaimFolder)object;
			
			Claim rejectedClaim = claimFolder.getClaim();
			RejectedClaimInfo rejectedInfo = claimFolder.getRejectedClaimInfo();
			
			String reason = rejectedInfo.getReason();
			String description = rejectedInfo.getDescription();
			String emailAddr = rejectedInfo.getEmailAddr();

			StringBuilder msg = new StringBuilder();
			msg.append("Claim Rejected because " + reason);
			if(!StringUtils.isBlank(description)) {
				msg.append("\nDescription: " + description);
			}
			logger.error(msg);
			
			if(sendEmail && !StringUtils.isBlank(emailAddr)) {
				String to = emailAddr;
				String subject = reason;
				String body = buildBody(msg, rejectedClaim);
				
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

			Message claimMessage = getSession().createObjectMessage(claimFolder);
			producer.send(claimMessage);
		}
		catch (Exception ex) {
			logError("RetrieveMemberProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}

	private String buildBody(StringBuilder msg, Claim claim) throws Exception
    {
		ClaimReader claimReader = ClaimReader.getSingleton();
		String claimBody = claimReader.buildXmlFromClaim(claim);
		if(claimBody == null) {
			throw new Exception("Unable to build claim xml body");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append("\n");
		sb.append("---------------------------------\n");
		sb.append(claimBody);
	    return sb.toString();
    }

	public boolean isSendEmail()
    {
    	return sendEmail;
    }

	public void setSendEmail(boolean sendEmail)
    {
    	this.sendEmail = sendEmail;
    }

}
