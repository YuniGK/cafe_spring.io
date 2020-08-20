package dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import vo.HitVo;

public class HitDao{

	@Autowired
	private SqlSession sqlSession;
	
	public HitVo selectOne(int c_idx) {

		HitVo vo = sqlSession.selectOne("hit.hit_select", c_idx);
		
		return vo;
		
	}
	
	public int insert(int c_idx) {
		
		int res = sqlSession.insert("hit.hit_insert", c_idx);
		
		//실패시 0	
		return res;
	}
	
	public int update(HitVo vo) {
		
		int res = sqlSession.update("hit.hit_update", vo);
		
		//실패시 0	
		return res;
	}
	
}
