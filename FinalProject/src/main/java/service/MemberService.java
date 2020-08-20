package service;

import java.util.List;

import vo.MemberVo;

public interface MemberService {

	//���̵� üũ --------------------------------------------------------
	public MemberVo selectOne(String m_id);
	
	//insert -----------------------------------------------------------
	public int insert(MemberVo vo);

	//�����ȸ --------------------------------------------------------
	public List<MemberVo> selectList();

	//���� --------------------------------------------------------
	public int delete(int m_idx);

	//ȸ�� �Ѹ� ��ȸ --------------------------------------------------------
	public MemberVo selectOne(int m_idx);

	//ȸ�� ���� ���� ---------------------------------------------------------
	public int update(MemberVo vo);
	
	public int todayMember();
	
}
