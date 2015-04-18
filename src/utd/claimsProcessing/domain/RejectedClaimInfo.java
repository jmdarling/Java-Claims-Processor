package utd.claimsProcessing.domain;

import java.io.Serializable;

/**
 * This entity maintains the information that describes the reasons for 
 * a Claim's rejection or denial. 
 */
public class RejectedClaimInfo implements Serializable
{
    private static final long serialVersionUID = -1817718832585579377L;
    
	private String reason;
	private String description;
	private String emailAddr;
	
	public RejectedClaimInfo(String reason)
	{
		this.reason = reason;
	}
	
	public String getEmailAddr()
    {
	    return emailAddr;
    }

	public void setEmailAddr(String emailAddr)
    {
	    this.emailAddr = emailAddr;
    }

	public String getReason()
    {
    	return reason;
    }

	public void setReason(String reason)
    {
    	this.reason = reason;
    }

	public String getDescription()
    {
    	return description;
    }

	public void setDescription(String description)
    {
    	this.description = description;
    }

}
