package utd.claimsProcessing.util;

import utd.claimsProcessing.domain.Claim;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This is a utility class that is used to load an instance of Claim from an XML
 * document, or to create the XML from a provided instance of Claim.
 */
public class ClaimReader
{
	private static ClaimReader singleton;
	private XStream xStream;

	public static ClaimReader getSingleton()
	{
		if (singleton == null) {
			singleton = new ClaimReader();
		}
		return singleton;
	}

	private ClaimReader()
	{
		initialize();
	}

	/**
	 * Initialize the XStream library for XML processing. 
	 */
	private void initialize()
	{
		xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		xStream.alias("claim", Claim.class);
	}

	/**
	 * Create an instance of Claim from the given XML document. 
	 */
	public Claim loadClaimFromString(String claimXML) throws XStreamException
	{
		Claim claim = (Claim) xStream.fromXML(claimXML);
		return claim;
	}

	/**
	 * Convert the given instance of Claim into an XML document. 
	 */
	public String buildXmlFromClaim(Claim claim) throws XStreamException
	{
		if (claim == null) {
			return "";
		}
		else {
			String claimXML = xStream.toXML(claim);
			return claimXML;
		}
	}

}
