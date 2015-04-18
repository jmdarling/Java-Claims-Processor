package utd.claimsProcessing.dao;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import utd.claimsProcessing.domain.Member;

public class MemberDAOTest extends TestCase
{
	@Test
	public void testLoad() throws Exception
	{
		MemberDAO memberDAO = MemberDAO.getSingleton();
		Member member = memberDAO.retrieveMember("member1");
		assertNotNull(member);
		
		assertEquals("Ellyn", member.getFirstName());
		assertEquals("Calchera", member.getLastName());
		assertEquals("policy1", member.getPolicyID());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S z");
		Date expectedDate = sdf.parse("2004-02-22 00:00:00.0 CST");
		assertEquals(expectedDate, member.getDob());
	}

	@Test
	public void testLoadFailure() throws Exception
	{
		MemberDAO memberDAO = MemberDAO.getSingleton();
		Member member = memberDAO.retrieveMember("member99");
		assertNull(member);
	}

	@Test
	public void testLoadAll() throws Exception
	{
		MemberDAO memberDAO = MemberDAO.getSingleton();
		Collection<Member> members = memberDAO.retrieveAllMembers();
		assertFalse(members.isEmpty());
		assertEquals(6, members.size());
	}
}
