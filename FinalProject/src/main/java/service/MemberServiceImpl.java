package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.MemberDao;
import vo.MemberVo;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDao dao;

	public MemberVo selectOne(String m_id) {
		MemberVo vo = dao.selectOne(m_id);
		return vo;
	}

	public int insert(MemberVo vo) {
		return dao.insert(vo);
	}

	public List<MemberVo> selectList() {
		List<MemberVo> list = dao.selectList();
		return list;
	}

	public int delete(int m_idx) {
		return dao.delete(m_idx);
	}

	public MemberVo selectOne(int m_idx) {
		MemberVo vo = dao.selectOne(m_idx);
		
		return vo;
	}

	public int update(MemberVo vo) {
		return dao.update(vo);
	}

	public int todayMember() {
		return dao.todayMember();
	}
	
}
