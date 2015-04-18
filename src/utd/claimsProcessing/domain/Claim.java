package utd.claimsProcessing.domain;

import java.io.Serializable;

/**
 * Claim are made by Providers for Payment to be made by the insurance company.
 * Providers are paid for Procedures performed on insured Members. Claims are
 * the initial entity in the claims processing workflow.
 */
public class Claim implements Serializable
{
	private static final long serialVersionUID = 1686699179879321240L;

	private String memberID;
	private String providerID;
	private String procedureCode;
	private String replyTo;
	private String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getReplyTo()
	{
		return replyTo;
	}

	public void setReplyTo(String replyTo)
	{
		this.replyTo = replyTo;
	}

	public String getMemberID()
	{
		return memberID;
	}

	public void setMemberID(String memberID)
	{
		this.memberID = memberID;
	}

	public String getProviderID()
	{
		return providerID;
	}

	public void setProviderID(String providerID)
	{
		this.providerID = providerID;
	}

	public String getProcedureCode()
	{
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode)
	{
		this.procedureCode = procedureCode;
	}
}
