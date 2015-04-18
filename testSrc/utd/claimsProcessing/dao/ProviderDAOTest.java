package utd.claimsProcessing.dao;

import java.util.Collection;

import junit.framework.TestCase;

import org.junit.Test;

import utd.claimsProcessing.domain.ProcedureCategory;
import utd.claimsProcessing.domain.Provider;

public class ProviderDAOTest extends TestCase
{
	@Test
	public void testLoad() throws Exception
	{
		ProviderDAO providerDAO = ProviderDAO.getSingleton();
		Provider provider = providerDAO.retrieveProvider("provider1");
		assertNotNull(provider);
		
		assertEquals("Health Care Service", provider.getProviderName());
		assertEquals(3, provider.getCategories().size());
		assertTrue(provider.getCategories().contains(ProcedureCategory.Radiology));
	}

	@Test
	public void testLoadFailure() throws Exception
	{
		ProviderDAO providerDAO = ProviderDAO.getSingleton();
		Provider provider = providerDAO.retrieveProvider("provider99");
		assertNull(provider);
	}

	@Test
	public void testLoadAll() throws Exception
	{
		ProviderDAO providerDAO = ProviderDAO.getSingleton();
		Collection<Provider> providers = providerDAO.retrieveAllProviders();
		assertFalse(providers.isEmpty());
		assertEquals(4, providers.size());
	}
}
