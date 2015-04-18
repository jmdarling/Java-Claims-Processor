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

import utd.claimsProcessing.dao.MemberDAO;
import utd.claimsProcessing.domain.Claim;
import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.Member;
import utd.claimsProcessing.domain.RejectedClaimInfo;

/**
 * A message processor responsible for retrieving the Member identified by the Claim
 * from the MemberDAO. The retrieved member is attached to the ClaimFolder before
 * passing to the next step in the process. 
 */

public class RetrieveMemberProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(RetrieveMemberProcessor.class);
	
	private MessageProducer producer;
	
	public RetrieveMemberProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{
		Queue queue = getSession().createQueue(QueueNames.retrieveProvider);
		producer = getSession().createProducer(queue);
	}

	public void onMessage(Message message)
	{
		logger.debug("RetrieveMemberProcessor ReceivedMessage");

		try {
			Object object = ((ObjectMessage) message).getObject();
			ClaimFolder claimFolder = (ClaimFolder)object;
			
			String memberID = claimFolder.getClaim().getMemberID();
			Member member = MemberDAO.getSingleton().retrieveMember(memberID);
			if(member == null) {
				Claim claim = claimFolder.getClaim();
				RejectedClaimInfo rejectedClaimInfo = new RejectedClaimInfo("Member Not Found: " + memberID);
				claimFolder.setRejectedClaimInfo(rejectedClaimInfo);
				if(!StringUtils.isBlank(claim.getReplyTo())) {
					rejectedClaimInfo.setEmailAddr(claim.getReplyTo());
				}
				rejectClaim(claimFolder);
			}
			else {
				logger.debug("Found Member: " + member.getFirstName() + " " + member.getLastName());

				claimFolder.setMember(member);
			
				Message claimMessage = getSession().createObjectMessage(claimFolder);
				producer.send(claimMessage);
				logger.debug("Finished Sending: " + member.getFirstName() + " " + member.getLastName());
			}
		}
		catch (Exception ex) {
			logError("RetrieveMemberProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}
}
