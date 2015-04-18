package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.Claim;
import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.RejectedClaimInfo;
import utd.claimsProcessing.smtp.SmtpService;
import utd.claimsProcessing.util.ClaimReader;
import utd.claimsProcessing.util.CodeGenerator;

import com.thoughtworks.xstream.XStreamException;

/**
 * This processor is the first in the overall claims processing workflow. Unlike the other
 * processors in the workflow, this processor accepts an XML document that describes
 * the new Claim to be processed. This processor 1) builds a new instance of CLaim from
 * this incoming document and 2) builds a new ClaimFolder which is passed to the next 
 * processor in the workflow. 
 */
public class BuildClaimsFolderProcessor extends MessageProcessor implements MessageListener
{
	private static final Logger logger = Logger.getLogger(BuildClaimsFolderProcessor.class);
	
	private MessageProducer producer;
	
	public BuildClaimsFolderProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{
		Queue queue = getSession().createQueue(QueueNames.retrieveMember);
		producer = getSession().createProducer(queue);
	}

	public void onMessage(Message message)
	{
		logger.debug("BuildClaimsFolderProcessor ReceivedMessage");

		String claimXML = null;
		try {
			claimXML = ((TextMessage) message).getText();

			ClaimReader claimReader = ClaimReader.getSingleton();
			Claim claim = claimReader.loadClaimFromString(claimXML);
			
			ClaimFolder claimFolder = new ClaimFolder();
			claimFolder.setClaimID(CodeGenerator.getSingleton().generateClaimNumber());
			claimFolder.setClaim(claim);
			
			sendReceivedClaimNotification(claimFolder);
			
			Message claimMessage = getSession().createObjectMessage(claimFolder);
			producer.send(claimMessage);
		}
		catch (XStreamException ex) {
			RejectedClaimInfo rejectedClaimInfo = new RejectedClaimInfo("Unable to parse Claim from input XML");
			rejectedClaimInfo.setDescription(ex.getMessage() + "\ninput XML: " + claimXML);
			ClaimFolder claimFolder = new ClaimFolder();
			claimFolder.setRejectedClaimInfo(rejectedClaimInfo);
			rejectClaim(claimFolder);
		}
		catch (Exception ex) {
			logError("Error BuildClaimsFolderProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}

	private void sendReceivedClaimNotification(ClaimFolder claimFolder) throws Exception
	{
		String emailAddr = claimFolder.getClaim().getReplyTo();
		String folderID = claimFolder.getClaimID();
		if(StringUtils.isBlank(emailAddr)) {
			logger.info("Unable to send notification email. Folder ID: " + folderID);
		}
		else {
			SmtpService smtpService = SmtpService.getSingleton();

			String domain = smtpService.getDomain();
			String from = "donotreply@" + domain;
			String to = emailAddr;
			String subject = "Your claim has been received. Claim: " + folderID;
			String body = buildBody(claimFolder);
			
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
		StringBuilder sb = new StringBuilder();
		sb.append("Your claim has been received and assigned the claim number " + claimFolder.getClaimID());
		sb.append("\nYou should receive information concerning its status within the next 2 business days.");
	    return sb.toString();
    }
}
