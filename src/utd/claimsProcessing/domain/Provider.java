package utd.claimsProcessing.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Providers are the entities that deliver services (Procedures) to Members. 
 * Providers file Claims with the claims processing service. The provider
 * maintains a unique ID that is used in the Claim. 
 */
public class Provider implements EntityObject
{
    private static final long serialVersionUID = 1984061472764623665L;
    
	private String ID;
	private String providerName;
	@XStreamImplicit(itemFieldName="category")
	private List<ProcedureCategory> categories;

	public List<ProcedureCategory> getCategories()
    {
	    return categories;
    }

	public void setCategories(List<ProcedureCategory> categories)
    {
	    this.categories = categories;
    }

	public String getID()
	{
		return ID;
	}

	public void setID(String iD)
	{
		ID = iD;
	}

	public String getProviderName()
    {
	    return providerName;
    }

	public void setProviderName(String providerName)
    {
	    this.providerName = providerName;
    }

}
