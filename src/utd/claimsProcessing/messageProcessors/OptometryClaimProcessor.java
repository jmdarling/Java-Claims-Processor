package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.ProcedureCategory;


/**
 * A message processor responsible for retrieving the Procedure identified by the Claim
 * from the ProcedureDAO. The retrieved procedure is attached to the ClaimFolder before
 * passing to the next step in the process. 
 */

public class OptometryClaimProcessor extends AbstractProcedureProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(OptometryClaimProcessor.class);
	
	//private MessageProducer payOrDenyProducer;
	
	public OptometryClaimProcessor(Session session)
	{
		super(session);
	}

	@Override
	public void onMessage(Message message)
	{
		  logger.debug("OptometryClaimProcessor ReceivedMessage");

		  try 
		  {
		    Object object = ((ObjectMessage) message).getObject();
		    ClaimFolder claimFolder = (ClaimFolder) object;
		    //Message claimMessage = getSession().createObjectMessage(claimFolder);

		    if(validatePolicy(claimFolder) && validateProcedure(claimFolder, ProcedureCategory.Optometry)) 
		    {  
		    	
				Message claimMessage = getSession().createObjectMessage(claimFolder);
				denyProducer.send(claimMessage);
	    	    
		    }
		    else
		    {
		    	
				Message claimMessage = getSession().createObjectMessage(claimFolder);
				denyProducer.send(claimMessage);
		    }
		  }
		  
		  catch (Exception ex) 
		  {
			  	logError("OptometryClaimProcessor.onMessage() " + ex.getMessage(), ex);
		  }

	}

	/*private boolean validProcedure(ClaimFolder claimFolder,ProcedureCategory optometry) 
	{
		logger.debug(String.format("Validating Optometry procedure for policy {0}, claim {1}",claimFolder.getPolicy().getID(), claimFolder.getClaimID()));
		
		return claimFolder.getPolicy().getCategories().contains(optometry);
		
	}

	private boolean validPolicy(ClaimFolder claimFolder) 
	{
		logger.debug(String.format("Validating policy for policy {0}", claimFolder.getPolicy().getID()));

		return claimFolder.getPolicy().getPolicyState() == PolicyState.active;
	}*/
}



