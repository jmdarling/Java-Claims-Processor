package utd.claimsProcessing.smtp;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * GMAIL account: utdallas4352@gmail.com/utdallas
 * Gateway address: smtp.gmail.com:587
 */
public class TestSmtpService extends TestCase
{
	@Test
	public void testSmtpService() throws Exception
	{
		System.out.println("Starting");
		
		// Will load properties from resource bundle
		SmtpService service = SmtpService.getSingleton();
		//service.setUserID("utdallas4352@gmail.com");
		//service.setPasswd("utdallas");
		
		String from = "utdallas4352@gmail.com";
		String to = "mikechristiansen2000@gmail.com";
		String subject = "Test Subject " + new Date();
		String message = "Test Message " + new Date();
		
		service.sendMail(from, to, subject, message);	
		
		System.out.println("Finished");
	}
}
