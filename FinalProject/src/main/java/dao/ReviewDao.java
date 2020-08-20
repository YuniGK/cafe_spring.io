 package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import vo.ReviewVo;

public class ReviewDao{

	@Autowired
	private SqlSession sqlSession;
	
	public int insert (ReviewVo vo) {
		
		int res = sqlSession.insert("review.review_insert", vo);
		
		//실패시 0	
		return res;
	}
	
	public List<ReviewVo> selectList() {

		List<ReviewVo> list = sqlSession.selectList("review.review_list");
		
		return list;
	}
	

	public List<ReviewVo> select(int m_idx) {

		List<ReviewVo> list = sqlSession.selectList("review.my_review_list", m_idx);
		
		return list;
	}
	
	public ReviewVo selectOne(int r_idx) {

		ReviewVo vo = sqlSession.selectOne("review.review_list_one", r_idx);

		return vo;
	}
	
	public List<ReviewVo> selectList(int c_idx) {

		List<ReviewVo> list = sqlSession.selectList("review.review_list_cafe", c_idx);
		
		return list;
	}
	
	public ReviewVo select_review_avg(int c_idx) {

		ReviewVo vo = sqlSession.selectOne("review.review_avg_grade", c_idx);
		
		return vo;
	}
	
	public ReviewVo select_review_total(int c_idx) {

		ReviewVo vo = sqlSession.selectOne("review.review_total_grade", c_idx);
		
		return vo;
	}
	
	public int delete(int r_idx) {
		
		int res = sqlSession.delete("review.review_delete", r_idx);
		
		//실패시 0	
		return res;
	}
	
	public int update(ReviewVo vo) {
		
		int res = sqlSession.update("review.review_update", vo);
		
		//실패시 0	
		return res;
	}
	
	public int today_review() {

		int count = sqlSession.selectOne("review.today_review");
		
		return count;
	}
	
	public int my_review(int m_idx) {

		int count = sqlSession.selectOne("review.my_review", m_idx);
		
		return count;
	}
	
}
