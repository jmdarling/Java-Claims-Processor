package utd.claimsProcessing.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * A Policy is assiged to a Member. It specifies the Procedures that the Member
 * is entitled coverage for. Each policy lists the procedure categories. The
 * policy also provides a state that indicates whether the Member is qualified
 * for coverage.
 */
public class Policy implements EntityObject
{
	private static final long serialVersionUID = 6531188722638739539L;

	private String ID;
	private PolicyState policyState;
	@XStreamImplicit(itemFieldName = "category")
	private List<ProcedureCategory> categories;

	public PolicyState getPolicyState()
	{
		return policyState;
	}

	public void setPolicyState(PolicyState policyState)
	{
		this.policyState = policyState;
	}

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

}
