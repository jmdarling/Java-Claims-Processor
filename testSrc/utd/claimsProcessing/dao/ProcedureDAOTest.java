package utd.claimsProcessing.dao;

import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import utd.claimsProcessing.domain.Procedure;
import utd.claimsProcessing.domain.ProcedureCategory;

public class ProcedureDAOTest extends TestCase
{
	@Test
	public void testLoad() throws Exception
	{
		ProcedureDAO procedureDAO = ProcedureDAO.getSingleton();
		Procedure procedure = procedureDAO.retrieveProcedure("procedureGP1");
		assertNotNull(procedure);
		
		assertEquals(ProcedureCategory.GeneralPractice, procedure.getProcedureCategory());
		assertEquals("GP101", procedure.getProcedureCode());
		assertEquals("Standard Blood Test", procedure.getDescription());
		assertEquals(123.00, procedure.getPaymentAmount());
	}

	@Test
	public void testLoadFailure() throws Exception
	{
		ProcedureDAO procedureDAO = ProcedureDAO.getSingleton();
		Procedure procedure = procedureDAO.retrieveProcedure("procedure99");
		assertNull(procedure);
	}

	@Test
	public void testLoadByProcedureCode() throws Exception
	{
		ProcedureDAO procedureDAO = ProcedureDAO.getSingleton();
		
		String procedureCode = "GP101";
		Procedure procedure = procedureDAO.retrieveByCode(procedureCode);
		assertNotNull(procedure);
		
		assertEquals(ProcedureCategory.GeneralPractice, procedure.getProcedureCategory());
		assertEquals("GP101", procedure.getProcedureCode());
		assertEquals("Standard Blood Test", procedure.getDescription());
		assertEquals(123.00, procedure.getPaymentAmount());
	}


	@Test
	public void testLoadByCategory() throws Exception
	{
		ProcedureDAO procedureDAO = ProcedureDAO.getSingleton();
		
		ProcedureCategory category = ProcedureCategory.GeneralPractice;
		List<Procedure> procedures = procedureDAO.retrieveByCategory(category);
		assertNotNull(procedures);
		assertEquals(3, procedures.size());
	}

	@Test
	public void testLoadByCodeFail() throws Exception
	{
		ProcedureDAO procedureDAO = ProcedureDAO.getSingleton();
		
		String procedureCode = "P12";
		Procedure procedure = procedureDAO.retrieveByCode(procedureCode);
		assertNull(procedure);
	}

	@Test
	public void testLoadAll() throws Exception
	{
		ProcedureDAO procedureDAO = ProcedureDAO.getSingleton();
		Collection<Procedure> procedures= procedureDAO.retrieveAllProcedures();
		assertFalse(procedures.isEmpty());
		assertEquals(12, procedures.size());
	}
}
