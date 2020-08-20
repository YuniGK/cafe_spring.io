package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import vo.MemberVo;

public class MemberDao{
	
	@Autowired
	SqlSession sqlSession;

	//���̵� üũ --------------------------------------------------------
	public MemberVo selectOne(String m_id) {
		
		MemberVo vo = sqlSession.selectOne("member.member_one", m_id);
		
		return vo;
	}
	
	//insert -----------------------------------------------------------
	public int insert(MemberVo vo) {
		
		int res = sqlSession.insert("member.member_insert", vo);
		
		//���н� 0	
		return res;
	}

	//�����ȸ --------------------------------------------------------
	public List<MemberVo> selectList() {
		
		List<MemberVo> list = sqlSession.selectList("member.member_list");
		
		return list;
	}

	//���� --------------------------------------------------------
	public int delete(int m_idx) {
		
		int res = sqlSession.delete("member.member_delete", m_idx);
				
		return res;
	}

	//ȸ�� �Ѹ� ��ȸ --------------------------------------------------------
	public MemberVo selectOne(int m_idx) {
		
		MemberVo vo = sqlSession.selectOne("member.member_one_list", m_idx);
		
		return vo;
	}

	//ȸ�� ���� ���� ---------------------------------------------------------
	public int update(MemberVo vo) {
		
		int res = sqlSession.update("member.member_update", vo);
		
		return res;
	}	
	
	public int todayMember() {

		int count = sqlSession.selectOne("member.today_member");

		return count;
	}
	
}