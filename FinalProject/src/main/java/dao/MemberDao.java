package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import vo.MemberVo;

public class MemberDao{
	
	@Autowired
	SqlSession sqlSession;

	//아이디 체크 --------------------------------------------------------
	public MemberVo selectOne(String m_id) {
		
		MemberVo vo = sqlSession.selectOne("member.member_one", m_id);
		
		return vo;
	}
	
	//insert -----------------------------------------------------------
	public int insert(MemberVo vo) {
		
		int res = sqlSession.insert("member.member_insert", vo);
		
		//실패시 0	
		return res;
	}

	//목록조회 --------------------------------------------------------
	public List<MemberVo> selectList() {
		
		List<MemberVo> list = sqlSession.selectList("member.member_list");
		
		return list;
	}

	//삭제 --------------------------------------------------------
	public int delete(int m_idx) {
		
		int res = sqlSession.delete("member.member_delete", m_idx);
				
		return res;
	}

	//회원 한명 조회 --------------------------------------------------------
	public MemberVo selectOne(int m_idx) {
		
		MemberVo vo = sqlSession.selectOne("member.member_one_list", m_idx);
		
		return vo;
	}

	//회원 정보 수정 ---------------------------------------------------------
	public int update(MemberVo vo) {
		
		int res = sqlSession.update("member.member_update", vo);
		
		return res;
	}	
	
	public int todayMember() {

		int count = sqlSession.selectOne("member.today_member");

		return count;
	}
	
}