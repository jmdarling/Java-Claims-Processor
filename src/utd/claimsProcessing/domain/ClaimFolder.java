package utd.claimsProcessing.domain;

import java.io.Serializable;

/**
 * Claims folder is a class used to manage hold all of the 
 * entity classes needed to process a claim. As the claim
 * travels through the workflow, entities are retrieved, or
 * created, and added to the folder.  
 */
public class ClaimFolder implements Serializable
{
    private static final long serialVersionUID = -1520400315783991405L;
    
    private String claimID;
	private Claim claim;
	private Member member;
	private Provider provider;
	private Policy policy;
	private Procedure procedure;
	private Payment payment;
	private RejectedClaimInfo rejectedClaimInfo;

	public String getClaimID()
    {
	    return claimID;
    }

	public void setClaimID(String claimID)
    {
	    this.claimID = claimID;
    }

	public RejectedClaimInfo getRejectedClaimInfo()
    {
	    return rejectedClaimInfo;
    }

	public void setRejectedClaimInfo(RejectedClaimInfo rejectedClaimInfo)
    {
	    this.rejectedClaimInfo = rejectedClaimInfo;
    }

	public Payment getPayment()
    {
	    return payment;
    }

	public void setPayment(Payment payment)
    {
	    this.payment = payment;
    }

	public Claim getClaim()
	{
		return claim;
	}

	public void setClaim(Claim claim)
	{
		this.claim = claim;
	}

	public Member getMember()
	{
		return member;
	}

	public void setMember(Member member)
	{
		this.member = member;
	}

	public Provider getProvider()
	{
		return provider;
	}

	public void setProvider(Provider provider)
	{
		this.provider = provider;
	}

	public Policy getPolicy()
	{
		return policy;
	}

	public void setPolicy(Policy policy)
	{
		this.policy = policy;
	}

	public Procedure getProcedure()
	{
		return procedure;
	}

	public void setProcedure(Procedure procedure)
	{
		this.procedure = procedure;
	}
}
