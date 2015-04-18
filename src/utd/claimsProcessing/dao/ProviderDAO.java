package utd.claimsProcessing.dao;

import java.util.Collection;
import java.util.Map;

import utd.claimsProcessing.domain.Provider;

/**
 * Manages and returns instances of Provider.
 */
public class ProviderDAO extends BaseDAO
{
	private Map<String, Provider> providers;
	private static ProviderDAO singleton;
	
	public static ProviderDAO getSingleton() throws Exception
	{
		if(singleton == null) {
			singleton = new ProviderDAO();
		}
		return singleton;
	}
	
	private ProviderDAO() throws Exception
	{
		providers = loadFromStore("provider");
	}
	
	/**
	 * Returns the instance with the given ID or null if none is found.
	 */
	public Provider retrieveProvider(String providerID) throws Exception
	{
		return providers.get(providerID);
	}

	/**
	 * Returns instances managed by this DAO.
	 */
	public Collection<Provider> retrieveAllProviders() throws Exception
	{
		return providers.values();
	}

}
