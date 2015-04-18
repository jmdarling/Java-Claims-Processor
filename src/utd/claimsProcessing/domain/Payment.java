package utd.claimsProcessing.domain;

import java.io.Serializable;

/**
 * Payment contains the amount to be reinbursed to a Provider for 
 * a Procedure specified in a Claim.
 */
public class Payment implements Serializable
{
    private static final long serialVersionUID = -1482165440962620363L;
    
	private String providerID;
	private String paymentCode;
	private double amount;
	private String comment;

	public String getProviderID()
	{
		return providerID;
	}

	public void setProviderID(String providerID)
	{
		this.providerID = providerID;
	}

	public String getPaymentCode()
	{
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode)
	{
		this.paymentCode = paymentCode;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

}
