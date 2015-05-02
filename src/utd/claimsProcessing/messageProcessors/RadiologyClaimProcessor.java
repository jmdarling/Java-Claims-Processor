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

public class RadiologyClaimProcessor  extends AbstractProcedureProcessor  implements MessageListener
{
	private final static Logger logger = Logger.getLogger(RadiologyClaimProcessor.class);
	
	//private MessageProducer payOrDenyProducer;
	
	public RadiologyClaimProcessor(Session session)
	{
		super(session);
	}

	@Override
	public void onMessage(Message message)
	{
		  logger.debug("RadiologyClaimProcessor ReceivedMessage");

		  try 
		  {
		    Object object = ((ObjectMessage) message).getObject();
		    ClaimFolder claimFolder = (ClaimFolder) object;
		   
		    if(validatePolicy(claimFolder) && validateProcedure(claimFolder, ProcedureCategory.Radiology)) 
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
			  	 logError("RadiologyClaimProcessor.onMessage() " + ex.getMessage(), ex);
		  }

	}

	/*private boolean validProcedure(ClaimFolder claimFolder,ProcedureCategory radiology) 
	{
		logger.debug(String.format("Validating Radiology Claim ID {0}", claimFolder.getClaimID()));
		
		return claimFolder.getPolicy().getCategories().contains(radiology);
		
	}

	private boolean validPolicy(ClaimFolder claimFolder) 
	{
		logger.debug(String.format("Validating policy for Radiology Claim ID {0}", claimFolder.getClaimID()));

		return claimFolder.getPolicy().getPolicyState() == PolicyState.active;
	}*/
}



