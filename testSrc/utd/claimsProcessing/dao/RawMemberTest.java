package utd.claimsProcessing.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import utd.claimsProcessing.domain.Member;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class RawMemberTest {

	public static void main(String[] args) {
		RawMemberTest personTest = new RawMemberTest();
		try {
			personTest.runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void runTest() throws Exception {
		String memberData = readMember("xmlStore/members/member1.xml");
		System.out.println(memberData);
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("member", Member.class);
		Member member = (Member) xStream.fromXML(memberData);

		System.out.println(member);
	}

	private String readMember(String filename) throws IOException {
		try {

			File file = new File(filename);
			if (!file.canRead()) {
				throw new IOException("Cant read file on classpath " + filename);
			}

			byte[] buffer = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(buffer);
			fis.close();
			return new String(buffer);
		} catch (Exception ex) {
			throw new IOException("Error reading member ", ex);
		}
	}
}
