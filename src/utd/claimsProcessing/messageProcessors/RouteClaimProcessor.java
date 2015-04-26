package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import utd.claimsProcessing.dao.ProcedureDAO;
import utd.claimsProcessing.domain.Claim;
import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.Procedure;
import utd.claimsProcessing.domain.RejectedClaimInfo;

/**
 * A message processor responsible for retrieving the Procedure identified by the Claim
 * from the ProcedureDAO. The retrieved procedure is attached to the ClaimFolder before
 * passing to the next step in the process. 
 */

public class RouteClaimProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(RetrieveProcedureProcessor.class);
	
	private MessageProducer producer;
	
	public RouteClaimProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{

	}

	public void onMessage(Message message)
	{
		logger.debug("RetrieveProcedureProcessor ReceivedMessage");

		try {
			Object object = ((ObjectMessage) message).getObject();
			ClaimFolder claimFolder = (ClaimFolder)object;
			
			String procedureCode = claimFolder.getClaim().getProcedureCode();
			Procedure procedure = ProcedureDAO.getSingleton().retrieveByCode(procedureCode);
			if(procedure == null) {
				Claim claim = claimFolder.getClaim();
				RejectedClaimInfo rejectedClaimInfo = new RejectedClaimInfo("Proceedure Not Found: " + procedureCode);
				claimFolder.setRejectedClaimInfo(rejectedClaimInfo);
				if(!StringUtils.isBlank(claim.getReplyTo())) {
					rejectedClaimInfo.setEmailAddr(claim.getReplyTo());
				}
				rejectClaim(claimFolder);
			}
			else {
				logger.debug("Found Claim: " + procedure.getProcedureCode() + "in category "+procedure.getProcedureCategory().name());
				
				if(procedure.getProcedureCategory().name().equals("GeneralPractice"))
				{
					Queue queue = getSession().createQueue(QueueNames.processGPClaim);
					producer = getSession().createProducer(queue);
					producer.send(message);
				}
				else if(procedure.getProcedureCategory().name().equals("Dental"))
				{
					Queue queue = getSession().createQueue(QueueNames.processDentalClaim);
					producer = getSession().createProducer(queue);
					producer.send(message);
				}
				else if(procedure.getProcedureCategory().name().equals("Radiology"))
				{
					Queue queue = getSession().createQueue(QueueNames.processRadiologyClaim);
					producer = getSession().createProducer(queue);
					producer.send(message);
				}
				else if(procedure.getProcedureCategory().name().equals("Optometry"))
				{
					Queue queue = getSession().createQueue(QueueNames.processOptometryClaim);
					producer = getSession().createProducer(queue);
					producer.send(message);
				}
				else
				{
					Claim claim = claimFolder.getClaim();
					RejectedClaimInfo rejectedClaimInfo = new RejectedClaimInfo("Unknown Category: " + procedure.getProcedureCategory().name());
					claimFolder.setRejectedClaimInfo(rejectedClaimInfo);
					if(!StringUtils.isBlank(claim.getReplyTo())) {
						rejectedClaimInfo.setEmailAddr(claim.getReplyTo());
					}
					rejectClaim(claimFolder);
				}

				logger.debug("Finished Sending: " + procedure.getProcedureCode());
			}
		}
		catch (Exception ex) {
			logError("RouteClaimProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}
}
