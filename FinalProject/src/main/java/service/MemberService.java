package service;

import java.util.List;

import vo.MemberVo;

public interface MemberService {

	//아이디 체크 --------------------------------------------------------
	public MemberVo selectOne(String m_id);
	
	//insert -----------------------------------------------------------
	public int insert(MemberVo vo);

	//목록조회 --------------------------------------------------------
	public List<MemberVo> selectList();

	//삭제 --------------------------------------------------------
	public int delete(int m_idx);

	//회원 한명 조회 --------------------------------------------------------
	public MemberVo selectOne(int m_idx);

	//회원 정보 수정 ---------------------------------------------------------
	public int update(MemberVo vo);
	
	public int todayMember();
	
}
