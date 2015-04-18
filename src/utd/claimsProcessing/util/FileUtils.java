package utd.claimsProcessing.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils
{
	/**
	 * Read the XML document from the file at the given path. The
	 * path must be either a full path, or relative to the application's
	 * working directory. 
	 */
	public static String readXML(File file) throws IOException
	{
		try {
			if (!file.canRead()) {
				throw new IOException("Cant find or read file " + file.getName());
			}

			byte[] buffer = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(buffer);
			fis.close();
			return new String(buffer);
		}
		catch (Exception ex) {
			throw new IOException("IO Error reading xml ", ex);
		}
	}

}
