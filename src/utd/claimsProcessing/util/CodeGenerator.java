package utd.claimsProcessing.util;

/**
 * This class is used to generate unique ID string for various purposes. 
 */
public class CodeGenerator
{
	private static CodeGenerator singleton;
	
	synchronized public static CodeGenerator getSingleton()
	{
		if(singleton == null) {
			singleton = new CodeGenerator();
		}
		return singleton;
	}
	
	private CodeGenerator()
	{
		
	}
	
	/**
	 * Generate a unique ID for a new Payment. 
	 */
	public String generatePaymentCode()
	{
		long timestamp = System.currentTimeMillis();
		return "Payment" + timestamp;
	}

	/**
	 * Generate a unique ID for a new Claim.
	 */
	public String generateClaimNumber()
	{
		long timestamp = System.currentTimeMillis();
		return "Claim" + timestamp;
	}
}
