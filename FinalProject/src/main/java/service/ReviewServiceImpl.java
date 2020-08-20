package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.ReviewDao;
import vo.ReviewVo;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewDao dao;
	
	public int insert (ReviewVo vo) {
		return dao.insert(vo);
	}
	
	public List<ReviewVo> selectList() {
		List<ReviewVo> list = dao.selectList();
		
		return list;
	}
	

	public List<ReviewVo> select(int m_idx) {

		List<ReviewVo> list = dao.select(m_idx);
		
		return list;
	}
	
	public ReviewVo selectOne(int r_idx) {

		ReviewVo vo = dao.selectOne(r_idx);

		return vo;
	}
	
	public List<ReviewVo> selectList(int c_idx) {

		List<ReviewVo> list = dao.selectList(c_idx);
		
		return list;
	}
	
	public ReviewVo select_review_avg(int c_idx) {

		ReviewVo vo = dao.select_review_avg(c_idx);
		
		return vo;
	}
	
	public ReviewVo select_review_total(int c_idx) {

		ReviewVo vo = dao.select_review_total(c_idx);
		
		return vo;
	}
	
	public int delete(int r_idx) {
		return dao.delete(r_idx);
	}
	
	public int update(ReviewVo vo) {
		return dao.update(vo);
	}
	
	public int today_review() {
		return dao.today_review();
	}
	
	public int my_review(int m_idx) {
		return dao.my_review(m_idx);
	}
	
}
