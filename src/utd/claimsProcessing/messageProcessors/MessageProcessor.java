package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;

/**
 * This is the abstract base class for all message processors. It provides services 
 * needed by all message handlers (implementors of MessageListener). 
 */
public abstract class MessageProcessor implements MessageListener
{
	final static private Logger logger = Logger.getLogger(MessageProcessor.class);
	
	private Session session;

	MessageProcessor(Session session)
	{
		this.session = session;
	}

	protected Session getSession()
	{
		return session;
	}

	public abstract void initialize() throws JMSException;
	
	protected void rejectClaim(ClaimFolder rejectedClaimFolder)
	{
		try {
			logger.info("Rejecting Claim " + rejectedClaimFolder.getRejectedClaimInfo().getReason());
			logger.info("Rejecting Claim " + rejectedClaimFolder.getRejectedClaimInfo().getDescription());
			
			Queue queue = getSession().createQueue(QueueNames.rejectClaims);
			MessageProducer producer = getSession().createProducer(queue);
			ObjectMessage msg = getSession().createObjectMessage(rejectedClaimFolder);
			producer.send(msg);
			producer.close();
		}
		catch (Exception ex) {
			logError("Unable to reject claim " + ex.getMessage(), ex);
		}
	}
	
	protected void logError(String msg, Exception ex)
    {
		System.err.println(msg);
		ex.printStackTrace();

		logger.error(msg, ex);
    }

	protected void logError(String msg)
    {
		System.err.println(msg);
		logger.error(msg);
    }

}
