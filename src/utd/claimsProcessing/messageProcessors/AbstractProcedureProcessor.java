package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.Policy;
import utd.claimsProcessing.domain.PolicyState;
import utd.claimsProcessing.domain.ProcedureCategory;
import utd.claimsProcessing.domain.RejectedClaimInfo;

/**
 * An abstract processor which is be parent class of all procedure processing 
 * message processors. 
 */
public abstract class AbstractProcedureProcessor extends MessageProcessor
{
	private final static Logger logger = Logger.getLogger(AbstractProcedureProcessor.class);

	protected MessageProducer paymentProducer;
	protected MessageProducer denyProducer;

	public AbstractProcedureProcessor(Session session)
	{
		super(session);
	}
	
	abstract public void onMessage(Message message);

	public void initialize() throws JMSException
	{
		Queue queue = getSession().createQueue(QueueNames.payClaim);
		paymentProducer = getSession().createProducer(queue);
		
		queue = getSession().createQueue(QueueNames.denyClaim);
		denyProducer = getSession().createProducer(queue);
	}

	protected boolean validateProcedure(ClaimFolder claimFolder, ProcedureCategory category) throws JMSException
    {
		Policy policy = claimFolder.getPolicy();
		if(policy.getCategories().contains(category)) {
			return true;
		}
		else {
			String reason = "The submitted procedure is covered by the member's policy";
			RejectedClaimInfo rejectionInfo = new RejectedClaimInfo(reason);
			rejectionInfo.setEmailAddr(claimFolder.getClaim().getReplyTo());
			rejectionInfo.setDescription(buildProcedureRejectionMsg(claimFolder));
			claimFolder.setRejectedClaimInfo(rejectionInfo);
			
			Message claimMessage = getSession().createObjectMessage(claimFolder);
			denyProducer.send(claimMessage);

			return false;
		}
    }

	private String buildProcedureRejectionMsg(ClaimFolder claimFolder)
    {
		StringBuilder sb = new StringBuilder();
		sb.append("The claim " + claimFolder.getClaimID());
		sb.append("\nhas been rejected because the procdure " + claimFolder.getProcedure().getID());
		sb.append(" : " + claimFolder.getProcedure().getDescription());
		sb.append(" is not covered by the member's policy.");
	    return sb.toString();
    }

	protected boolean validatePolicy(ClaimFolder claimFolder) throws JMSException
    {
		Policy policy = claimFolder.getPolicy();
		if(policy.getPolicyState() == PolicyState.active) {
			return true;
		}
		else {
			String reason = "Member's Policy is " + policy.getPolicyState();
			RejectedClaimInfo rejectionInfo = new RejectedClaimInfo(reason);
			rejectionInfo.setEmailAddr(claimFolder.getClaim().getReplyTo());
			rejectionInfo.setDescription(buildPolicyRejectionMsg(claimFolder));
			claimFolder.setRejectedClaimInfo(rejectionInfo);

			Message claimMessage = getSession().createObjectMessage(claimFolder);
			denyProducer.send(claimMessage);
		    
			return false;
	    }
    }

	private String buildPolicyRejectionMsg(ClaimFolder claimFolder)
    {
		StringBuilder sb = new StringBuilder();
		sb.append("The claim " + claimFolder.getClaimID());
		sb.append("has been rejected because the member's policy is " + claimFolder.getPolicy().getPolicyState());
	    return sb.toString();
    }	
}
