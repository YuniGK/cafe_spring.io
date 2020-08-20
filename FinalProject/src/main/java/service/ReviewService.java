package service;

import java.util.List;

import vo.ReviewVo;

public interface ReviewService {

	public int insert (ReviewVo vo);
	
	public List<ReviewVo> selectList();

	public List<ReviewVo> select(int m_idx);
	
	public ReviewVo selectOne(int r_idx);
	
	public List<ReviewVo> selectList(int c_idx);
	
	public ReviewVo select_review_avg(int c_idx);
	
	public ReviewVo select_review_total(int c_idx);
	
	public int delete(int r_idx);
	
	public int update(ReviewVo vo);
	
	public int today_review();
	
	public int my_review(int m_idx);
	
}
