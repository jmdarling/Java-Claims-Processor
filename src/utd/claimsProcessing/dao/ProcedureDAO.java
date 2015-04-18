package utd.claimsProcessing.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import utd.claimsProcessing.domain.Procedure;
import utd.claimsProcessing.domain.ProcedureCategory;

/**
 * Manages and returns instances of Procedure.
 */
public class ProcedureDAO extends BaseDAO
{
	private Map<String, Procedure> procedures;
	private static ProcedureDAO singleton;
	
	public static ProcedureDAO getSingleton() throws Exception
	{
		if(singleton == null) {
			singleton = new ProcedureDAO();
		}
		return singleton;
	}
	
	private ProcedureDAO() throws Exception
	{
		procedures = loadFromStore("procedure");
	}
	
	/**
	 * Returns the instance with the given ID or null if none is found.
	 */
	public Procedure retrieveProcedure(String procedureID) throws Exception
	{
		return procedures.get(procedureID);
	}

	/**
	 * Returns the instance with the given procedureCode or null if none is found.
	 */
	public Procedure retrieveByCode(String procedureCode)
	{
		if(procedureCode == null) {
			throw new IllegalArgumentException("argument procedureCode is null");
		}
		
		for(Procedure procedure: procedures.values()) {
			if(procedureCode.equals(procedure.getProcedureCode())) {
				return procedure;
			}
		}
		return null;
	}
	
	/**
	 * Returns ALL instances with the given procedureCategory or an empty List if none is found.
	 */
	public List<Procedure> retrieveByCategory(ProcedureCategory procedureCategory) throws Exception
	{
		if(procedureCategory == null) {
			throw new IllegalArgumentException("argument procedureCategory is null");
		}
		
		List<Procedure> result = new ArrayList<Procedure>();
		for(Procedure procedure: procedures.values()) {
			if(procedureCategory.equals(procedure.getProcedureCategory())) {
				result.add(procedure);
			}
		}
		return result;
	}

	/**
	 * Returns instances managed by this DAO.
	 */
	public Collection<Procedure> retrieveAllProcedures() throws Exception
	{
		return procedures.values();
	}

}
