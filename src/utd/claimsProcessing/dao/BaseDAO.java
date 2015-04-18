package utd.claimsProcessing.dao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import utd.claimsProcessing.domain.EntityObject;
import utd.claimsProcessing.domain.Member;
import utd.claimsProcessing.domain.Policy;
import utd.claimsProcessing.domain.Procedure;
import utd.claimsProcessing.domain.Provider;
import utd.claimsProcessing.util.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This is the base class for all of the application's DAO. This application
 * provides for read-only persistence by retrieving objects from XML documents
 * contained in files.
 * 
 * It is important to note that all of the objects retrieved by these DAOs will
 * be sub-classes of EntityObject. This is important because objects are managed
 * in Maps which are keyed on EntityObject's ID attribute.
 * 
 * It is expected that entity objects are created by the user with a text
 * editor. DAO's can only read objects from XML, and can not store an object's
 * state back to its file.
 */
public abstract class BaseDAO
{
	/**
	 * This method will load all of the files contained in the given file which
	 * is assumed to be a director. It is further assumed that the given
	 * directory is a sub-directory of a directory named "xmlStore". The
	 * directory xmlStore is assumed to be located on the application's
	 * classpath. This method assumes that all of the files in the given
	 * directory xmlStore/<storeDir> contain XML documents that will be
	 * converted into Java domain objects. Finally, each of the objects loaded
	 * from files will be placed into a Map that is keyed by the EntityObject's
	 * ID attribute.
	 */
	Map loadFromStore(String storeDir) throws Exception
	{
		Map result = new HashMap();

		// Get all files from the correct directory
		File targetDir = new File("xmlStore", storeDir);
		if (!targetDir.exists()) {
			throw new Exception("Can not find store directory " + storeDir);
		}

		File objectFiles[] = targetDir.listFiles();
		for (File file : objectFiles) {
			EntityObject entity = loadEntityFromFile(file);
			result.put(entity.getID(), entity);
		}
		return result;
	}

	/**
	 * This method uses the XStream library to retrieve a domain object from the
	 * given file. In this application, all domain objects subclass
	 * EntityObject.
	 */
	protected EntityObject loadEntityFromFile(File file) throws Exception
	{
		String xmlData = FileUtils.readXML(file);
		XStream xStream = new XStream(new DomDriver());
		xStream.autodetectAnnotations(true);
		xStream.alias("member", Member.class);
		xStream.alias("policy", Policy.class);
		xStream.alias("provider", Provider.class);
		xStream.alias("procedure", Procedure.class);

		EntityObject entity = (EntityObject) xStream.fromXML(xmlData);
		return entity;
	}

}
