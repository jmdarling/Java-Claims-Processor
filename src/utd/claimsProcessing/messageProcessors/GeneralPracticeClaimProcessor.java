package utd.claimsProcessing.messageProcessors;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.ProcedureCategory;

public class GeneralPracticeClaimProcessor extends AbstractProcedureProcessor
{
	
	private final static Logger logger = Logger.getLogger(GeneralPracticeClaimProcessor.class);

	public GeneralPracticeClaimProcessor(Session session) 
	{
		super(session);
	}

	@Override
	public void onMessage(Message message)
	{
	  logger.debug("GeneralClaimProcessor ReceivedMessage");

	  try 
	  {
	    Object object = ((ObjectMessage) message).getObject();
	    ClaimFolder claimFolder = (ClaimFolder) object;

	    if(validatePolicy(claimFolder) && validateProcedure(claimFolder, ProcedureCategory.GeneralPractice)) 
	    {  
	        Message claimMessage = getSession().createObjectMessage(claimFolder);
	        paymentProducer.send(claimMessage);
	    }
	    else
	    {
	    	Message claimMessage = getSession().createObjectMessage(claimFolder);
	    	denyProducer.send(claimMessage);
	    }
	  }
	  catch (Exception ex) 
	  {
	    logError("GeneralClaimProcessor.onMessage() " + ex.getMessage(), ex);
	  }
	}

}
