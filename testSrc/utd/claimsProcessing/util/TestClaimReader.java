package utd.claimsProcessing.util;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;

import utd.claimsProcessing.domain.Claim;

import com.thoughtworks.xstream.converters.ConversionException;

public class TestClaimReader extends TestCase
{
	@Test
	public void testReadClaim() throws Exception
	{
		File file = new File("claims", "claim10.xml");
		String claimXML = FileUtils.readXML(file);

		ClaimReader claimReader = ClaimReader.getSingleton();
		Claim claim = claimReader.loadClaimFromString(claimXML);
		assertNotNull(claim);
		
		assertEquals("member1", claim.getMemberID());
		assertEquals("provider1", claim.getProviderID());
		assertEquals("GP101", claim.getProcedureCode());
		assertNotNull(claim.getDescription());
	}

	@Test
	public void testMissingClaim() throws Exception
	{
		try {
			File file = new File("claims", "claim999.xml");
			String claimXML = FileUtils.readXML(file);
		}
		catch(IOException ex) {
			return;
		}
		fail("Invalid Claim Filename Not Caught");
	}
	
	@Test
	public void testBrokenClaim() throws Exception
	{
		try {
			File file = new File("claims", "brokenClaim.xml");
			String claimXML = FileUtils.readXML(file);
			
			ClaimReader claimReader = ClaimReader.getSingleton();
			Claim claim = claimReader.loadClaimFromString(claimXML);
			assertNotNull(claim);
		}
		catch(ConversionException ex) {
			return;
		}
		fail("Invalid ProcedureCategory Not Caught");
	}
}
