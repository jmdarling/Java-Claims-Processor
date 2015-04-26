package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.log4j.Logger;
import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.PolicyState;
import utd.claimsProcessing.domain.ProcedureCategory;


/**
 * A message processor responsible for retrieving the Procedure identified by the Claim
 * from the ProcedureDAO. The retrieved procedure is attached to the ClaimFolder before
 * passing to the next step in the process. 
 */

public class RadiologyClaimProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(RetrieveProcedureProcessor.class);
	
	private MessageProducer payOrDenyProducer;
	
	public RadiologyClaimProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{

	}

	public void onMessage(Message message)
	{
		  logger.debug("RadiologyClaimProcessor ReceivedMessage");

		  try 
		  {
		    Object object = ((ObjectMessage) message).getObject();
		    ClaimFolder claimFolder = (ClaimFolder) object;
		    Message claimMessage = getSession().createObjectMessage(claimFolder);

		    if(validPolicy(claimFolder) && validProcedure(claimFolder, ProcedureCategory.Radiology)) 
		    {  
		        
		          payOrDenyProducer = getSession().createProducer(getSession().createQueue(QueueNames.payClaim));
		          payOrDenyProducer.send(claimMessage);
		        
		    }
		    else
		    {
		    	  payOrDenyProducer = getSession().createProducer(getSession().createQueue(QueueNames.denyClaim));
			      payOrDenyProducer.send(claimMessage);
		    }
		  }
		  
		  catch (Exception ex) 
		  {
			  	 logError("RadiologyClaimProcessor.onMessage() " + ex.getMessage(), ex);
		  }

	}

	private boolean validProcedure(ClaimFolder claimFolder,ProcedureCategory radiology) 
	{
		logger.debug(String.format("Validating Radiology Claim ID {0}", claimFolder.getClaimID()));
		
		return claimFolder.getPolicy().getCategories().contains(radiology);
		
	}

	private boolean validPolicy(ClaimFolder claimFolder) 
	{
		logger.debug(String.format("Validating policy for Radiology Claim ID {0}", claimFolder.getClaimID()));

		return claimFolder.getPolicy().getPolicyState() == PolicyState.active;
	}
}



