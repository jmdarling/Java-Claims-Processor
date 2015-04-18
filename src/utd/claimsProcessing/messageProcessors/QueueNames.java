package utd.claimsProcessing.messageProcessors;

/**
 * This class maintains all of the queue names needed by the 
 * application. 
 */
public class QueueNames
{
	public final static String rejectClaims = "rejectClaims";

	public final static String incomingClaims = "incomingClaims";
	public final static String retrieveMember = "retrieveMember";
	public final static String retrieveProvider = "retrieveProvider";
	public final static String retrievePolicy = "retrievePolicy";
	public final static String retrieveProcedure = "retrieveProcedure";
	public final static String routeClaim = "routeClaim";
	public final static String saveFolder = "saveFolder";
	
	public final static String processGPClaim = "processGPClaim";
	public final static String processDentalClaim = "processDentalClaim";
	public final static String processRadiologyClaim = "processRadiologyClaim";
	public final static String processOptometryClaim = "processOptometryClaim";

	public final static String payClaim = "payClaim";
	public final static String denyClaim = "denyClaim";
}
