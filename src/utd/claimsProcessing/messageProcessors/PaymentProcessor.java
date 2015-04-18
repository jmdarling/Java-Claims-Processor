package utd.claimsProcessing.messageProcessors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import utd.claimsProcessing.domain.ClaimFolder;
import utd.claimsProcessing.domain.Payment;
import utd.claimsProcessing.domain.Procedure;
import utd.claimsProcessing.domain.Provider;
import utd.claimsProcessing.smtp.SmtpService;
import utd.claimsProcessing.util.CodeGenerator;

/**
 * A processor responsible for handling a claim which has been accepted for payment by the
 * workflow. The Provider originating the claim is notified of the payment
 * via email using the address provided by the Claim.
 */
public class PaymentProcessor extends MessageProcessor implements MessageListener
{
	private final static Logger logger = Logger.getLogger(PaymentProcessor.class);

	private MessageProducer producer;

	public PaymentProcessor(Session session)
	{
		super(session);
	}

	public void initialize() throws JMSException
	{
		Queue queue = getSession().createQueue(QueueNames.saveFolder);
		producer = getSession().createProducer(queue);
	}

	public void onMessage(Message message)
	{
		logger.debug("PaymentProcessor ReceivedMessage");

		try {
			Object object = ((ObjectMessage) message).getObject();
			ClaimFolder claimFolder = (ClaimFolder) object;
			
			Procedure procedure = claimFolder.getProcedure();
			Provider provider = claimFolder.getProvider();
			
			Payment payment = new Payment();
			payment.setPaymentCode(CodeGenerator.getSingleton().generatePaymentCode());
			payment.setAmount(procedure.getPaymentAmount());
			payment.setComment("Claim number:" + claimFolder.getClaimID());
			payment.setProviderID(provider.getID());
			claimFolder.setPayment(payment);
			
			sendPaymentNotification(claimFolder);
			
			Message claimMessage = getSession().createObjectMessage(claimFolder);
			producer.send(claimMessage);
		}
		catch (Exception ex) {
			logError("PaymentProcessor.onMessage() " + ex.getMessage(), ex);
		}
	}
	
	private void sendPaymentNotification(ClaimFolder claimFolder) throws Exception
	{
		String emailAddr = claimFolder.getClaim().getReplyTo();
		String claimID = claimFolder.getClaimID();
		if(StringUtils.isBlank(emailAddr)) {
			logger.info("Unable to send notification email. Folder ID: " + claimID);
		}
		else {
			String to = emailAddr;
			String subject = "Your claim has been paid. Claim: " + claimID;
			String body = buildBody(claimFolder);
			
			SmtpService smtpService = SmtpService.getSingleton();
			String domain = smtpService.getDomain();
			String from = "donotreply@" + domain;

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
		String paymentAmount = Double.toString(claimFolder.getPayment().getAmount());
		
		StringBuilder sb = new StringBuilder();
		sb.append("Your claim number " + claimFolder.getClaimID());
		sb.append(" has been accepted and will be paid for the amount " + paymentAmount);
		sb.append("\nYou should receive your payment in the mail within the next 10 business days.");
	    return sb.toString();
    }
}
