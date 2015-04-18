package utd.claimsProcessing.domain;

import java.util.Date;

/**
 * A Member is an insured individual which has an assigned policy that determines
 * what procedures they may be reinbursed for. 
 */
public class Member implements EntityObject
{
    private static final long serialVersionUID = -182438858711106140L;
    
	private String id;
	private String policyID;
	private String firstName;
	private String lastName;
	private Date dob;

	public String getID()
	{
		return id;
	}
	
	public String getPolicyID()
	{
		return policyID;
	}

	public void setPolicyID(String policyID)
	{
		this.policyID = policyID;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Date getDob()
	{
		return dob;
	}

	public void setDob(Date dob)
	{
		this.dob = dob;
	}

	public void setId(String id)
    {
    	this.id = id;
    }
}
