package utd.claimsProcessing.dao;

import java.util.Collection;
import java.util.Map;

import utd.claimsProcessing.domain.Member;

/**
 * Manages and returns instances of Member.
 */
public class MemberDAO extends BaseDAO
{
	private Map<String, Member> members;
	private static MemberDAO singleton;
	
	public static MemberDAO getSingleton() throws Exception
	{
		if(singleton == null) {
			singleton = new MemberDAO();
		}
		return singleton;
	}
	
	private MemberDAO() throws Exception
	{
		members = loadFromStore("member");
	}
	
	/**
	 * Returns the instance with the given ID or null if none is found.
	 */
	public Member retrieveMember(String memberID) throws Exception
	{
		return members.get(memberID);
	}

	/**
	 * Returns instances managed by this DAO.
	 */
	public Collection<Member> retrieveAllMembers() throws Exception
	{
		return members.values();
	}

}
