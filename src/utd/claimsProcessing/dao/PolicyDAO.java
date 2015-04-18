package utd.claimsProcessing.dao;

import java.util.Collection;
import java.util.Map;

import utd.claimsProcessing.domain.Policy;

/**
 * Manages and returns instances of Policy.
 */
public class PolicyDAO extends BaseDAO
{
	private Map<String, Policy> policies;
	private static PolicyDAO singleton;
	
	public static PolicyDAO getSingleton() throws Exception
	{
		if(singleton == null) {
			singleton = new PolicyDAO();
		}
		return singleton;
	}
	
	private PolicyDAO() throws Exception
	{
		policies = loadFromStore("policy");
	}
	
	/**
	 * Returns the instance with the given ID or null if none is found.
	 */
	public Policy retrievePolicy(String policyID) throws Exception
	{
		return policies.get(policyID);
	}

	/**
	 * Returns instances managed by this DAO.
	 */
	public Collection<Policy> retrieveAllPolicies() throws Exception
	{
		return policies.values();
	}

}
