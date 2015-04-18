package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;

/**
 * A message process responsible for persisting (but not implemented) the contents 
 * given ClaimFolder. 
 */
public class SaveFolderProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(SaveFolderProcessor.class);

	public SaveFolderProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{
	}

	public void onMessage(Message message)
	{
		logger.debug("SaveFolderProcessor ReceivedMessage");

		try {
			Object object = ((ObjectMessage) message).getObject();
			ClaimFolder claimFolder = (ClaimFolder) object;
			
			logger.info("Saving Claim Folder " + claimFolder.getClaimID());
		}
		catch (Exception ex) {
			logError("SaveFolderProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}
}
