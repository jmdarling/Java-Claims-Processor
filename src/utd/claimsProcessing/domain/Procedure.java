package utd.claimsProcessing.domain;

/**
 * Procedure is an operation that was performed by a Provider for a Member. 
 * Each Procedure is marked by a unique procedure code that is used by 
 * the Provider when filing a Claim. Procedures also maintain the amount
 * to be paid by the insurance company to the Provider.
 */
public class Procedure implements EntityObject
{
    private static final long serialVersionUID = 6776437778154382991L;
    
	private String ID;
	private ProcedureCategory procedureCategory;
	private String procedureCode;
	private String description;
	private double paymentAmount;

	public String getID()
	{
		return ID;
	}

	public void setID(String iD)
	{
		ID = iD;
	}

	public ProcedureCategory getProcedureCategory()
	{
		return procedureCategory;
	}

	public void setProcedureCategory(ProcedureCategory procedureCategory)
	{
		this.procedureCategory = procedureCategory;
	}

	public String getProcedureCode()
	{
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode)
	{
		this.procedureCode = procedureCode;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public double getPaymentAmount()
	{
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount)
	{
		this.paymentAmount = paymentAmount;
	}

}
