package utd.claimsProcessing.dao;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;

import utd.claimsProcessing.domain.Policy;
import utd.claimsProcessing.domain.PolicyState;
import utd.claimsProcessing.domain.ProcedureCategory;

public class PolicyDAOTest extends TestCase
{
	@Test
	public void testLoad() throws Exception
	{
		PolicyDAO policyDAO = PolicyDAO.getSingleton();
		Policy policy = policyDAO.retrievePolicy("policy1");
		assertNotNull(policy);
		
		assertEquals(PolicyState.active, policy.getPolicyState());
		assertEquals(4, policy.getCategories().size());
		assertTrue(policy.getCategories().contains(ProcedureCategory.Dental));
	}

	@Test
	public void testLoadFailure() throws Exception
	{
		PolicyDAO policyDAO = PolicyDAO.getSingleton();
		Policy policy = policyDAO.retrievePolicy("policy99");
		assertNull(policy);
	}

	@Test
	public void testLoadAll() throws Exception
	{
		PolicyDAO policyDAO = PolicyDAO.getSingleton();
		Collection<Policy> policies = policyDAO.retrieveAllPolicies();
		assertFalse(policies.isEmpty());
		assertEquals(6, policies.size());
	}
}
